package honajun.football_community.global;

import honajun.football_community.global.dto.HealthCheckResponseDTO;
import honajun.football_community.global.response.CommonResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/health")
@Tag(name = "Health Check", description = "헬스체크 API")
public class HealthCheckController {

    private final JdbcTemplate jdbcTemplate;

    @Operation(summary = "헬스체크", description = "애플리케이션 및 데이터베이스 연결 상태를 확인합니다.")
    @GetMapping
    public CommonResponse<HealthCheckResponseDTO> healthCheck() {
        HealthCheckResponseDTO.DatabaseStatus databaseStatus = checkDatabaseConnection();

        String overallStatus = "UP";
        if (!"UP".equals(databaseStatus.getStatus())) {
            overallStatus = "DOWN";
        }

        HealthCheckResponseDTO response = HealthCheckResponseDTO.builder()
                .status(overallStatus)
                .database(databaseStatus)
                .build();

        return CommonResponse.onSuccess(response);
    }

    /**
     * 데이터베이스 연결 상태를 확인합니다.
     */
    private HealthCheckResponseDTO.DatabaseStatus checkDatabaseConnection() {
        try {
            jdbcTemplate.queryForObject("SELECT 1", Integer.class);
            return HealthCheckResponseDTO.DatabaseStatus.builder()
                    .status("UP")
                    .message("Database connection is healthy")
                    .build();
        } catch (Exception e) {
            log.error("Database connection check failed", e);
            return HealthCheckResponseDTO.DatabaseStatus.builder()
                    .status("DOWN")
                    .message("Database connection failed: " + e.getMessage())
                    .build();
        }
    }
}
