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

    @Getter
    public static class login {

        @NotBlank(message = "이메일을 입력해주세요.")
        private String email;

        @NotBlank(message = "비밀번호를 입력해주세요.")
        private String password;
    }

    @Getter
    public static class changePassword {

        @NotBlank(message = "현재 비밀번호를 입력해주세요.")
        private String currentPassword;

        @NotBlank(message = "신규 비밀번호를 입력해주세요.")
        private String newPassword;
    }
}
