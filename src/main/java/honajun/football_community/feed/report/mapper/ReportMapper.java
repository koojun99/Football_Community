package honajun.football_community.feed.report.mapper;

import honajun.football_community.feed.comment.entity.Comment;
import honajun.football_community.feed.post.entity.Post;
import honajun.football_community.feed.report.dto.ReportRequestDTO;
import honajun.football_community.feed.report.dto.ReportResponseDTO;
import honajun.football_community.feed.report.entity.Report;
import honajun.football_community.member.entity.Member;

public class ReportMapper {

    public static Report toPostReport(Member member, Post targetPost, ReportRequestDTO.createReport request) {
        return Report.builder()
                .reporter(member)
                .targetType(request.getTargetType())
                .targetId(targetPost.getId())
                .post(targetPost)
                .reason(request.getReportReason())
                .build();
    }

    public static Report toCommentReport(Member member, Comment targetComment, ReportRequestDTO.createReport request) {
        return Report.builder()
                .reporter(member)
                .targetType(request.getTargetType())
                .targetId(targetComment.getId())
                .comment(targetComment)
                .reason(request.getReportReason())
                .build();
    }

    public static ReportResponseDTO.onSuccess toCreateReport(Report report) {
        return ReportResponseDTO.onSuccess.builder()
                .targetId(report.getTargetId())
                .reportId(report.getId())
                .build();
    }
}
