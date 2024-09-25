package honajun.football_community.global.annotation.resolver;


import honajun.football_community.global.annotation.AuthMember;
import honajun.football_community.member.entity.Member;
import honajun.football_community.member.exception.MemberException;
import honajun.football_community.member.exception.MemberExceptionCode;
import honajun.football_community.member.mapper.MemberMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
@Slf4j
@RequiredArgsConstructor
public class AuthMemberArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterAnnotation(AuthMember.class) != null
                && Member.class.equals(parameter.getParameterType());
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication.getPrincipal() == null || authentication.getPrincipal() instanceof String) {
            AuthMember authMember = parameter.getParameterAnnotation(AuthMember.class);
            if (authMember != null && !authMember.required()) {
                return null;
            }
            throw new MemberException(MemberExceptionCode._MEMBER_NOT_FOUND);
        }

        // Authentication의 principal을 사용하여 Member 객체를 가져옴
        String memberId = authentication.getName();
        return MemberMapper.toMemberSecurity(memberId);
    }
}


