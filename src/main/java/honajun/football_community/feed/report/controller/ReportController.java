package honajun.football_community.feed.report.controller;

import honajun.football_community.feed.report.dto.ReportRequestDTO;
import honajun.football_community.feed.report.dto.ReportResponseDTO;
import honajun.football_community.feed.report.service.ReportService;
import honajun.football_community.global.annotation.AuthMember;
import honajun.football_community.global.response.CommonResponse;
import honajun.football_community.member.entity.Member;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/feed/{targetId}/report")
public class ReportController {

    private final ReportService reportService;

    @Operation(summary = "신고하기", description = "게시글 또는 댓글을 신고합니다.")
    @PostMapping
    public CommonResponse<ReportResponseDTO.onSuccess> createReport(
            @AuthMember Member member,
            @PathVariable Long targetId,
            @RequestBody ReportRequestDTO.createReport request
    ) {
        return CommonResponse.onSuccess(reportService.createReport(member, targetId, request));
    }
}
