package honajun.football_community.global.http;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FixtureApiResponse {
    private List<ResponseData> response;

    @Getter
    @Setter
    public static class ResponseData {
        private Fixture fixture;
        private League league;
        private Teams teams;
        private Goals goals;
    }

    @Getter
    @Setter
    public static class Fixture {
        private Long id;
        private String date;
        private Long timestamp;
        @JsonProperty("status")
        private Status status;
    }

    @Getter
    @Setter
    public static class Status {
        @JsonProperty("short")
        private String shortStatus;
    }

    @Getter
    @Setter
    public static class League {
        private String name;
    }

    @Getter
    @Setter
    public static class Teams {
        private Team home;
        private Team away;

        @Getter
        @Setter
        public static class Team {
            private String name;
        }
    }

    @Getter
    @Setter
    public static class Goals {
        private Integer home;
        private Integer away;
    }
}
