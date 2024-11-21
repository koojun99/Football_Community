package honajun.football_community.global;

import honajun.football_community.global.response.CommonResponse;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

    private static final Logger logger = LoggerFactory.getLogger(HealthCheckController.class);

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


    @GetMapping("/test-error")
    public String testInternalServerError()  {
        logger.info("Attempted access to / endpoint resulted in 403 Forbidden");
        return null;
    }
}

