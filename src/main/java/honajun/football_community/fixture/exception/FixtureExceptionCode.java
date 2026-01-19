package honajun.football_community.fixture.exception;

import honajun.football_community.global.response.code.BaseCode;
import honajun.football_community.global.response.code.Reason;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum FixtureExceptionCode implements BaseCode {

    // 400
    INVALID_STATUS(HttpStatus.BAD_REQUEST, "FIXTURE400_1", "유효하지 않은 경기 상태입니다."),
    DUPLICATE_FOLLOW(HttpStatus.BAD_REQUEST, "FIXTURE400_2", "이미 Follow한 경기입니다."),
    
    // 404
    FOLLOW_NOT_FOUND(HttpStatus.NOT_FOUND, "FIXTURE404_1", "Follow를 찾을 수 없습니다."),
    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    @Override
    public Reason getReason() {
        return Reason.builder()
                .message(message)
                .code(code)
                .isSuccess(false)
                .build();
    }

    @Override
    public Reason getReasonHttpStatus() {
        return Reason.builder()
                .message(message)
                .code(code)
                .isSuccess(false)
                .httpStatus(httpStatus)
                .build();
    }
}