package honajun.football_community.wiki.comment.entity;

import honajun.football_community.global.common.BaseDateTimeEntity;
import honajun.football_community.member.entity.Member;
import honajun.football_community.wiki.entity.Wiki;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
public class Comment extends BaseDateTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String body;

    @JoinColumn(name = "writer_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Member writer;

    @JoinColumn(name = "wiki_id")
    @ManyToOne(fetch = FetchType.LAZY)
    @Setter
    private Wiki wiki;

    public boolean isWriter(Long id) {
        return this.writer.getId().equals(id);
    }

    public void update(String body) {
        this.body = body;
    }
}

