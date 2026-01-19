package honajun.football_community.fixture.dto;

import java.util.List;
import lombok.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FollowResponseDTO {

    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    public static class getFollow {
        private Long id;
        private Long fixtureId;
    }

    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    public static class getFollows {
        List<getFollow> follows;
    }
}

