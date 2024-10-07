package honajun.football_community.feed.exception;

import honajun.football_community.global.response.code.BaseCode;
import honajun.football_community.global.response.code.Reason;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@Getter
@AllArgsConstructor
public enum FeedExceptionCode implements BaseCode {

    // 403
    _WRONG_WRITER(FORBIDDEN, "FEED403_1", "작성자만 수정할 수 있습니다."),

    // Category + 404
    _CATEGORY_NOT_FOUND(NOT_FOUND, "FEED404_1", "카테고리를 찾을 수 없습니다."),

    // Post + 404
    _POST_NOT_FOUND(NOT_FOUND, "FEED404_2", "게시글을 찾을 수 없습니다."),

    // Comment + 404
    _COMMENT_NOT_FOUND(NOT_FOUND, "FEED404_3", "댓글을 찾을 수 없습니다."),

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
