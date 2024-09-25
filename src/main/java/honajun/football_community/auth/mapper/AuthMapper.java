package honajun.football_community.auth.mapper;

import honajun.football_community.auth.dto.AuthResponseDTO;
import honajun.football_community.member.entity.Member;

public class AuthMapper {

    public static AuthResponseDTO.login toLogin(Member member, String accessToken, String refreshToken) {
        return AuthResponseDTO.login.builder()
                .memberId(member.getId())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}
