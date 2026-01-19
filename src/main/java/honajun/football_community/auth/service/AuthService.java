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
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collection;

/**
 * 인증 관련 비즈니스 로직을 처리하는 서비스
 */
@Service
@RequiredArgsConstructor
public class AuthService {

    private final PasswordEncoder passwordEncoder;

    private final MemberCommandAdapter memberCommandAdapter;
    private final MemberQueryAdapter memberQueryAdapter;

    private final JwtTokenProvider jwtTokenProvider;
    private final RedisService redisService;

    /**
     * 이메일 중복 여부를 확인합니다.
     */
    public void emailDuplicateCheck(String email) {
        if (memberQueryAdapter.existsByEmail(email)) {
            throw new AuthException(AuthExceptionCode.DUPLICATED_EMAIL);
        }
    }

    /**
     * 사용자 로그인을 처리합니다.
     */
    public AuthResponseDTO.login login(AuthRequestDTO.login request) {
        Member member = memberQueryAdapter.findByEmail(request.getEmail());

        // Role을 SimpleGrantedAuthority로 변환하여 authorities로 만듬
        Collection<GrantedAuthority> authorities = Arrays
                .asList(new SimpleGrantedAuthority(String.valueOf(member.getRole())));
        if (!passwordEncoder.matches(request.getPassword(), member.getPassword())) {
            throw new AuthException(AuthExceptionCode.INVALID_PASSWORD);
        }
        String accessToken = jwtTokenProvider.createAccessToken(member, authorities);
        String refreshToken = jwtTokenProvider.createRefreshToken();

        // Redis에 AccessToken 저장
        redisService.saveToken(member.getId().toString(), accessToken, jwtTokenProvider.getExpirationTime(accessToken));
        // Redis에 RefreshToken 저장 (사용자 식별자와 함께 저장)
        redisService.saveToken(member.getId().toString(), refreshToken,
                jwtTokenProvider.getExpirationTime(refreshToken));

        return AuthMapper.toLogin(member, accessToken, refreshToken);
    }

    /**
     * 사용자 비밀번호를 변경합니다.
     */
    public void changePassword(Member member, AuthRequestDTO.changePassword request) {
        if (!passwordEncoder.matches(request.getCurrentPassword(), member.getPassword())) {
            throw new AuthException(AuthExceptionCode.INVALID_PASSWORD);
        }
        memberCommandAdapter.changePassword(member, passwordEncoder.encode(request.getNewPassword()));
    }

}
