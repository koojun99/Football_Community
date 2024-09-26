package honajun.football_community.blind.mapper;

import honajun.football_community.blind.entity.Blind;
import honajun.football_community.member.entity.Member;

public class BlindMapper {

    public static Blind toBlind(Member fromMember, Member toMember) {
        return Blind.builder()
                .fromMember(fromMember)
                .toMember(toMember)
                .build();
    }
}
