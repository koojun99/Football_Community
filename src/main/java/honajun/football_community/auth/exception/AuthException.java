package honajun.football_community.auth.exception;

import honajun.football_community.global.exception.GeneralException;

public class AuthException extends GeneralException {
    public AuthException(AuthExceptionCode code) {
        super(code);
    }
}
