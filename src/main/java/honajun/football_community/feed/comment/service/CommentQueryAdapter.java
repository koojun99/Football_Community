package honajun.football_community.feed.comment.service;

import honajun.football_community.feed.comment.entity.Comment;
import honajun.football_community.feed.comment.repository.CommentRepository;
import honajun.football_community.feed.exception.FeedException;
import honajun.football_community.feed.exception.FeedExceptionCode;
import honajun.football_community.global.annotation.Adapter;
import lombok.RequiredArgsConstructor;

@Adapter
@RequiredArgsConstructor
public class CommentQueryAdapter {

    private final CommentRepository commentRepository;

    public Comment findById(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new FeedException(FeedExceptionCode._COMMENT_NOT_FOUND));
    }
}
