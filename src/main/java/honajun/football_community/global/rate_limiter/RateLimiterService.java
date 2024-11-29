package honajun.football_community.global.rate_limiter;

import jakarta.servlet.http.HttpServletRequest;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RateLimiterService {

    private final StringRedisTemplate stringRedisTemplate;

    private static final String RATE_LIMIT_PREFIX = "rate_limit:";
    private static final int LIMIT = 5; // 요청 허용 횟수
    private static final Duration TIME_WINDOW = Duration.ofMinutes(1); // 제한 시간

    public boolean isAllowed(HttpServletRequest request) {
        String clientIp = getClientIp(request); // 클라이언트 IP 가져오기
        String key = RATE_LIMIT_PREFIX + clientIp;
        Long count = stringRedisTemplate.opsForValue().increment(key);

        // Redis에서 해당 IP의 요청 횟수 가져오기
        Long currentCount = stringRedisTemplate.opsForValue().increment(key, 1);

        if (currentCount == 1) {
            // 새로 생성된 키라면 TTL 설정
            stringRedisTemplate.expire(key, TIME_WINDOW);
        }

        // 요청 횟수가 제한을 초과하면 false 반환
        return currentCount <= LIMIT;
    }

    /**
     * 클라이언트 IP 주소를 가져오는 유틸 메서드
     */
    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty()) {
            ip = request.getRemoteAddr();
        } else {
            // X-Forwarded-For 값이 다수일 경우 첫 번째 값이 클라이언트 IP
            ip = ip.split(",")[0].trim();
        }
        return ip;
    }
}
