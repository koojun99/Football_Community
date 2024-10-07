package honajun.football_community.feed.post.entity;

import honajun.football_community.feed.category.Category;
import honajun.football_community.feed.comment.entity.Comment;
import honajun.football_community.global.common.BaseDateTimeEntity;
import honajun.football_community.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.Objects;

@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
public class Post extends BaseDateTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String body;

    @JoinColumn(name = "writer_id")
    @ManyToOne
    private Member writer;

    @JoinColumn(name = "category_id")
    @ManyToOne
    private Category category;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments;


    public void update(String title, String body) {
        if (Objects.nonNull(title)) {
            this.title = title;
        }
        if (Objects.nonNull(body)) {
            this.body = body;
        }
    }

    public boolean isWriter(Long id) {
        return this.writer.getId().equals(id);
    }

    public void removeComment(Comment comment) {
        this.comments.remove(comment);
    }
}
