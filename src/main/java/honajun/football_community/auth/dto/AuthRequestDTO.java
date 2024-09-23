package honajun.football_community.auth.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AuthRequestDTO {

    @Getter
    public static class sendVerificationCode {
        private String email;
    }

    @Getter
    public static class verifyCode {
        private String email;
        private String inputCode;
    }
}
