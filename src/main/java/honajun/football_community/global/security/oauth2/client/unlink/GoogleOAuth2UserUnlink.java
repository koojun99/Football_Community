package honajun.football_community.global.security.oauth2.client.unlink;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class GoogleOAuth2UserUnlink implements OAuth2UserUnlink {

    private static final String URL = "https://oauth2.googleapis.com/revoke";
    private final RestTemplate restTemplate;

    @Override
    public void unlink(String accessToken) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>(); // MultiValueMap은 key 하나에 여러 개의 value를 가질 수 있는 맵
        params.add("token", accessToken); // token이라는 key에 accessToken을 value로 추가
        restTemplate.postForObject(URL, params, String.class); // URL로 POST 요청을 보내고, 응답을 String.class로 받음
    }
}