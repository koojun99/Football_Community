package honajun.football_community.global.security.oauth2.service;

import honajun.football_community.global.enums.member.MemberRole;
import honajun.football_community.global.enums.member.MemberStatus;
import honajun.football_community.global.security.jwt.JwtTokenProvider;
import honajun.football_community.global.security.oauth2.client.OAuth2UserInfo;
import honajun.football_community.global.security.oauth2.client.OAuth2UserInfoFactory;
import honajun.football_community.global.security.oauth2.client.OAuth2UserPrincipal;
import honajun.football_community.global.security.oauth2.exception.OAuth2AuthenticationProcessingException;
import honajun.football_community.member.entity.Member;
import honajun.football_community.member.repository.MemberRepository;
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationProcessingException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        try {
            return processOAuth2User(userRequest, oAuth2User);
        } catch (Exception ex) {
            // Throwing an instance of AuthenticationException will trigger the OAuth2AuthenticationFailureHandler
            throw new InternalAuthenticationServiceException(ex.getMessage(), ex.getCause());
        }
    }

    private OAuth2User processOAuth2User(OAuth2UserRequest userRequest, OAuth2User oAuth2User) {

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        String accessToken = userRequest.getAccessToken().getTokenValue();

        OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(registrationId, accessToken, oAuth2User.getAttributes());

        if (!StringUtils.hasText(oAuth2UserInfo.getEmail())) {
            throw new OAuth2AuthenticationProcessingException("Email not found from OAuth2 provider");
        }

        // DB에서 회원 조회 또는 생성
        Member member = memberRepository.findByEmail(oAuth2UserInfo.getEmail())
                .orElseGet(() -> {
                    Member newMember = Member.builder()
                            .email(oAuth2UserInfo.getEmail())
                            .name(oAuth2UserInfo.getName())
                            .username(oAuth2UserInfo.getEmail())
                            .role(MemberRole.ROLE_USER)
                            .status(MemberStatus.ACTIVE)
                            .build();
                    return memberRepository.save(newMember);
                });

        // JWT Access Token 생성
        Collection<GrantedAuthority> authorities = Arrays.asList(new SimpleGrantedAuthority(String.valueOf(member.getRole())));
        String jwtAccessToken = jwtTokenProvider.createAccessToken(member, authorities);
        String jwtRefreshToken = jwtTokenProvider.createRefreshToken();
        log.info("AccessToken 생성: {}", jwtAccessToken);
        log.info("RefreshToken 생성: {}", jwtRefreshToken);

        // 유저 정보를 OAuth2UserPrincipal에 저장
        OAuth2UserPrincipal principal = new OAuth2UserPrincipal(oAuth2UserInfo);
        System.out.println("oauth2Principal = " + principal);
        principal.setJwtAccessToken(jwtAccessToken);
        principal.setJwtRefreshToken(jwtRefreshToken);

        return principal;
    }
}
