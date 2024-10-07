package honajun.football_community.feed.exception;

import honajun.football_community.global.exception.GeneralException;

public class FeedException extends GeneralException {
    public FeedException(FeedExceptionCode code) {
        super(code);
    }
}
