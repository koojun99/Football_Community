package honajun.football_community.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RedisService {

    private final RedisTemplate<String, String> redisTemplate;

    public void saveEmailCode(String email, String code, long expirationTime) {
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        valueOperations.set("email:verification:" + email, code, expirationTime, TimeUnit.MINUTES);
    }

    public void saveToken(String memberId, String token, long expirationTime) {
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        valueOperations.set(memberId , token, expirationTime, TimeUnit.MILLISECONDS);
    }

    public String getItem(String key) {
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        return valueOperations.get(key);
    }

    public void deleteItem(String key) {
        redisTemplate.delete(key);
    }
}
