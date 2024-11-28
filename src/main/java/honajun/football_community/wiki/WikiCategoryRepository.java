package honajun.football_community.wiki;

import honajun.football_community.wiki.entity.WikiCategory;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WikiCategoryRepository extends JpaRepository<WikiCategory, Long> {
    List<WikiCategory> findByWiki_Id(Long wikiId);
}