package honajun.football_community.auth.exception;


import honajun.football_community.global.response.code.BaseCode;
import honajun.football_community.global.response.code.Reason;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum AuthExceptionCode implements BaseCode {

    //400
    _INVALID_VERIFICATION_CODE(HttpStatus.BAD_REQUEST, "AUTH400_1", "유효하지 않는 인증코드입니다."),

    //409
    _DUPLICATED_EMAIL(HttpStatus.CONFLICT, "AUTH409_1", "이미 존재하는 이메일입니다."),
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
