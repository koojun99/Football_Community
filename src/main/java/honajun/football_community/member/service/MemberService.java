package honajun.football_community.member.service;

import honajun.football_community.blind.entity.Blind;
import honajun.football_community.blind.mapper.BlindMapper;
import honajun.football_community.member.dto.MemberRequestDTO;
import honajun.football_community.member.dto.MemberResponseDTO;
import honajun.football_community.member.entity.Member;
import honajun.football_community.member.mapper.MemberMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 회원 관련 비즈니스 로직을 처리하는 서비스
 */
@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberCommandAdapter memberCommandAdapter;
    private final MemberQueryAdapter memberQueryAdapter;

    /**
     * 회원가입을 처리합니다.
     */
    @Transactional
    public MemberResponseDTO.getMemberId register(MemberRequestDTO.register request) {
        Member savedMember = memberCommandAdapter.register(MemberMapper.toMember(request));
        return MemberMapper.toMemberId(savedMember);
    }

    /**
     * 회원 정보를 조회합니다.
     */
    @Transactional(readOnly = true)
    public Member findById(Long memberId) {
        return memberQueryAdapter.findById(memberId);
    }

    /**
     * 내 프로필 정보를 조회합니다.
     */
    @Transactional(readOnly = true)
    public MemberResponseDTO.getMyProfile getMyProfile(Member member) {
        return MemberMapper.toMyProfile(member);
    }

    /**
     * 다른 회원의 프로필 정보를 조회합니다.
     */
    @Transactional(readOnly = true)
    public MemberResponseDTO.getMemberProfile getMemberProfile(Long memberId) {
        Member member = memberQueryAdapter.findById(memberId);
        return MemberMapper.toMemberProfile(member);
    }

    /**
     * 내 프로필 정보를 수정합니다.
     */
    @Transactional
    public MemberResponseDTO.getMemberId updateProfile(Member member, MemberRequestDTO.updateProfile request) {
        Member updatedMember = memberCommandAdapter.updateProfile(member, request);
        return MemberMapper.toMemberId(updatedMember);
    }

    /**
     * 회원 탈퇴를 처리합니다.
     */
    @Transactional
    public MemberResponseDTO.getMemberId withdraw(Member member) {
        memberCommandAdapter.withdraw(member);
        return MemberMapper.toMemberId(member);
    }

    /**
     * 회원을 삭제합니다.
     */
    @Transactional
    public MemberResponseDTO.getMemberId deleteMember(Member member) {
        memberCommandAdapter.deleteMember(member);
        return MemberMapper.toMemberId(member);
    }

    /**
     * 특정 회원을 블라인드 처리합니다.
     */
    @Transactional
    public void blindMember(Member member, Long memberId) {
        Member toMember = memberQueryAdapter.findById(memberId);
        memberCommandAdapter.blindMember(BlindMapper.toBlind(member, toMember));
    }

    /**
     * 특정 회원의 블라인드를 해제합니다.
     */
    @Transactional
    public void cancelBlind(Member member, Long memberId) {
        Member toMember = memberQueryAdapter.findById(memberId);
        Blind blind = memberQueryAdapter.findBlind(member, toMember);
        memberCommandAdapter.cancelBlind(blind);
    }

}
