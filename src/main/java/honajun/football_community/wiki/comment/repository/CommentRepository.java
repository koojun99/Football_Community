package honajun.football_community.wiki.comment.repository;

import honajun.football_community.wiki.comment.entity.Comment;
import honajun.football_community.wiki.entity.Wiki;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByWiki(Wiki wiki);
}

