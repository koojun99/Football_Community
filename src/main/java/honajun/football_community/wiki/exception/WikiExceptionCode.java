package honajun.football_community.wiki.exception;

import honajun.football_community.global.response.code.BaseCode;
import honajun.football_community.global.response.code.Reason;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;


@Getter
@AllArgsConstructor
public enum WikiExceptionCode implements BaseCode {

    // 404
    WIKI_NOT_FOUND(NOT_FOUND, "WIKI404_1", "해당 위키 페이지를 찾을 수 없습니다."),
    WIKI_CATEGORY_NOT_FOUND(NOT_FOUND, "WIKI404_2", "해당 카테고리를 찾을 수 없습니다."),
    COMMENT_NOT_FOUND(NOT_FOUND, "WIKI404_3", "해당 댓글을 찾을 수 없습니다."),
    WRONG_WRITER(FORBIDDEN, "WIKI403_1", "작성자만 수정/삭제할 수 있습니다."),
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