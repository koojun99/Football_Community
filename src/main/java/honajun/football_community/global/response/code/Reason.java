package honajun.football_community.global.response.code;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.HashMap;

@Getter
@Builder
public class Reason {

    private HttpStatus httpStatus;
    private final boolean isSuccess;
    private final String code;
    private final String message;
    private final HashMap<String, String> data;

}
