package honajun.football_community.feed.report.service;

import honajun.football_community.feed.report.dto.ReportRequestDTO;
import honajun.football_community.feed.report.repository.ReportRepository;
import honajun.football_community.global.annotation.Adapter;
import honajun.football_community.member.entity.Member;
import lombok.RequiredArgsConstructor;

@Adapter
@RequiredArgsConstructor
public class ReportQueryAdapter {

    private final ReportRepository reportRepository;

    public boolean existsReport(Member member, Long targetId, ReportRequestDTO.createReport request) {
        return reportRepository.existsByReporterAndTargetIdAndTargetType(member, targetId, request.getTargetType());
    }
}
