package honajun.football_community.member.service;

import honajun.football_community.global.annotation.Adapter;
import honajun.football_community.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;

@Adapter
@RequiredArgsConstructor
public class MemberQueryAdapter {

    private final MemberRepository memberRepository;
}
