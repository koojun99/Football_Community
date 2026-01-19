package honajun.football_community.wiki.comment.mapper;

import honajun.football_community.wiki.comment.dto.CommentRequestDTO;
import honajun.football_community.wiki.comment.dto.CommentResponseDTO;
import honajun.football_community.wiki.comment.entity.Comment;
import honajun.football_community.wiki.entity.Wiki;
import honajun.football_community.member.entity.Member;
import java.util.List;
import java.util.stream.Collectors;

public class CommentMapper {

    public static Comment toComment(Member member, Wiki wiki, CommentRequestDTO.createComment request) {
        return Comment.builder()
                .body(request.getBody())
                .writer(member)
                .wiki(wiki)
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

