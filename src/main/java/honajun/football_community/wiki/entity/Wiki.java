package honajun.football_community.wiki.entity;

import honajun.football_community.global.common.BaseDateTimeEntity;
import honajun.football_community.wiki.comment.entity.Comment;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
public class Wiki extends BaseDateTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = true)
    private Long teamId; // 팀 위키인 경우 팀 ID

    @Column(nullable = true)
    private Long leagueId; // 리그 위키인 경우 리그 ID

    @OneToMany(mappedBy = "wiki", cascade = CascadeType.ALL)
    private List<WikiCategory> categories;

    @OneToMany(mappedBy = "wiki", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments;
}
