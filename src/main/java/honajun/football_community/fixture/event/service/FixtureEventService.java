package honajun.football_community.fixture.event.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import honajun.football_community.fixture.event.dto.FixtureEventDTO;
import honajun.football_community.fixture.repository.FollowRepository;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

/**
 * 실시간 경기 이벤트 브로드캐스팅 서비스
 * Follow된 fixtureId를 기반으로 모든 member들에게 이벤트를 브로드캐스팅합니다.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class FixtureEventService {

    private final FollowRepository followRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    // fixtureId별로 연결된 SseEmitter 목록 관리
    private final ConcurrentHashMap<Long, List<SseEmitter>> fixtureEmitters = new ConcurrentHashMap<>();

    // Follow된 fixtureId 목록 캐싱 (DB 쿼리 최적화)
    private volatile Set<Long> cachedFollowedFixtureIds = ConcurrentHashMap.newKeySet();
    private volatile long lastFollowedFixtureIdsUpdate = 0;
    private static final long FOLLOWED_FIXTURE_IDS_CACHE_TTL = 30_000; // 30초 캐시

    /**
     * 특정 fixtureId에 대한 SSE 연결을 생성합니다.
     */
    public SseEmitter subscribe(Long fixtureId) {
        // SSE 타임아웃: 30분 (1,800,000ms)
        // 클라이언트는 주기적으로 재연결해야 함
        SseEmitter emitter = new SseEmitter(1_800_000L);

        emitter.onCompletion(() -> {
            log.debug("SSE connection completed for fixtureId: {}", fixtureId);
            removeEmitter(fixtureId, emitter);
        });

        emitter.onTimeout(() -> {
            log.debug("SSE connection timeout for fixtureId: {}", fixtureId);
            removeEmitter(fixtureId, emitter);
        });

        emitter.onError((ex) -> {
            log.debug("SSE connection error for fixtureId: {}", fixtureId, ex);
            removeEmitter(fixtureId, emitter);
        });

        // 해당 fixtureId를 Follow한 사용자인지 확인
        if (!isFixtureFollowed(fixtureId)) {
            try {
                emitter.send(SseEmitter.event()
                        .name("error")
                        .data("Fixture not followed"));
                emitter.complete();
            } catch (IOException e) {
                log.error("Failed to send error message", e);
            }
            return emitter;
        }

        // emitter 등록
        fixtureEmitters.computeIfAbsent(fixtureId, k -> new CopyOnWriteArrayList<>()).add(emitter);

        try {
            emitter.send(SseEmitter.event()
                    .name("connected")
                    .data("Connected to fixture " + fixtureId));
        } catch (IOException e) {
            log.error("Failed to send initial connection message", e);
            removeEmitter(fixtureId, emitter);
        }

        // 로그 레벨을 DEBUG로 변경 (INFO는 너무 상세함)
        int subscriberCount = fixtureEmitters.get(fixtureId).size();
        if (subscriberCount % 100 == 0 || subscriberCount <= 10) {
            log.info("New SSE subscriber for fixtureId: {}, total subscribers: {}",
                    fixtureId, subscriberCount);
        } else {
            log.debug("New SSE subscriber for fixtureId: {}, total subscribers: {}",
                    fixtureId, subscriberCount);
        }

        return emitter;
    }

    /**
     * 특정 fixtureId에 이벤트를 브로드캐스팅합니다.
     */
    public void broadcastEvent(Long fixtureId, FixtureEventDTO event) {
        List<SseEmitter> emitters = fixtureEmitters.get(fixtureId);
        if (emitters == null || emitters.isEmpty()) {
            return;
        }

        try {
            String eventJson = objectMapper.writeValueAsString(event);
            List<SseEmitter> deadEmitters = new CopyOnWriteArrayList<>();

            for (SseEmitter emitter : emitters) {
                try {
                    emitter.send(SseEmitter.event()
                            .name("fixture-event")
                            .data((Object) eventJson));
                } catch (Exception e) {
                    log.debug("Failed to send event to emitter, removing it", e);
                    deadEmitters.add(emitter);
                }
            }

            // 실패한 emitter 제거
            deadEmitters.forEach(emitter -> removeEmitter(fixtureId, emitter));

            log.debug("Broadcasted event to {} subscribers for fixtureId: {}",
                    emitters.size() - deadEmitters.size(), fixtureId);
        } catch (Exception e) {
            log.error("Failed to broadcast event for fixtureId: {}", fixtureId, e);
        }
    }

    /**
     * Follow된 fixtureId 목록을 조회합니다 (캐싱 적용, private).
     */
    private Set<Long> getCachedFollowedFixtureIds() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastFollowedFixtureIdsUpdate > FOLLOWED_FIXTURE_IDS_CACHE_TTL) {
            synchronized (this) {
                // Double-check locking
                if (currentTime - lastFollowedFixtureIdsUpdate > FOLLOWED_FIXTURE_IDS_CACHE_TTL) {
                    List<Long> followedIds = followRepository.findAllDistinctFixtureIds();
                    cachedFollowedFixtureIds = ConcurrentHashMap.newKeySet();
                    cachedFollowedFixtureIds.addAll(followedIds);
                    lastFollowedFixtureIdsUpdate = currentTime;
                    log.debug("Updated cached followed fixtureIds: {}", cachedFollowedFixtureIds.size());
                }
            }
        }
        return cachedFollowedFixtureIds;
    }

    /**
     * 특정 fixtureId가 Follow되었는지 확인합니다 (캐싱 사용).
     */
    private boolean isFixtureFollowed(Long fixtureId) {
        return getCachedFollowedFixtureIds().contains(fixtureId);
    }

    /**
     * Follow된 fixtureId 목록을 반환합니다 (public API).
     */
    public List<Long> getFollowedFixtureIds() {
        return followRepository.findAllDistinctFixtureIds();
    }

    /**
     * emitter를 제거합니다.
     */
    private void removeEmitter(Long fixtureId, SseEmitter emitter) {
        List<SseEmitter> emitters = fixtureEmitters.get(fixtureId);
        if (emitters != null) {
            emitters.remove(emitter);
            if (emitters.isEmpty()) {
                fixtureEmitters.remove(fixtureId);
            }
        }
    }

    /**
     * 모든 활성 구독자 수를 반환합니다.
     */
    public int getActiveSubscriberCount(Long fixtureId) {
        List<SseEmitter> emitters = fixtureEmitters.get(fixtureId);
        return emitters != null ? emitters.size() : 0;
    }
}
