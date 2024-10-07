package honajun.football_community.feed.reaction.mapper;

import honajun.football_community.feed.comment.entity.Comment;
import honajun.football_community.feed.post.entity.Post;
import honajun.football_community.feed.reaction.dto.ReactionRequestDTO;
import honajun.football_community.feed.reaction.dto.ReactionResponseDTO;
import honajun.football_community.feed.reaction.entity.Reaction;
import honajun.football_community.member.entity.Member;

public class ReactionMapper {

    public static Reaction toPostReaction(Member member, Post targetPost, ReactionRequestDTO.createReaction request) {
        return Reaction.builder()
                .targetType(request.getTargetType())
                .targetId(targetPost.getId())
                .reactionType(request.getReactionType())
                .post(targetPost)
                .reactor(member)
                .build();
    }

    public static Reaction toCommentReaction(Member member, Comment targetComment, ReactionRequestDTO.createReaction request) {
        return Reaction.builder()
                .targetType(request.getTargetType())
                .targetId(targetComment.getId())
                .reactionType(request.getReactionType())
                .comment(targetComment)
                .reactor(member)
                .build();
    }

    public static ReactionResponseDTO.onSuccess toCreateReaction(Reaction reaction) {
        return ReactionResponseDTO.onSuccess.builder()
                .targetId(reaction.getTargetId())
                .reactionId(reaction.getId())
                .reactionMessage(reaction.getReactionType().getMessage() + "를 눌렀습니다")
                .build();
    }

    public static ReactionResponseDTO.onSuccess toDeleteReaction(Reaction reaction) {
        return ReactionResponseDTO.onSuccess.builder()
                .targetId(reaction.getTargetId())
                .reactionId(reaction.getId())
                .reactionMessage(reaction.getReactionType().getMessage() + "를 취소했습니다")
                .build();
    }
}
