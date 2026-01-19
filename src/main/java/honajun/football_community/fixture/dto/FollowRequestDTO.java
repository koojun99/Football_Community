package honajun.football_community.fixture.dto;

import lombok.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FollowRequestDTO {

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class addFollow {
        private Long fixtureId;
    }
}

