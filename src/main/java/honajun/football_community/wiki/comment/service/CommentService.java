package honajun.football_community.wiki.comment.service;

import honajun.football_community.member.entity.Member;
import honajun.football_community.wiki.comment.dto.CommentRequestDTO;
import honajun.football_community.wiki.comment.dto.CommentResponseDTO;
import honajun.football_community.wiki.comment.entity.Comment;
import honajun.football_community.wiki.comment.mapper.CommentMapper;
import honajun.football_community.wiki.comment.repository.CommentRepository;
import honajun.football_community.wiki.entity.Wiki;
import honajun.football_community.wiki.exception.WikiException;
import honajun.football_community.wiki.exception.WikiExceptionCode;
import honajun.football_community.wiki.service.WikiQueryAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Wiki 댓글 관련 비즈니스 로직을 처리하는 서비스
 */
@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentCommandAdapter commentCommandAdapter;
    private final CommentQueryAdapter commentQueryAdapter;
    private final CommentRepository commentRepository;
    private final WikiQueryAdapter wikiQueryAdapter;

    /**
     * Wiki에 댓글을 생성합니다.
     */
    @Transactional
    public CommentResponseDTO.getCommentId createComment(Member member, Long wikiId, CommentRequestDTO.createComment request) {
        Wiki wiki = wikiQueryAdapter.findById(wikiId);
        Comment savedComment = commentCommandAdapter.createComment(CommentMapper.toComment(member, wiki, request));
        return CommentMapper.toGetCommentId(savedComment);
    }

    /**
     * Wiki의 댓글 목록을 조회합니다.
     */
    @Transactional(readOnly = true)
    public CommentResponseDTO.getCommentList getComments(Long wikiId) {
        Wiki wiki = wikiQueryAdapter.findById(wikiId);
        List<Comment> comments = commentRepository.findAllByWiki(wiki);
        return CommentMapper.toGetCommentList(comments);
    }

    /**
     * 댓글을 수정합니다.
     */
    @Transactional
    public CommentResponseDTO.getCommentId updateComment(Member member, Long commentId, CommentRequestDTO.updateComment request) {
        Comment comment = commentQueryAdapter.findById(commentId);
        if (!comment.isWriter(member.getId())) {
            throw new WikiException(WikiExceptionCode.WRONG_WRITER);
        }
        commentCommandAdapter.updateComment(comment, request);
        return CommentMapper.toGetCommentId(comment);
    }

    /**
     * 댓글을 삭제합니다.
     */
    @Transactional
    public void deleteComment(Member member, Long commentId) {
        Comment comment = commentQueryAdapter.findById(commentId);
        if (!comment.isWriter(member.getId())) {
            throw new WikiException(WikiExceptionCode.WRONG_WRITER);
        }
        commentCommandAdapter.deleteComment(comment);
    }
}

