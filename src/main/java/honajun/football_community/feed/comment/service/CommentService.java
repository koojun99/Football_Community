package honajun.football_community.feed.comment.service;

import honajun.football_community.feed.comment.dto.CommentRequestDTO;
import honajun.football_community.feed.comment.dto.CommentResponseDTO;
import honajun.football_community.feed.comment.entity.Comment;
import honajun.football_community.feed.comment.mapper.CommentMapper;
import honajun.football_community.feed.exception.FeedException;
import honajun.football_community.feed.exception.FeedExceptionCode;
import honajun.football_community.feed.post.entity.Post;
import honajun.football_community.feed.post.service.PostQueryAdapter;
import honajun.football_community.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentCommandAdapter commentCommandAdapter;
    private final CommentQueryAdapter commentQueryAdapter;

    private final PostQueryAdapter postQueryAdapter;

    @Transactional
    public CommentResponseDTO.getCommentId createComment(Member member, Long postId, CommentRequestDTO.createComment request) {
        Post post = postQueryAdapter.findById(postId);
        Comment savedComment = commentCommandAdapter.createComment(CommentMapper.toComment(member, post, request));
        return CommentMapper.toGetCommentId(savedComment);
    }

    @Transactional(readOnly = true)
    public CommentResponseDTO.getCommentList getComments(Long postId) {
        Post post = postQueryAdapter.findById(postId);
        return CommentMapper.toGetCommentList(post.getComments());
    }

    @Transactional
    public CommentResponseDTO.getCommentId updateComment(Member member, Long commentId, CommentRequestDTO.updateComment request) {
        Comment comment = commentQueryAdapter.findById(commentId);
        if (!comment.isWriter(member.getId())) {
            throw new FeedException(FeedExceptionCode._WRONG_WRITER);
        }
        commentCommandAdapter.updateComment(comment, request);
        return CommentMapper.toGetCommentId(comment);
    }

    @Transactional
    public void deleteComment(Member member, Long commentId) {
        Comment comment = commentQueryAdapter.findById(commentId);
        if (!comment.isWriter(member.getId())) {
            throw new FeedException(FeedExceptionCode._WRONG_WRITER);
        }
        commentCommandAdapter.deleteComment(comment);
    }
}
