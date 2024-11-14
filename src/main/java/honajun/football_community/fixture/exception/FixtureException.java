package honajun.football_community.fixture.exception;

import honajun.football_community.global.exception.GeneralException;

public class FixtureException extends GeneralException {
    public FixtureException(FixtureExceptionCode code) {
        super(code);
    }
}

