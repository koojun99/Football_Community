package honajun.football_community.wiki.comment.service;

import honajun.football_community.global.annotation.Adapter;
import honajun.football_community.wiki.comment.entity.Comment;
import honajun.football_community.wiki.comment.repository.CommentRepository;
import honajun.football_community.wiki.exception.WikiException;
import honajun.football_community.wiki.exception.WikiExceptionCode;
import lombok.RequiredArgsConstructor;

@Adapter
@RequiredArgsConstructor
public class CommentQueryAdapter {

    private final CommentRepository commentRepository;

    public Comment findById(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new WikiException(WikiExceptionCode.COMMENT_NOT_FOUND));
    }
}

