package honajun.football_community.member.controller;

import honajun.football_community.global.annotation.AuthMember;
import honajun.football_community.global.response.CommonResponse;
import honajun.football_community.member.dto.MemberRequestDTO;
import honajun.football_community.member.dto.MemberResponseDTO;
import honajun.football_community.member.entity.Member;
import honajun.football_community.member.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;


    @Operation(summary = "내 프로필 조회", description = "내 프로필을 조회합니다.")
    @GetMapping("/my-profile")
    public CommonResponse<MemberResponseDTO.getMyProfile> getMyProfile(
            @AuthMember Member member
    ) {
        return CommonResponse.onSuccess(memberService.getMyProfile(member));
    }

    @Operation(summary = "회원 프로필 조회", description = "특정 회원의 프로필을 조회합니다.")
    @GetMapping("/{memberId}")
    public CommonResponse<MemberResponseDTO.getMemberProfile> getMemberProfile(
            @PathVariable Long memberId
    ) {
        return CommonResponse.onSuccess(memberService.getMemberProfile(memberId));
    }

    @Operation(summary = "회원 정보 수정", description = "회원 정보를 수정합니다.")
    @PutMapping
    public CommonResponse<MemberResponseDTO.getMemberId> updateMyProfile(
            @AuthMember Member member,
            @RequestBody MemberRequestDTO.updateProfile request
    ) {
        return CommonResponse.onSuccess(memberService.updateProfile(member, request));
    }

    @Operation(summary = "회원 탈퇴", description = "회원 탈퇴를 진행합니다.")
    @DeleteMapping
    public CommonResponse<MemberResponseDTO.getMemberId> withdraw(
            @AuthMember Member member
    ) {
        return CommonResponse.onSuccess(memberService.withdraw(member));
    }

    @Operation(summary = "회원 블라인드", description = "회원을 블라인드 처리합니다.")
    @PostMapping("/{memberId}/blind")
    public CommonResponse blindMember(
            @AuthMember Member member,
            @PathVariable Long memberId
    ) {
        memberService.blindMember(member, memberId);
        return CommonResponse.onNoContent();
    }

    @Operation(summary = "회원 블라인드 해제", description = "회원을 블라인드 해제합니다.")
    @PatchMapping("/{memberId}/blind")
    public CommonResponse cancelBlind(
            @AuthMember Member member,
            @PathVariable Long memberId
    ) {
        memberService.cancelBlind(member, memberId);
        return CommonResponse.onNoContent();
    }
}
