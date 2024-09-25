package honajun.football_community.member.service;

import honajun.football_community.member.dto.MemberRequestDTO;
import honajun.football_community.member.dto.MemberResponseDTO;
import honajun.football_community.member.entity.Member;
import honajun.football_community.member.mapper.MemberMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberCommandAdapter memberCommandAdapter;
    private final MemberQueryAdapter memberQueryAdapter;

    @Transactional
    public MemberResponseDTO.getMemberId register(MemberRequestDTO.register request) {
        Member savedMember = memberCommandAdapter.register(MemberMapper.toMember(request));
        return MemberMapper.toMemberId(savedMember);
    }

    // 회원 조회
    @Transactional(readOnly = true)
    public Member findById(Long memberId) {
        return memberQueryAdapter.findById(memberId);
    }

    @Transactional(readOnly = true)
    public MemberResponseDTO.getMyProfile getMyProfile(Member member) {
        return MemberMapper.toMyProfile(member);
    }

    @Transactional(readOnly = true)
    public MemberResponseDTO.getMemberProfile getMemberProfile(Long memberId) {
        Member member = memberQueryAdapter.findById(memberId);
        return MemberMapper.toMemberProfile(member);
    }

    @Transactional
    public MemberResponseDTO.getMemberId updateProfile(Member member, MemberRequestDTO.updateProfile request) {
        Member updatedMember = memberCommandAdapter.updateProfile(member, request);
        return MemberMapper.toMemberId(updatedMember);
    }

    @Transactional
    public MemberResponseDTO.getMemberId withdraw(Member member) {
        memberCommandAdapter.withdraw(member);
        return MemberMapper.toMemberId(member);
    }

    @Transactional MemberResponseDTO.getMemberId deleteMember(Member member) {
        memberCommandAdapter.deleteMember(member);
        return MemberMapper.toMemberId(member);
    }
}
