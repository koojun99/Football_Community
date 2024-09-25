package honajun.football_community.auth.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AuthRequestDTO {

    @Getter
    public static class sendVerificationCode {

        @NotBlank(message = "이메일을 입력해주세요.")
        private String email;
    }

    @Getter
    public static class verifyCode {

        @NotBlank(message = "이메일을 입력해주세요.")
        private String email;

        @NotBlank(message = "인증코드를 입력해주세요.")
        private String inputCode;
    }
}
