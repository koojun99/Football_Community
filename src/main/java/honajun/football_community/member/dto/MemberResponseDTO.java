package honajun.football_community.member.dto;

import lombok.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MemberResponseDTO {

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class getMemberId {

        private Long memberId;
    }

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class getMemberProfile {

        private Long memberId;

        private String username;

        private String bio;
    }

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class getMyProfile {

            private Long memberId;

            private String name;

            private String username;

            private String email;

            private String bio;

            private String phoneNumber;
    }
}
