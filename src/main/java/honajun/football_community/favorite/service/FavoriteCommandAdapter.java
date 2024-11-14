package honajun.football_community.favorite.service;

import honajun.football_community.favorite.entity.Favorite;
import lombok.RequiredArgsConstructor;
import honajun.football_community.global.annotation.Adapter;
import honajun.football_community.favorite.repository.FavoriteRepository;

@Adapter
@RequiredArgsConstructor
public class FavoriteCommandAdapter {

    private final FavoriteRepository favoriteRepository;

    public void save(Favorite favorite) {
        favoriteRepository.save(favorite);
    }

    public void delete(Favorite favorite) {
        favoriteRepository.delete(favorite);
    }
}