package honajun.football_community.blind.repository;

import honajun.football_community.blind.entity.Blind;
import honajun.football_community.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BlindRepository extends JpaRepository<Blind, Long> {
    Optional<Blind> findByFromMemberAndToMember(Member fromMember, Member toMember);
}
