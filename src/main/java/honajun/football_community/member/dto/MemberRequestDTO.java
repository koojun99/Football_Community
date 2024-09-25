package honajun.football_community.member.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MemberRequestDTO {

    @Getter
    public static class register {

        @NotBlank(message = "이름을 입력해주세요.")
        private String name;

        @NotBlank(message = "이메일을 입력해주세요.")
        private String email;

        @NotBlank(message = "비밀번호를 입력해주세요.")
        private String password;

        @NotBlank(message = "유저네임 입력해주세요.")
        private String username;

        private String bio;

        private String phoneNumber;
    }

    @Getter
    public static class updateProfile {

        private String name;

        private String username;

        private String bio;

        private String phoneNumber;
    }
}
