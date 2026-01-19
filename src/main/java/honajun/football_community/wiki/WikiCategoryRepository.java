package honajun.football_community.wiki;

import honajun.football_community.wiki.entity.WikiCategory;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface WikiCategoryRepository extends JpaRepository<WikiCategory, Long> {
    List<WikiCategory> findByWiki_IdOrderByOrderIndexAsc(Long wikiId);
    
    // 특정 wikiId에서 최대 orderIndex 조회 (새 카테고리 생성 시 사용)
    @Query("SELECT COALESCE(MAX(wc.orderIndex), 0) FROM WikiCategory wc WHERE wc.wiki.id = :wikiId")
    Integer findMaxOrderIndexByWiki_Id(@Param("wikiId") Long wikiId);
}