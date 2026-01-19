package honajun.football_community.wiki.comment.service;

import honajun.football_community.global.annotation.Adapter;
import honajun.football_community.wiki.comment.dto.CommentRequestDTO;
import honajun.football_community.wiki.comment.entity.Comment;
import honajun.football_community.wiki.comment.repository.CommentRepository;
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
        commentRepository.delete(comment);
    }
}

