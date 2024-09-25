package honajun.football_community.member.mapper;

import honajun.football_community.global.enums.MemberStatus;
import honajun.football_community.member.dto.MemberRequestDTO;
import honajun.football_community.member.dto.MemberResponseDTO;
import honajun.football_community.member.entity.Member;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MemberMapper {

    public static Member toMember(MemberRequestDTO.register request) {
        return Member.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(request.getPassword())
                .username(request.getUsername())
                .bio(request.getBio())
                .phoneNumber(request.getPhoneNumber())
                .status(MemberStatus.ACTIVE)
                .build();
    }

    public static MemberResponseDTO.getMemberId toMemberId(Member member) {
        return MemberResponseDTO.getMemberId.builder()
                .memberId(member.getId())
                .build();
    }

    public static MemberResponseDTO.getMemberProfile toMemberProfile(Member member) {
        return MemberResponseDTO.getMemberProfile.builder()
                .memberId(member.getId())
                .username(member.getUsername())
                .bio(member.getBio())
                .build();
    }

    public static MemberResponseDTO.getMyProfile toMyProfile(Member member) {
        return MemberResponseDTO.getMyProfile.builder()
                .memberId(member.getId())
                .name(member.getName())
                .username(member.getUsername())
                .email(member.getEmail())
                .bio(member.getBio())
                .phoneNumber(member.getPhoneNumber())
                .build();
    }
}
