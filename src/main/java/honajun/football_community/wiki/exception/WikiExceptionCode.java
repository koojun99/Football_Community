package honajun.football_community.wiki.exception;

import honajun.football_community.global.response.code.BaseCode;
import honajun.football_community.global.response.code.Reason;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum WikiExceptionCode implements BaseCode {

    // 404
    _WIKI_NOT_FOUND(HttpStatus.NOT_FOUND, "WIKI_404", "해당 위키 페이지를 찾을 수 없습니다."),
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