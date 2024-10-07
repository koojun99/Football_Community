package honajun.football_community.feed.report.dto;

import honajun.football_community.global.enums.reaction.TargetType;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ReportRequestDTO {

    @Getter
    public static class createReport {

        @NotNull(message = "신고 대상을 입력해주세요(POST, COMMENT)")
        private TargetType targetType;

        @NotNull(message = "신고 사유를 입력해주세요")
        private String reportReason;
    }
}
