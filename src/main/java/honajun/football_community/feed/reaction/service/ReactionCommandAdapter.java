package honajun.football_community.feed.reaction.service;

import honajun.football_community.feed.reaction.entity.Reaction;
import honajun.football_community.feed.reaction.repository.ReactionRepository;
import honajun.football_community.global.annotation.Adapter;
import lombok.RequiredArgsConstructor;

@Adapter
@RequiredArgsConstructor
public class ReactionCommandAdapter {

    private final ReactionRepository reactionRepository;

    public Reaction createReaction(Reaction postReaction) {
       return reactionRepository.save(postReaction);
    }

    public void deleteReaction(Reaction reaction) {
        reactionRepository.delete(reaction);
    }
}
