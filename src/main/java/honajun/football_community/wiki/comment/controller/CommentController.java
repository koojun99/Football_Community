package honajun.football_community.wiki.comment.controller;

import honajun.football_community.global.annotation.AuthMember;
import honajun.football_community.global.response.CommonResponse;
import honajun.football_community.member.entity.Member;
import honajun.football_community.wiki.comment.dto.CommentRequestDTO;
import honajun.football_community.wiki.comment.dto.CommentResponseDTO;
import honajun.football_community.wiki.comment.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/wikis/{wikiId}/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @Operation(summary = "댓글 작성", description = "Wiki에 댓글을 작성합니다.")
    @PostMapping
    public CommonResponse<CommentResponseDTO.getCommentId> createComment(
            @AuthMember Member member,
            @PathVariable Long wikiId,
            @RequestBody CommentRequestDTO.createComment request
    ) {
        return CommonResponse.onSuccess(commentService.createComment(member, wikiId, request));
    }

    @Operation(summary = "댓글 조회", description = "Wiki의 댓글 목록을 조회합니다.")
    @GetMapping
    public CommonResponse<CommentResponseDTO.getCommentList> getComments(
            @PathVariable Long wikiId
    ) {
        return CommonResponse.onSuccess(commentService.getComments(wikiId));
    }

    @Operation(summary = "댓글 수정", description = "댓글을 수정합니다.")
    @PutMapping("/{commentId}")
    public CommonResponse<CommentResponseDTO.getCommentId> updateComment(
            @AuthMember Member member,
            @PathVariable Long commentId,
            @RequestBody CommentRequestDTO.updateComment request
    ) {
        return CommonResponse.onSuccess(commentService.updateComment(member, commentId, request));
    }

    @Operation(summary = "댓글 삭제", description = "댓글을 삭제합니다.")
    @DeleteMapping("/{commentId}")
    public CommonResponse<Void> deleteComment(
            @AuthMember Member member,
            @PathVariable Long commentId
    ) {
        commentService.deleteComment(member, commentId);
        return CommonResponse.onNoContent();
    }
}

