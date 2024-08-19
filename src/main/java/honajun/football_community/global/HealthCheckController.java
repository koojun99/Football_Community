package honajun.football_community.global;

import honajun.football_community.global.response.CommonResponse;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Hidden
@RequestMapping("/health")
@RestController
public class HealthCheckController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping
    public CommonResponse<String> healthCheck() {
        return CommonResponse.onSuccess("Health check successful!");
    }

    @GetMapping("/db")
    public String checkDB() {
        try {
            jdbcTemplate.queryForObject("SELECT 1", Integer.class);
            return "Database Connection: OK";
        } catch (Exception e) {
            return "Database Connection: Failed - " + e.getMessage();
        }
    }
}

