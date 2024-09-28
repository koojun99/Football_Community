package honajun.football_community.feed.post.exception;

import honajun.football_community.global.exception.GeneralException;

public class PostException extends GeneralException {
    public PostException(PostExceptionCode code) {
        super(code);
    }
}
