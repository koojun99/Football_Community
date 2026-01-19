package honajun.football_community.fixture.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import honajun.football_community.fixture.event.dto.FixtureEventDTO;
import honajun.football_community.fixture.event.service.FixtureEventService;
import honajun.football_community.global.enums.fixture.FixtureStatus;
import honajun.football_community.global.http.FixtureApiClient;
import honajun.football_community.fixture.mapper.FixtureMapper;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CachePut;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 경기 정보를 주기적으로 업데이트하는 스케줄러
 * 외부 API 호출을 최적화하여 비용을 절감합니다.
 * - 최대 15초에 1회, 최소 1분에 1회 업데이트
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class FixtureUpdateScheduler {

    private final FixtureApiClient fixtureApiClient;
    private final FollowQueryAdapter followQueryAdapter;
    private final FixtureEventService fixtureEventService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    // API 호출 제한을 위한 마지막 호출 시간 추적 (fixtureId/teamId -> 마지막 호출 시간)
    private final ConcurrentHashMap<String, Long> lastApiCallTime = new ConcurrentHashMap<>();

    // 이전 이벤트 상태 저장 (중복 이벤트 방지)
    private final ConcurrentHashMap<Long, String> lastEventState = new ConcurrentHashMap<>();

    // 스킵해야 할 경기 추적 (NS, HT, 종료된 경기는 스케줄링에서 제외)
    // 값: "NS", "HT", "FINISHED" - 상태에 따라 다른 처리
    private final ConcurrentHashMap<Long, String> skippedFixtures = new ConcurrentHashMap<>();

    // NS/HT 상태 경기의 마지막 확인 시간 (상태 변경 확인용)
    private final ConcurrentHashMap<Long, Long> skippedFixtureCheckTime = new ConcurrentHashMap<>();

    // NS 상태 경기의 fixtureDate 저장 (동적 간격 계산용)
    private final ConcurrentHashMap<Long, LocalDateTime> nsFixtureDates = new ConcurrentHashMap<>();

    // HT 상태 경기 확인 간격: 3분 (180000ms)
    private static final long HT_CHECK_INTERVAL = 180_000;

    // NS 상태 경기 확인 간격 상수
    private static final long NS_CHECK_INTERVAL_HOURLY = 60 * 60 * 1000L; // 1시간
    private static final long NS_CHECK_INTERVAL_10MIN = 10 * 60 * 1000L; // 10분

    // Follow된 fixtureId 목록 캐싱 (DB 쿼리 최적화)
    private volatile List<Long> cachedFollowedFixtureIds = List.of();
    private volatile long lastFollowedFixtureIdsUpdate = 0;
    private static final long FOLLOWED_FIXTURE_IDS_CACHE_TTL = 30_000; // 30초 캐시

    // 최소 호출 간격: 1분 (60000ms)
    // 요구사항: 최대 15초에 1회, 최소 1분에 1회
    // 실제 구현: 최소 1분 간격으로 제한하여 비용 최적화
    private static final long MIN_CALL_INTERVAL = 60_000;

    /**
     * Follow된 fixtureId 목록을 조회합니다 (캐싱 적용).
     */
    private List<Long> getFollowedFixtureIds() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastFollowedFixtureIdsUpdate > FOLLOWED_FIXTURE_IDS_CACHE_TTL) {
            synchronized (this) {
                // Double-check locking
                if (currentTime - lastFollowedFixtureIdsUpdate > FOLLOWED_FIXTURE_IDS_CACHE_TTL) {
                    cachedFollowedFixtureIds = followQueryAdapter.findAllDistinctFixtureIds();
                    lastFollowedFixtureIdsUpdate = currentTime;
                    log.debug("Updated cached followed fixtureIds: {}", cachedFollowedFixtureIds.size());
                }
            }
        }
        return cachedFollowedFixtureIds;
    }

    /**
     * Follow된 모든 경기의 상세 정보를 주기적으로 업데이트합니다.
     * Follow된 경기만 자동으로 스케줄링하여 업데이트합니다.
     * 1분마다 실행되며, 각 경기별로 최소 1분 간격으로 API를 호출합니다.
     */
    @Scheduled(fixedRate = 60_000) // 1분마다 실행
    public void updateFollowedMatchFixtures() {
        try {
            // Follow된 모든 경기 조회 (캐싱된 fixtureId 목록)
            List<Long> followedFixtureIds = getFollowedFixtureIds();

            // Follow된 경기가 없으면 스킵
            if (followedFixtureIds == null || followedFixtureIds.isEmpty()) {
                log.debug("No followed fixtures found, skipping update");
                return;
            }

            log.debug("Updating {} followed fixtures", followedFixtureIds.size());

            for (Long fixtureId : followedFixtureIds) {
                String skipReason = skippedFixtures.get(fixtureId);

                // 종료된 경기는 완전히 스킵
                if ("FINISHED".equals(skipReason)) {
                    continue;
                }

                // NS/HT 상태 경기는 주기적으로 확인 (상태 변경 체크)
                if (skipReason != null) {
                    long currentTime = System.currentTimeMillis();
                    Long lastCheckTime = skippedFixtureCheckTime.get(fixtureId);

                    // 상태별 확인 간격 계산
                    long checkInterval = calculateCheckInterval(skipReason, fixtureId);

                    // 마지막 확인 후 계산된 간격이 지나지 않았으면 스킵
                    if (lastCheckTime != null && (currentTime - lastCheckTime) < checkInterval) {
                        continue;
                    }

                    // 주기적으로 상태 확인
                    try {
                        Object fixtureData = updateFixtureCache(fixtureId);
                        checkSkippedFixtureStatus(fixtureId, fixtureData);
                        skippedFixtureCheckTime.put(fixtureId, currentTime);
                    } catch (IOException e) {
                        // 존재하지 않는 fixtureId인 경우 스킵 목록에서 제거
                        if (e.getMessage() != null && e.getMessage().contains("Fixture not found")) {
                            log.warn("Fixture {} no longer exists, removing from skipped list", fixtureId);
                            skippedFixtures.remove(fixtureId);
                            skippedFixtureCheckTime.remove(fixtureId);
                            if ("NS".equals(skipReason)) {
                                nsFixtureDates.remove(fixtureId);
                            }
                        } else {
                            log.warn("Failed to check skipped fixture status for fixtureId: {}: {}", 
                                    fixtureId, e.getMessage());
                        }
                    }
                    continue;
                }

                String cacheKey = "fixture::" + fixtureId;
                long currentTime = System.currentTimeMillis();
                Long lastCallTime = lastApiCallTime.get(cacheKey);

                // 최소 간격(1분)이 지나지 않았으면 스킵
                if (lastCallTime != null && (currentTime - lastCallTime) < MIN_CALL_INTERVAL) {
                    continue;
                }

                try {
                    // 경기 정보 업데이트 및 스킵 상태 확인
                    Object fixtureData = updateFixtureCache(fixtureId);
                    checkAndMarkSkipped(fixtureId, fixtureData);
                    lastApiCallTime.put(cacheKey, currentTime);
                } catch (IOException e) {
                    // 존재하지 않는 fixtureId이거나 API 에러인 경우
                    // 다음 스케줄링에서 다시 시도하도록 로그만 남기고 스킵
                    log.warn("Failed to update fixture for fixtureId: {} (may not exist or API error: {})", 
                            fixtureId, e.getMessage());
                }
            }
        } catch (Exception e) {
            log.error("Error in updateFollowedMatchFixtures scheduler", e);
        }
    }

    /**
     * 자정마다 오늘 진행되는 Fixture가 포함된 Follow가 있는지 확인합니다.
     * NS 상태 경기 중 오늘 날짜인 경기를 찾아서 스케줄링에 포함시킵니다.
     */
    @Scheduled(cron = "0 0 0 * * *") // 매일 자정 (00:00:00)
    public void checkTodayFixtures() {
        try {
            log.info("Checking today's fixtures at midnight");

            // Follow된 모든 경기 조회
            List<Long> followedFixtureIds = followQueryAdapter.findAllDistinctFixtureIds();

            if (followedFixtureIds == null || followedFixtureIds.isEmpty()) {
                log.debug("No followed fixtures found, skipping today's fixture check");
                return;
            }

            LocalDate today = LocalDate.now(ZoneId.of("Asia/Seoul"));
            int checkedCount = 0;
            int todayFixtureCount = 0;

            for (Long fixtureId : followedFixtureIds) {
                // NS 상태로 스킵된 경기만 확인
                String skipReason = skippedFixtures.get(fixtureId);
                if (!"NS".equals(skipReason)) {
                    continue;
                }

                // fixtureDate가 없으면 스킵
                LocalDateTime fixtureDate = nsFixtureDates.get(fixtureId);
                if (fixtureDate == null) {
                    continue;
                }

                checkedCount++;
                LocalDate fixtureDateOnly = fixtureDate.toLocalDate();

                // 오늘 날짜인 NS 상태 경기 발견
                if (fixtureDateOnly.equals(today)) {
                    todayFixtureCount++;
                    // 스킵 목록에서 제거하여 정상 스케줄링에 포함
                    // (다음 updateFollowedMatchFixtures 실행 시 자동으로 처리됨)
                    skippedFixtures.remove(fixtureId);
                    skippedFixtureCheckTime.remove(fixtureId);
                    log.info("Fixture {} (NS status) is scheduled for today, resuming normal scheduling", fixtureId);
                }
            }

            log.info("Today's fixture check completed: checked {} NS fixtures, found {} today's fixtures",
                    checkedCount, todayFixtureCount);
        } catch (Exception e) {
            log.error("Error in checkTodayFixtures scheduler", e);
        }
    }

    /**
     * Follow된 모든 경기의 실시간 이벤트를 수집합니다.
     * Follow 여부만으로 이벤트를 수집하므로, 모든 Follow된 경기에 대해 이벤트를 가져옵니다.
     * 15초마다 실행되며, 각 경기별로 최소 1분 간격으로 API를 호출합니다.
     */
    @Scheduled(fixedRate = 15_000) // 15초마다 실행
    public void collectFixtureEvents() {
        try {
            // Follow된 모든 경기 조회 (캐싱된 fixtureId 목록)
            List<Long> followedFixtureIds = getFollowedFixtureIds();

            // Follow된 경기가 없으면 스킵
            if (followedFixtureIds == null || followedFixtureIds.isEmpty()) {
                log.debug("No followed fixtures found, skipping event collection");
                return;
            }

            log.debug("Collecting events for {} followed fixtures", followedFixtureIds.size());

            for (Long fixtureId : followedFixtureIds) {
                String skipReason = skippedFixtures.get(fixtureId);

                // 스킵된 경기는 이벤트 수집에서 제외 (NS/HT는 상태 확인만 하고 이벤트는 수집하지 않음)
                if (skipReason != null) {
                    continue;
                }

                String cacheKey = "fixtureEvents::" + fixtureId;
                long currentTime = System.currentTimeMillis();
                Long lastCallTime = lastApiCallTime.get(cacheKey);

                // 최소 간격(1분)이 지나지 않았으면 스킵
                if (lastCallTime != null && (currentTime - lastCallTime) < MIN_CALL_INTERVAL) {
                    continue;
                }

                try {
                    // 실시간 이벤트 수집 (향후 알림 발송 등에 사용)
                    collectEventsForFixture(fixtureId);
                    lastApiCallTime.put(cacheKey, currentTime);
                } catch (IOException e) {
                    log.error("Failed to collect events for fixtureId: {}", fixtureId, e);
                }
            }
        } catch (Exception e) {
            log.error("Error in collectFixtureEvents scheduler", e);
        }
    }

    /**
     * 특정 경기의 실시간 이벤트를 수집하고 브로드캐스팅합니다.
     */
    private void collectEventsForFixture(Long fixtureId) throws IOException {
        // 실시간 이벤트 수집
        String eventsJson = fixtureApiClient.getFixtureEvents(fixtureId, null, null, null);
        log.debug("Collected events for fixtureId: {}, events: {}", fixtureId, eventsJson);

        // 이벤트가 변경되었는지 확인 (중복 브로드캐스팅 방지)
        String previousState = lastEventState.get(fixtureId);
        if (previousState != null && previousState.equals(eventsJson)) {
            return; // 변경사항이 없으면 스킵
        }

        // 이벤트 파싱 및 브로드캐스팅
        try {
            JsonNode rootNode = objectMapper.readTree(eventsJson);
            JsonNode responseNode = rootNode.get("response");

            if (responseNode != null && responseNode.isArray()) {
                for (JsonNode eventNode : responseNode) {
                    // 각 이벤트를 브로드캐스팅
                    FixtureEventDTO eventDTO = FixtureEventDTO.builder()
                            .fixtureId(fixtureId)
                            .eventType(eventNode.has("type") ? eventNode.get("type").asText() : "unknown")
                            .eventData(eventNode.toString())
                            .timestamp(System.currentTimeMillis())
                            .build();

                    fixtureEventService.broadcastEvent(fixtureId, eventDTO);
                }
            }

            // 현재 상태 저장
            lastEventState.put(fixtureId, eventsJson);
        } catch (Exception e) {
            log.error("Failed to parse and broadcast events for fixtureId: {}", fixtureId, e);
        }
    }

    /**
     * 특정 팀의 경기 정보를 캐시에 업데이트합니다.
     */
    @CachePut(cacheNames = "teamFixtures", key = "#teamId")
    public Object updateTeamFixtureCache(Long teamId) throws IOException {
        // teamId는 API-Football에서 사용하는 ID를 직접 사용
        String apiResult = fixtureApiClient.getTeamFixtures(teamId, "2024", 10, "Asia/Seoul");
        return FixtureMapper.toGetFixtures(apiResult);
    }

    /**
     * 특정 경기의 상세 정보를 캐시에 업데이트합니다.
     */
    @CachePut(cacheNames = "fixtures", key = "#fixtureId")
    public Object updateFixtureCache(Long fixtureId) throws IOException {
        String apiResult = fixtureApiClient.getFixture(fixtureId, "Asia/Seoul");
        return FixtureMapper.toGetFixture(apiResult);
    }

    /**
     * 경기 데이터에서 스킵해야 할 상태(NS, HT, 종료)를 확인하고 마킹합니다.
     */
    private void checkAndMarkSkipped(Long fixtureId, Object fixtureData) {
        try {
            if (fixtureData instanceof honajun.football_community.fixture.dto.FixtureResponseDTO.getFixture) {
                honajun.football_community.fixture.dto.FixtureResponseDTO.getFixture fixture = (honajun.football_community.fixture.dto.FixtureResponseDTO.getFixture) fixtureData;

                FixtureStatus status = fixture.getFixtureStatus();
                if (status != null && status.shouldSkipScheduling()) {
                    String skipReason = determineSkipReason(status);
                    skippedFixtures.put(fixtureId, skipReason);
                    skippedFixtureCheckTime.put(fixtureId, System.currentTimeMillis());

                    // NS 상태인 경우 fixtureDate 저장 (동적 간격 계산용)
                    if (status == FixtureStatus.NS && fixture.getFixtureDate() != null) {
                        nsFixtureDates.put(fixtureId, fixture.getFixtureDate());
                    }

                    log.info("Fixture {} marked as skipped with status: {} (reason: {})", fixtureId, status,
                            skipReason);
                }
            }
        } catch (Exception e) {
            log.debug("Failed to check fixture status for fixtureId: {}", fixtureId, e);
        }
    }

    /**
     * 스킵된 경기(NS/HT)의 상태를 확인하여 진행 중 상태로 변경되었는지 체크합니다.
     * 진행 중 상태로 변경되면 스킵 목록에서 제거합니다.
     */
    private void checkSkippedFixtureStatus(Long fixtureId, Object fixtureData) {
        try {
            if (fixtureData instanceof honajun.football_community.fixture.dto.FixtureResponseDTO.getFixture) {
                honajun.football_community.fixture.dto.FixtureResponseDTO.getFixture fixture = (honajun.football_community.fixture.dto.FixtureResponseDTO.getFixture) fixtureData;

                FixtureStatus status = fixture.getFixtureStatus();
                if (status != null) {
                    String currentSkipReason = skippedFixtures.get(fixtureId);

                    // 진행 중 상태로 변경되었으면 스킵 목록에서 제거
                    if (!status.shouldSkipScheduling()) {
                        skippedFixtures.remove(fixtureId);
                        skippedFixtureCheckTime.remove(fixtureId);
                        nsFixtureDates.remove(fixtureId); // NS fixtureDate도 제거
                        log.info("Fixture {} resumed scheduling - status changed to: {}", fixtureId, status);
                    } else {
                        // 여전히 스킵 상태이지만 상태가 변경되었을 수 있음 (예: NS -> HT)
                        String newSkipReason = determineSkipReason(status);
                        if (!newSkipReason.equals(currentSkipReason)) {
                            skippedFixtures.put(fixtureId, newSkipReason);
                            // HT로 변경되면 fixtureDate 제거 (더 이상 필요 없음)
                            if ("HT".equals(newSkipReason)) {
                                nsFixtureDates.remove(fixtureId);
                            }
                            // NS로 변경되면 fixtureDate 저장
                            else if ("NS".equals(newSkipReason) && fixture.getFixtureDate() != null) {
                                nsFixtureDates.put(fixtureId, fixture.getFixtureDate());
                            }
                            log.debug("Fixture {} skip reason updated: {} -> {}", fixtureId, currentSkipReason,
                                    newSkipReason);
                        } else if ("NS".equals(newSkipReason) && fixture.getFixtureDate() != null) {
                            // NS 상태가 유지되지만 fixtureDate가 업데이트되었을 수 있음
                            nsFixtureDates.put(fixtureId, fixture.getFixtureDate());
                        }
                    }
                }
            }
        } catch (Exception e) {
            log.debug("Failed to check skipped fixture status for fixtureId: {}", fixtureId, e);
        }
    }

    /**
     * 경기 상태에 따라 스킵 이유를 결정합니다.
     */
    private String determineSkipReason(FixtureStatus status) {
        if (status == FixtureStatus.NS) {
            return "NS";
        } else if (status == FixtureStatus.HT) {
            return "HT";
        } else if (status.isFinished()) {
            return "FINISHED";
        }
        return "UNKNOWN";
    }

    /**
     * 스킵된 경기의 상태 확인 간격을 계산합니다.
     * - HT: 3분마다 확인
     * - NS: fixtureDate를 기반으로 동적 계산
     * - 같은 날짜: 1시간마다
     * - 경기 시작까지 1시간 이하: 10분마다
     * - 다른 날짜: 확인하지 않음 (Long.MAX_VALUE)
     */
    private long calculateCheckInterval(String skipReason, Long fixtureId) {
        if ("HT".equals(skipReason)) {
            return HT_CHECK_INTERVAL; // 3분
        } else if ("NS".equals(skipReason)) {
            LocalDateTime fixtureDate = nsFixtureDates.get(fixtureId);
            if (fixtureDate == null) {
                // fixtureDate가 없으면 확인하지 않음
                return Long.MAX_VALUE;
            }

            LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Seoul"));
            LocalDate fixtureDateOnly = fixtureDate.toLocalDate();
            LocalDate nowDateOnly = now.toLocalDate();

            // 다른 날짜면 확인하지 않음
            if (!fixtureDateOnly.equals(nowDateOnly)) {
                return Long.MAX_VALUE;
            }

            // 같은 날짜면 경기 시작까지 남은 시간 계산
            Duration timeUntilFixture = Duration.between(now, fixtureDate);
            long minutesUntilFixture = timeUntilFixture.toMinutes();

            // 경기 시작까지 1시간(60분) 이하면 10분마다 확인
            if (minutesUntilFixture <= 60) {
                return NS_CHECK_INTERVAL_10MIN;
            }

            // 같은 날짜이지만 1시간 이상 남았으면 1시간마다 확인
            return NS_CHECK_INTERVAL_HOURLY;
        }

        // 기본값 (FINISHED 등)
        return Long.MAX_VALUE; // 종료된 경기는 확인하지 않음
    }
}
