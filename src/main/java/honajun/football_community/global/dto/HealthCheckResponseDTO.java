package honajun.football_community.global.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class HealthCheckResponseDTO {
    private String status;
    private DatabaseStatus database;

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class DatabaseStatus {
        private String status;
        private String message;
    }
}

