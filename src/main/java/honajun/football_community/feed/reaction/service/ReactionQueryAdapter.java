package honajun.football_community.feed.reaction.service;

import honajun.football_community.feed.reaction.dto.ReactionRequestDTO;
import honajun.football_community.feed.reaction.entity.Reaction;
import honajun.football_community.feed.reaction.repository.ReactionRepository;
import honajun.football_community.global.annotation.Adapter;
import honajun.football_community.member.entity.Member;
import lombok.RequiredArgsConstructor;

@Adapter
@RequiredArgsConstructor
public class ReactionQueryAdapter {

        private final ReactionRepository reactionRepository;

        public boolean existsReaction(Member member, Long targetId, ReactionRequestDTO.createReaction request) {
            return reactionRepository.existsByReactorAndTargetIdAndTargetTypeAndReactionType(member, targetId, request.getTargetType(), request.getReactionType());
        }

        public Reaction findByReactorAndTargetAndReactionType(Member member, Long targetId, ReactionRequestDTO.createReaction request) {
            return reactionRepository.findByReactorAndTargetIdAndTargetTypeAndReactionType(member, targetId, request.getTargetType(), request.getReactionType());
        }
}
