package honajun.football_community.member.exception;

import honajun.football_community.global.exception.GeneralException;

public class MemberException extends GeneralException {
    public MemberException(MemberExceptionCode code) {
        super(code);
    }
}
