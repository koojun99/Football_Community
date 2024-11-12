package honajun.football_community.league.exception;

import honajun.football_community.global.exception.GeneralException;

public class LeagueException extends GeneralException {
    public LeagueException(LeagueExceptionCode code) {
        super(code);
    }
}
