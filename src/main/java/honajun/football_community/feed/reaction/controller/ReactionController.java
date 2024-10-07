package honajun.football_community.feed.reaction.controller;

import honajun.football_community.feed.reaction.dto.ReactionRequestDTO;
import honajun.football_community.feed.reaction.dto.ReactionResponseDTO;
import honajun.football_community.feed.reaction.service.ReactionService;
import honajun.football_community.global.annotation.AuthMember;
import honajun.football_community.global.response.CommonResponse;
import honajun.football_community.member.entity.Member;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/feed/{targetId}/reaction")
public class ReactionController {

    private final ReactionService reactionService;

    @Operation(summary = "좋아요/싫어요 추가 또는 삭제", description = "게시글 또는 댓글에 좋아요 또는 싫어요를 추가합니다. 동일한 리액션을 다시 누르면 삭제됩니다.")
    @PostMapping
    public CommonResponse<ReactionResponseDTO.onSuccess> createReaction(
            @AuthMember Member member,
            @PathVariable Long targetId,
            @RequestBody @Valid ReactionRequestDTO.createReaction request
    ) {
        return CommonResponse.onSuccess(reactionService.createReaction(member, targetId, request));
    }
}
