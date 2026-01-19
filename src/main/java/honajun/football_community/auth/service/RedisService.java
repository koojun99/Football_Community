package honajun.football_community.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * Redis 관련 비즈니스 로직을 처리하는 서비스
 */
@Service
@RequiredArgsConstructor
public class RedisService {

    private final RedisTemplate<String, String> redisTemplate;

    /**
     * 토큰을 Redis에 저장합니다.
     */
    public void saveToken(String memberId, String token, long expirationTime) {
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        valueOperations.set(memberId, token, expirationTime, TimeUnit.MILLISECONDS);
    }

    /**
     * Redis에서 항목을 조회합니다.
     */
    public String getItem(String key) {
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        return valueOperations.get(key);
    }

    /**
     * Redis에서 항목을 삭제합니다.
     */
    public void deleteItem(String key) {
        redisTemplate.delete(key);
    }
}
