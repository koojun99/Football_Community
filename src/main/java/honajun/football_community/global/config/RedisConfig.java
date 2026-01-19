package honajun.football_community.global.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@EnableCaching
@Configuration
public class RedisConfig {

        @Value("${spring.data.redis.host}")
        private String host;

        @Value("${spring.data.redis.port}")
        private int port;

        @Bean
        public RedisConnectionFactory redisConnectionFactory() {
                RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
                redisStandaloneConfiguration.setHostName(host);
                redisStandaloneConfiguration.setPort(port);
                LettuceConnectionFactory lettuceConnectionFactory = new LettuceConnectionFactory(
                                redisStandaloneConfiguration);
                return lettuceConnectionFactory;
        }

        @Bean
        public RedisTemplate<String, Object> redisTemplate() {
                RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
                redisTemplate.setConnectionFactory(redisConnectionFactory());
                redisTemplate.setKeySerializer(new StringRedisSerializer());

                // Jackson2JsonRedisSerializer 설정
                ObjectMapper objectMapper = new ObjectMapper()
                                .setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY)
                                .activateDefaultTyping(LaissezFaireSubTypeValidator.instance,
                                                ObjectMapper.DefaultTyping.NON_FINAL);

                Jackson2JsonRedisSerializer<Object> serializer = new Jackson2JsonRedisSerializer<>(objectMapper,
                                Object.class);

                redisTemplate.setValueSerializer(serializer);

                return redisTemplate;
        }

        @Bean
        public CacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
                // ObjectMapper 생성 및 Jackson2JsonRedisSerializer 설정
                ObjectMapper objectMapper = new ObjectMapper()
                                .setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY)
                                .activateDefaultTyping(LaissezFaireSubTypeValidator.instance,
                                                ObjectMapper.DefaultTyping.NON_FINAL);
                objectMapper.registerModule(new JavaTimeModule());

                Jackson2JsonRedisSerializer<Object> serializer = new Jackson2JsonRedisSerializer<>(objectMapper,
                                Object.class);

                RedisCacheConfiguration defaultConfig = RedisCacheConfiguration.defaultCacheConfig()
                                .serializeValuesWith(
                                                RedisSerializationContext.SerializationPair.fromSerializer(serializer))
                                .entryTtl(Duration.ofMinutes(10));

                Map<String, RedisCacheConfiguration> cacheConfigurations = new HashMap<>();

                cacheConfigurations.put("teamFixtures", RedisCacheConfiguration.defaultCacheConfig()
                                .serializeValuesWith(
                                                RedisSerializationContext.SerializationPair.fromSerializer(serializer))
                                .entryTtl(Duration.ofMinutes(2))); // 스케줄러가 1분마다 업데이트하므로 2분 TTL
                cacheConfigurations.put("leagueFixtures", RedisCacheConfiguration.defaultCacheConfig()
                                .serializeValuesWith(
                                                RedisSerializationContext.SerializationPair.fromSerializer(serializer))
                                .entryTtl(Duration.ofHours(1)));
                cacheConfigurations.put("fixtures", RedisCacheConfiguration.defaultCacheConfig()
                                .serializeValuesWith(
                                                RedisSerializationContext.SerializationPair.fromSerializer(serializer))
                                .entryTtl(Duration.ofMinutes(1))); // 경기 상세 정보는 1분 TTL

                return RedisCacheManager.builder(redisConnectionFactory)
                                .cacheDefaults(defaultConfig)
                                .withInitialCacheConfigurations(cacheConfigurations)
                                .build();
        }
}
