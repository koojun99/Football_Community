package honajun.football_community.feed.reaction.service;

import honajun.football_community.feed.comment.entity.Comment;
import honajun.football_community.feed.comment.service.CommentQueryAdapter;
import honajun.football_community.feed.post.entity.Post;
import honajun.football_community.feed.post.service.PostQueryAdapter;
import honajun.football_community.feed.reaction.dto.ReactionRequestDTO;
import honajun.football_community.feed.reaction.dto.ReactionResponseDTO;
import honajun.football_community.feed.reaction.entity.Reaction;
import honajun.football_community.feed.reaction.mapper.ReactionMapper;
import honajun.football_community.global.enums.reaction.TargetType;
import honajun.football_community.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReactionService {

    private final ReactionCommandAdapter reactionCommandAdapter;
    private final ReactionQueryAdapter reactionQueryAdapter;

    private final PostQueryAdapter postQueryAdapter;
    private final CommentQueryAdapter commentQueryAdapter;

    @Transactional
    public ReactionResponseDTO.onSuccess createReaction(Member member, Long targetId, ReactionRequestDTO.createReaction request) {
        // TODO: 동시성 문제 해결 위한 Lock 처리(10초 이내에 다시 재요청(취소) 불가하도록)
        // Reaction이 이미 존재하는 경우 삭제 처리
        if (reactionQueryAdapter.existsReaction(member, targetId, request)) {
            Reaction reaction = reactionQueryAdapter.findByReactorAndTargetAndReactionType(member, targetId, request);
            deleteReaction(reaction);
            return ReactionMapper.toDeleteReaction(reaction);
        }

        // Reaction 생성 로직 통합 처리
        Reaction savedReaction = createReactionBasedOnTargetType(member, targetId, request);

        return ReactionMapper.toCreateReaction(savedReaction);
    }

    @Transactional
    protected void deleteReaction(Reaction reaction) {
        reactionCommandAdapter.deleteReaction(reaction);
    }

    // TargetType에 따른 Reaction 생성 로직 통합
    private Reaction createReactionBasedOnTargetType(Member member, Long targetId, ReactionRequestDTO.createReaction request) {
        Reaction reaction = null;

        if (request.getTargetType().equals(TargetType.POST)) {
            Post targetPost = postQueryAdapter.findById(targetId);
            reaction = ReactionMapper.toPostReaction(member, targetPost, request);
        } else if (request.getTargetType().equals(TargetType.COMMENT)) {
            Comment targetComment = commentQueryAdapter.findById(targetId);
            reaction = ReactionMapper.toCommentReaction(member, targetComment, request);
        }

        return reactionCommandAdapter.createReaction(reaction);
    }
}
