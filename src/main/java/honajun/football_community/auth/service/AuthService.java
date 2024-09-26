package honajun.football_community.auth.service;

import honajun.football_community.auth.dto.AuthRequestDTO;
import honajun.football_community.auth.dto.AuthResponseDTO;
import honajun.football_community.auth.exception.AuthException;
import honajun.football_community.auth.exception.AuthExceptionCode;
import honajun.football_community.auth.mapper.AuthMapper;
import honajun.football_community.global.security.jwt.JwtTokenProvider;
import honajun.football_community.member.entity.Member;
import honajun.football_community.member.service.MemberCommandAdapter;
import honajun.football_community.member.service.MemberQueryAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collection;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final PasswordEncoder passwordEncoder;

    private final MemberCommandAdapter memberCommandAdapter;
    private final MemberQueryAdapter memberQueryAdapter;

    private final RedisTemplate<String, String> redisTemplate;
    private static final long EMAIL_CODE_TTL = 5;
    private final EmailService emailService;
    private final JwtTokenProvider jwtTokenProvider;
    private final RedisService redisService;

    public void emailDuplicateCheck(String email) {
        if (memberQueryAdapter.existsByEmail(email)) {
            throw new AuthException(AuthExceptionCode._DUPLICATED_EMAIL);
        }
    }

    public void sendVerificationCode(AuthRequestDTO.sendVerificationCode request) {
        // 인증코드 생성
        String verificationCode = generateVerificationCode();

        // Redis에 저장 (email:인증코드 형태로 저장)
        redisService.saveEmailCode(request.getEmail(), verificationCode, EMAIL_CODE_TTL);

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

    public AuthResponseDTO.login login(AuthRequestDTO.login request) {
        Member member = memberQueryAdapter.findByEmail(request.getEmail());

        // Role을 SimpleGrantedAuthority로 변환하여 authorities로 만듬
        Collection<GrantedAuthority> authorities = Arrays.asList(new SimpleGrantedAuthority(String.valueOf(member.getRole())));
        if (!passwordEncoder.matches(request.getPassword(), member.getPassword())) {
            throw new AuthException(AuthExceptionCode._INVALID_PASSWORD);
        }
        String accessToken = jwtTokenProvider.createAccessToken(member, authorities);
        String refreshToken = jwtTokenProvider.createRefreshToken();

        // Redis에 AccessToken 저장
        redisService.saveToken(member.getId().toString(), accessToken, jwtTokenProvider.getExpirationTime(accessToken));
        // Redis에 RefreshToken 저장 (사용자 식별자와 함께 저장)
        redisService.saveToken(member.getId().toString(), refreshToken, jwtTokenProvider.getExpirationTime(refreshToken));

        return AuthMapper.toLogin(member, accessToken, refreshToken);
    }

    public void changePassword(Member member, AuthRequestDTO.changePassword request) {
        if (!passwordEncoder.matches(request.getCurrentPassword(), member.getPassword())) {
            throw new AuthException(AuthExceptionCode._INVALID_PASSWORD);
        }
        memberCommandAdapter.changePassword(member, passwordEncoder.encode(request.getNewPassword()));
    }

}
