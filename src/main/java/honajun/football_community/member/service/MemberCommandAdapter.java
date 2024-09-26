package honajun.football_community.member.service;

import honajun.football_community.blind.entity.Blind;
import honajun.football_community.blind.repository.BlindRepository;
import honajun.football_community.global.annotation.Adapter;
import honajun.football_community.member.dto.MemberRequestDTO;
import honajun.football_community.member.entity.Member;
import honajun.football_community.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;

@Adapter
@RequiredArgsConstructor
public class MemberCommandAdapter {

    private final MemberRepository memberRepository;
    private final BlindRepository blindRepository;

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

    public void changePassword(Member member, String newPassword) {
        member.changePassword(newPassword);
        memberRepository.save(member);
    }

    public void blindMember(Blind blind) {
        blindRepository.save(blind);
    }

    public void cancelBlind(Blind blind) {
        blindRepository.delete(blind);
    }
}
