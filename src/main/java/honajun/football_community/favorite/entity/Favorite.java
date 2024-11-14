package honajun.football_community.favorite.entity;

import honajun.football_community.global.enums.favorite.FavoriteType;
import honajun.football_community.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
public class Favorite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long targetId; // 팀 ID 또는 리그 ID

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member; // 사용자를 참조

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FavoriteType favoriteType; // FAVORITE_TEAM, FAVORITE_LEAGUE

    @Column(nullable = false)
    private boolean isPushed; // 알림 활성화 여부 (삭제 시 비활성화 처리)

    public void toggleNotification() {
        this.isPushed = !this.isPushed;
    }
}