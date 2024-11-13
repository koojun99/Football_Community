package honajun.football_community.team.entity;

import honajun.football_community.league.entity.League;
import honajun.football_community.wiki.entity.Wiki;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @JoinColumn(name = "league_id", nullable = false)
    @ManyToOne
    private League league;

}