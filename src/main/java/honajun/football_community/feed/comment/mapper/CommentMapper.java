package honajun.football_community.feed.comment.mapper;

import honajun.football_community.feed.comment.dto.CommentRequestDTO;
import honajun.football_community.feed.comment.dto.CommentResponseDTO;
import honajun.football_community.feed.comment.entity.Comment;
import honajun.football_community.feed.post.entity.Post;
import honajun.football_community.member.entity.Member;

import java.util.List;
import java.util.stream.Collectors;

public class CommentMapper {

    public static Comment toComment(Member member, Post post, CommentRequestDTO.createComment request) {
        return Comment.builder()
                .body(request.getBody())
                .writer(member)
                .post(post)
                .build();
    }

    public static CommentResponseDTO.getCommentId toGetCommentId(Comment comment) {
        return CommentResponseDTO.getCommentId.builder()
                .commentId(comment.getId())
                .build();
    }

    public static CommentResponseDTO.getComment toGetComment(Comment comment) {
        return CommentResponseDTO.getComment.builder()
                .commentId(comment.getId())
                .body(comment.getBody())
                .writerId(comment.getWriter().getId())
                .writerName(comment.getWriter().getUsername())
                .build();
    }

    public static CommentResponseDTO.getCommentList toGetCommentList(List<Comment> comments) {
        List<CommentResponseDTO.getComment> commentList = comments.stream()
                .map(CommentMapper::toGetComment)
                .collect(Collectors.toList());
        return CommentResponseDTO.getCommentList.builder()
                .comments(commentList)
                .build();
    }
}
