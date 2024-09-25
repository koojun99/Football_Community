package honajun.football_community.auth.dto;

import lombok.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AuthResponseDTO {

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class login {

            private Long memberId;

            private String accessToken;

            private String refreshToken;
    }
}
