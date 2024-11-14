package honajun.football_community.favorite.exception;

import honajun.football_community.global.exception.GeneralException;

public class FavoriteException extends GeneralException {
    public FavoriteException(FavoriteExceptionCode code) {
        super(code);
    }
}

