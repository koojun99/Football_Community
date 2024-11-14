package honajun.football_community.wiki;

import honajun.football_community.wiki.entity.WikiCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WikiCategoryRepository extends JpaRepository<WikiCategory, Long> {
}