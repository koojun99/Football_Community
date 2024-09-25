package honajun.football_community.auth.service;

import honajun.football_community.auth.dto.AuthRequestDTO;
import honajun.football_community.auth.exception.AuthException;
import honajun.football_community.auth.exception.AuthExceptionCode;
import honajun.football_community.member.service.MemberCommandAdapter;
import honajun.football_community.member.service.MemberQueryAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final MemberQueryAdapter memberQueryAdapter;

    private final RedisTemplate<String, String> redisTemplate;
    private static final long EMAIL_CODE_TTL = 5;
    private final EmailService emailService;

    public void emailDuplicateCheck(String email) {
        if (memberQueryAdapter.existsByEmail(email)) {
            throw new AuthException(AuthExceptionCode._DUPLICATED_EMAIL);
        }
    }

    public void sendVerificationCode(AuthRequestDTO.sendVerificationCode request) {
        // 인증코드 생성
        String verificationCode = generateVerificationCode();

        // Redis에 저장 (email:인증코드 형태로 저장)
        redisTemplate.opsForValue().set("email:verification:" + request.getEmail(), verificationCode, EMAIL_CODE_TTL, TimeUnit.MINUTES);

        // 이메일 전송
        emailService.sendVerificationEmail(request.getEmail(), verificationCode);
    }

    // 인증코드 검증
    public boolean verifyCode(AuthRequestDTO.verifyCode request) {
        // Redis에서 저장된 인증코드 가져오기
        String storedCode = redisTemplate.opsForValue().get("email:verification:" + request.getEmail());

        // 저장된 인증코드가 없거나 일치하지 않으면 실패
        if (storedCode == null || !storedCode.equals(request.getInputCode())) {
            throw new AuthException(AuthExceptionCode._INVALID_VERIFICATION_CODE);
        }

        // 인증 성공 시 Redis에서 해당 인증코드 삭제 (1회용)
        redisTemplate.delete("email:verification:" + request.getEmail());
        return true;
    }

    // 6자리 랜덤 인증코드 생성
    private String generateVerificationCode() {
        Random random = new Random();
        return String.format("%06d", random.nextInt(1000000));  // 000000~999999 사이의 숫자
    }
}
