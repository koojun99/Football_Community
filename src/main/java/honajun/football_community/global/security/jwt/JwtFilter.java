package honajun.football_community.global.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import honajun.football_community.auth.exception.AuthExceptionCode;
import honajun.football_community.global.response.CommonResponse;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtTokenProvider tokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            // HTTP 요청에서 Authorization 헤더를 찾아 토큰 반환
            String accessToken = tokenProvider.resolveToken(request, "Access");

            // 토큰이 있다면 유효성 검사 후 인증
            if (StringUtils.hasText(accessToken) && tokenProvider.validateToken(accessToken)) {
                Authentication authentication = tokenProvider.getAuthentication(accessToken);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else {
                SecurityContextHolder.getContext().setAuthentication(null);
            }

            // 다음 단계로 필터 체인 실행
            filterChain.doFilter(request, response);

        } catch (Exception e) {
            log.error("JWT 필터에서 예외 발생: {}", e.getMessage());
            jwtExceptionHandler(response, AuthExceptionCode._INVALID_TOKEN); // 적절한 예외 코드 사용
        }
    }

    // JWT 인증과 관련된 예외 처리
    public void jwtExceptionHandler(HttpServletResponse response, AuthExceptionCode errorCode) {
        response.setStatus(errorCode.getHttpStatus().value());
        response.setContentType("application/json");

        try {
            String code = errorCode.getCode();
            String message = errorCode.getMessage();
            String json = new ObjectMapper().writeValueAsString(CommonResponse.onFailure(code, message, null)); // ApiResponse 객체를 JSON으로 변환
            response.getWriter().write(json);
        } catch (Exception e) {
            log.error("JWT 예외 처리 중 오류: {}", e.getMessage());
        }
    }
}
