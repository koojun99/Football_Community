package honajun.football_community.member.mapper;

import honajun.football_community.global.enums.member.MemberRole;
import honajun.football_community.global.enums.member.MemberStatus;
import honajun.football_community.member.dto.MemberRequestDTO;
import honajun.football_community.member.dto.MemberResponseDTO;
import honajun.football_community.member.entity.Member;
import honajun.football_community.member.service.MemberService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MemberMapper {

    private final MemberService memberService;
    private static MemberService staticMemberService;

    private final PasswordEncoder passwordEncoder;
    private static PasswordEncoder staticPasswordEncoder;

    @PostConstruct
    public void init(){
        this.staticMemberService = this.memberService;
        this.staticPasswordEncoder = this.passwordEncoder;
    }

    public static Member toMember(MemberRequestDTO.register request) {
        return Member.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(staticPasswordEncoder.encode(request.getPassword()))
                .username(request.getUsername())
                .bio(request.getBio())
                .phoneNumber(request.getPhoneNumber())
                .status(MemberStatus.ACTIVE)
                .role(MemberRole.ROLE_USER)
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

    public static Member toMemberSecurity(String id) {
        return staticMemberService.findById(Long.parseLong(id));
    }
}
