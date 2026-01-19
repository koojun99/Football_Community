package honajun.football_community.fixture.event.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class FixtureEventDTO {
    private Long fixtureId;
    private String eventType;
    private String eventData;
    private Long timestamp;
}

