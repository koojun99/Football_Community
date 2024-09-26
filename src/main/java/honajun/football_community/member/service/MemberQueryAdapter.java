package honajun.football_community.member.service;

import honajun.football_community.blind.entity.Blind;
import honajun.football_community.blind.repository.BlindRepository;
import honajun.football_community.global.annotation.Adapter;
import honajun.football_community.member.entity.Member;
import honajun.football_community.member.exception.MemberException;
import honajun.football_community.member.exception.MemberExceptionCode;
import honajun.football_community.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;

@Adapter
@RequiredArgsConstructor
public class MemberQueryAdapter {

    private final MemberRepository memberRepository;
    private final BlindRepository blindRepository;

    public boolean existsByEmail(String email) {
        return memberRepository.existsByEmail(email);
    }

    public Member findByEmail(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new MemberException(MemberExceptionCode._MEMBER_NOT_FOUND));
    }

    public Member findById(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(MemberExceptionCode._MEMBER_NOT_FOUND));
    }

    public Blind findBlind(Member fromMember, Member toMember) {
        return blindRepository.findByFromMemberAndToMember(fromMember, toMember)
                .orElseThrow(() -> new MemberException(MemberExceptionCode._BLIND_NOT_FOUND));
    }
}
