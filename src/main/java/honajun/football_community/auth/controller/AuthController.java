package honajun.football_community.auth.controller;

import honajun.football_community.auth.dto.AuthRequestDTO;
import honajun.football_community.auth.service.AuthService;
import honajun.football_community.global.response.CommonResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

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
}
