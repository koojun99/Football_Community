package honajun.football_community.feed.comment.service;

import honajun.football_community.feed.comment.dto.CommentRequestDTO;
import honajun.football_community.feed.comment.entity.Comment;
import honajun.football_community.feed.comment.repository.CommentRepository;
import honajun.football_community.global.annotation.Adapter;
import lombok.RequiredArgsConstructor;

@Adapter
@RequiredArgsConstructor
public class CommentCommandAdapter {

    private final CommentRepository commentRepository;

    public Comment createComment(Comment comment) {
        return commentRepository.save(comment);
    }

    public void updateComment(Comment comment, CommentRequestDTO.updateComment request) {
        comment.update(request.getBody());
    }

    public void deleteComment(Comment comment) {
        comment.getPost().removeComment(comment); // Post와 Comment의 양방향 관계를 해제
        comment.setPost(null);
        commentRepository.delete(comment);
    }
}
