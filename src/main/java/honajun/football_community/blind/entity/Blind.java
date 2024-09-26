package honajun.football_community.blind.entity;

import honajun.football_community.global.common.BaseDateTimeEntity;
import honajun.football_community.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "blinds")
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Blind extends BaseDateTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "from_member_id")
    private Member fromMember;

    @ManyToOne
    @JoinColumn(name = "to_member_id", nullable = true)
    private Member toMember;
}
