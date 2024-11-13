package honajun.football_community.wiki.exception;

import honajun.football_community.global.exception.GeneralException;

public class WikiException extends GeneralException {
    public WikiException(WikiExceptionCode code) {
        super(code);
    }
}

