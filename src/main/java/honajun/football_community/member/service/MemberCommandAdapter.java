package honajun.football_community.member.service;

import honajun.football_community.global.annotation.Adapter;
import honajun.football_community.member.dto.MemberRequestDTO;
import honajun.football_community.member.entity.Member;
import honajun.football_community.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;

@Adapter
@RequiredArgsConstructor
public class MemberCommandAdapter {

    private final MemberRepository memberRepository;

    public Member register(Member member) {
        return memberRepository.save(member);
    }

    public Member updateProfile(Member member, MemberRequestDTO.updateProfile request) {
        member.update(request.getName(), request.getUsername(), request.getBio(), request.getPhoneNumber());
        return memberRepository.save(member);
    }

    public void withdraw(Member member) {
        member.withdraw();
        memberRepository.save(member);
    }

    public void deleteMember(Member member) {
        memberRepository.delete(member);
    }
}
