package honajun.football_community.feed.report.service;

import honajun.football_community.feed.comment.entity.Comment;
import honajun.football_community.feed.comment.service.CommentQueryAdapter;
import honajun.football_community.feed.exception.FeedException;
import honajun.football_community.feed.exception.FeedExceptionCode;
import honajun.football_community.feed.post.entity.Post;
import honajun.football_community.feed.post.service.PostQueryAdapter;
import honajun.football_community.feed.report.dto.ReportRequestDTO;
import honajun.football_community.feed.report.dto.ReportResponseDTO;
import honajun.football_community.feed.report.entity.Report;
import honajun.football_community.feed.report.mapper.ReportMapper;
import honajun.football_community.global.enums.reaction.TargetType;
import honajun.football_community.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final ReportCommandAdapter reportCommandAdapter;
    private final ReportQueryAdapter reportQueryAdapter;

    private final PostQueryAdapter postQueryAdapter;

    private final CommentQueryAdapter commentQueryAdapter;

    @Transactional
    public ReportResponseDTO.onSuccess createReport(Member member, Long targetId, ReportRequestDTO.createReport request) {

        if (reportQueryAdapter.existsReport(member, targetId, request)) {
            throw new FeedException(FeedExceptionCode._REPORT_ALREADY_EXISTS);
        }
        Report savedReport = createReportBasedOnTargetType(member, targetId, request);

        return ReportMapper.toCreateReport(savedReport);
    }

    private Report createReportBasedOnTargetType(Member member, Long targetId, ReportRequestDTO.createReport request) {
        Report report = null;

        if (request.getTargetType().equals(TargetType.POST)) {
            Post targetPost = postQueryAdapter.findById(targetId);
            report = ReportMapper.toPostReport(member, targetPost, request);
        } else if (request.getTargetType().equals(TargetType.COMMENT)) {
            Comment targetComment = commentQueryAdapter.findById(targetId);
            report = ReportMapper.toCommentReport(member, targetComment, request);
        }

        return reportCommandAdapter.createReport(report);
    }
}
