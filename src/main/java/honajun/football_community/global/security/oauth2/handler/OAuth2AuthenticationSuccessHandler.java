package honajun.football_community.global.security.oauth2.handler;

import static honajun.football_community.global.security.oauth2.HttpCookieOAuth2AuthorizationRequestRepository.MODE_PARAM_COOKIE_NAME;
import static honajun.football_community.global.security.oauth2.HttpCookieOAuth2AuthorizationRequestRepository.REDIRECT_URI_PARAM_COOKIE_NAME;

import honajun.football_community.global.security.oauth2.HttpCookieOAuth2AuthorizationRequestRepository;
import honajun.football_community.global.security.oauth2.client.OAuth2Provider;
import honajun.football_community.global.security.oauth2.client.OAuth2UserPrincipal;
import honajun.football_community.global.security.oauth2.client.unlink.OAuth2UserUnlinkManager;
import honajun.football_community.global.security.oauth2.util.CookieUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;
import jakarta.servlet.http.Cookie;


@Component
@RequiredArgsConstructor
@Slf4j
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository;
    private final OAuth2UserUnlinkManager oAuth2UserUnlinkManager;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        String targetUrl;

        targetUrl = determineTargetUrl(request, response, authentication);

        if (response.isCommitted()) {
            logger.debug("Response has already been committed. Unable to redirect to " + targetUrl);
            return;
        }

        clearAuthenticationAttributes(request, response);
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }

    protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) {
        System.out.println("authentication = " + authentication);

        Optional<String> redirectUri = CookieUtils.getCookie(request, REDIRECT_URI_PARAM_COOKIE_NAME)
                .map(Cookie::getValue);

        String targetUrl = redirectUri.orElse(getDefaultTargetUrl());

        String mode = CookieUtils.getCookie(request, MODE_PARAM_COOKIE_NAME)
                .map(Cookie::getValue)
                .orElse("");


        OAuth2UserPrincipal principal = getOAuth2UserPrincipal(authentication);
        System.out.println("principal = " + principal);

        if (principal == null) {
            log.error("Principal is null - targetUrl: {}", targetUrl);
            return UriComponentsBuilder.fromUriString(targetUrl)
                    .queryParam("error", "Login failed")
                    .build().toUriString();
        }

        if ("login".equalsIgnoreCase(mode)) {

            String accessToken = principal.getJwtAccessToken(); // JWT Access Token
            String refreshToken = principal.getJwtRefreshToken(); // 필요 시 JwtTokenProvider로 생성
            if (accessToken == null) {
                log.error("AccessToken이 null입니다. principal={}", principal);
                return UriComponentsBuilder.fromUriString(targetUrl)
                        .queryParam("error", "Login failed")
                        .build().toUriString();
            }

            CookieUtils.addCookie(response, "access_token", accessToken, 3600); // 1시간 유효
            CookieUtils.addCookie(response, "refresh_token", refreshToken, 14 * 24 * 60 * 60); // 14일 유효

            return targetUrl;

        } else if ("unlink".equalsIgnoreCase(mode)) {

            String accessToken = principal.getUserInfo().getAccessToken();
            OAuth2Provider provider = principal.getUserInfo().getProvider();

            // TODO: DB 삭제
            // TODO: 리프레시 토큰 삭제
            oAuth2UserUnlinkManager.unlink(provider, accessToken);

            return UriComponentsBuilder.fromUriString(targetUrl)
                    .build().toUriString();
        }

        log.error("Login failed - mode not matched or principal missing. targetUrl: {}", targetUrl);

        return UriComponentsBuilder.fromUriString(targetUrl)
                .queryParam("error", "Login failed")
                .build().toUriString();
    }

    private OAuth2UserPrincipal getOAuth2UserPrincipal(Authentication authentication) {
        Object principal = authentication.getPrincipal();

        if (principal instanceof OAuth2UserPrincipal) {
            return (OAuth2UserPrincipal) principal;
        }
        return null;
    }

    protected void clearAuthenticationAttributes(HttpServletRequest request, HttpServletResponse response) {
        super.clearAuthenticationAttributes(request);
        httpCookieOAuth2AuthorizationRequestRepository.removeAuthorizationRequestCookies(request, response);
    }
}
