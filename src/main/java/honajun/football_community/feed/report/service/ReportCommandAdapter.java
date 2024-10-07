package honajun.football_community.feed.report.service;

import honajun.football_community.feed.report.entity.Report;
import honajun.football_community.feed.report.repository.ReportRepository;
import honajun.football_community.global.annotation.Adapter;
import lombok.RequiredArgsConstructor;

@Adapter
@RequiredArgsConstructor
public class ReportCommandAdapter {

    private final ReportRepository reportRepository;

    public Report createReport(Report report) {
        return reportRepository.save(report);
    }
}
