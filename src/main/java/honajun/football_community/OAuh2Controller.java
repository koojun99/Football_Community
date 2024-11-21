package honajun.football_community;

import honajun.football_community.global.response.CommonResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OAuh2Controller {

    @GetMapping("/login/oauth2/code/{provider}")
    public CommonResponse<String> handleSocialLogin(
            @PathVariable String provider,
            @RequestParam("access_token") String accessToken,
            @RequestParam("refresh_token") String refreshToken) {
        // 프론트엔드 없이 직접 토큰 확인용 페이지로 리턴
        return CommonResponse.onSuccess(provider + " 로그인 성공!" + "\nAccessToken: " + accessToken + "\n RefreshToken: " + refreshToken);
    }
}
