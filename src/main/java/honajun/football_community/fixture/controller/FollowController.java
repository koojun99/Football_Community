package honajun.football_community.fixture.controller;

import honajun.football_community.fixture.dto.FollowRequestDTO;
import honajun.football_community.fixture.dto.FollowResponseDTO;
import honajun.football_community.fixture.service.FollowService;
import honajun.football_community.global.annotation.AuthMember;
import honajun.football_community.global.response.CommonResponse;
import honajun.football_community.member.entity.Member;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Follow 관련 API를 제공하는 컨트롤러
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/follows")
public class FollowController {

    private final FollowService followService;

    @Operation(summary = "경기 Follow 추가", description = "특정 경기를 Follow합니다.")
    @PostMapping
    public CommonResponse<FollowResponseDTO.getFollow> addFollow(
            @AuthMember Member member,
            @RequestBody FollowRequestDTO.addFollow request) {
        return CommonResponse.onSuccess(followService.addFollow(member, request));
    }

    @Operation(summary = "Follow 삭제", description = "특정 Follow를 삭제합니다.")
    @DeleteMapping("/{followId}")
    public CommonResponse<Void> deleteFollow(
            @AuthMember Member member,
            @PathVariable Long followId) {
        followService.deleteFollow(followId);
        return CommonResponse.onSuccess(null);
    }

    @Operation(summary = "Follow 목록 조회", description = "사용자의 Follow 목록을 조회합니다.")
    @GetMapping
    public CommonResponse<FollowResponseDTO.getFollows> getFollows(
            @AuthMember Member member) {
        return CommonResponse.onSuccess(followService.getFollows(member));
    }
}

