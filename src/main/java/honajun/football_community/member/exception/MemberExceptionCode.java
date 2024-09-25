package honajun.football_community.member.exception;

import honajun.football_community.global.response.code.BaseCode;
import honajun.football_community.global.response.code.Reason;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@Getter
@AllArgsConstructor
public enum MemberExceptionCode implements BaseCode {

    // 400
    _MEMBER_PASSWORD_NOT_MATCH(BAD_REQUEST, "MEMBER400_1","비밀번호가 일치하지 않습니다."),
    // 403
    _MEMBER_NOT_AUTHORIZED(FORBIDDEN, "MEMBER403_1","해당 회원은 권한이 없습니다."),
    // 404
    _MEMBER_NOT_FOUND(NOT_FOUND, "MEMBER401_1","해당 회원을 찾을 수 없습니다."),
    // 409
    _MEMBER_DUPLICATE(CONFLICT,"MEMBER409_1","이미 존재하는 회원입니다."),
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
