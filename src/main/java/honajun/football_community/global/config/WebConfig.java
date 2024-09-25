package honajun.football_community.global.config;

import honajun.football_community.global.annotation.resolver.AuthMemberArgumentResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {
    private final AuthMemberArgumentResolver authMemberArgumentResolver;

    /**
     * 컨트롤러 메서드의 특정 파라미터를 지원하는 커스텀한 ArgumentResolver를 추가
     * @param resolverList
     */
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolverList) {
        resolverList.add(authMemberArgumentResolver);
    }
}