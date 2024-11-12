package honajun.football_community.team.exception;

import honajun.football_community.global.exception.GeneralException;

public class TeamException extends GeneralException {
    public TeamException(TeamExceptionCode code) {
        super(code);
    }
}

