package honajun.football_community.league.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
public class League {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private String country;

    @Column(nullable = false)
    private Integer division;

    @Column
    private String founded;

    @Column
    private String mostRecentChampion;

    @Column
    private String mostWinningTeam;

}