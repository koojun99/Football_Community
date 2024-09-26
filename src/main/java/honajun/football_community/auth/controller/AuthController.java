package honajun.football_community.auth.controller;

import honajun.football_community.auth.dto.AuthRequestDTO;
import honajun.football_community.auth.dto.AuthResponseDTO;
import honajun.football_community.auth.service.AuthService;
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
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final MemberService memberService;

    @Operation(summary = "이메일 중복 확인", description = "이메일 중복 확인")
    @GetMapping("/email-verification")
    public CommonResponse emailDuplicateCheck(
            @RequestParam(name = "email") String email
    ) {
        authService.emailDuplicateCheck(email);
        return CommonResponse.onSuccess(null);
    }

    @Operation(summary = "이메일 인증코드 전송", description = "이메일 인증코드 전송")
    @PostMapping("/email-verification")
    public CommonResponse sendVerificationCode(
            @RequestBody AuthRequestDTO.sendVerificationCode request
    ) {
        authService.sendVerificationCode(request);
        return CommonResponse.onSuccess(null);
    }

    @Operation(summary = "이메일 인증코드 검증", description = "이메일 인증코드 검증")
    @GetMapping("/email-verification/verify")
    public CommonResponse verifyCode(
            @RequestBody AuthRequestDTO.verifyCode request
    ) {
        authService.verifyCode(request);
        return CommonResponse.onSuccess(null);
    }

    @Operation(summary = "회원가입", description = "회원가입을 진행합니다.")
    @PostMapping("/signup")
    public CommonResponse<MemberResponseDTO.getMemberId> register(
            @RequestBody MemberRequestDTO.register request
    ) {
        return CommonResponse.onSuccess(memberService.register(request));
    }

    @Operation(summary = "로그인", description = "로그인을 진행합니다")
    @PostMapping("/login")
    public CommonResponse<AuthResponseDTO.login> login(
            @RequestBody AuthRequestDTO.login request
    ) {
        return CommonResponse.onSuccess(authService.login(request));
    }

    @Operation(summary = "비밀번호 변경", description = "비밀번호 변경")
    @PatchMapping("/password")
    public CommonResponse changePassword(
            @AuthMember Member member,
            @RequestBody AuthRequestDTO.changePassword request
    ) {
        authService.changePassword(member, request);
        return CommonResponse.onNoContent();
    }
}
