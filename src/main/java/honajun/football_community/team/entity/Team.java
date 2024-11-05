package honajun.football_community.team.entity;

import honajun.football_community.league.entity.League;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

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

    @Column
    private String founded;

    @Column(nullable = false)
    private String stadium;

    @Column(nullable = false)
    private String headCoach;

    @JoinColumn(name = "league_id", nullable = false)
    @ManyToOne
    private League league;

}