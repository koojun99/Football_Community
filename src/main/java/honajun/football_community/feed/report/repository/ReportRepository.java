package honajun.football_community.feed.report.repository;

import honajun.football_community.feed.report.entity.Report;
import honajun.football_community.global.enums.reaction.TargetType;
import honajun.football_community.member.entity.Member;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {
    boolean existsByReporterAndTargetIdAndTargetType(Member reporter, Long targetId, @NotNull(message = "신고 대상을 입력해주세요(POST, COMMENT)") TargetType targetType);
}
