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

    @Operation(summary = "회원가입", description = "회원가입을 진행합니다.")
    @PostMapping("/register")
    public CommonResponse<MemberResponseDTO.getMemberId> register(
            @RequestBody MemberRequestDTO.register request
    ) {
        return CommonResponse.onSuccess(memberService.register(request));
    }

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
}
