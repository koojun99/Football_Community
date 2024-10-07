package honajun.football_community.feed.report.dto;

import lombok.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ReportResponseDTO {

    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    public static class onSuccess {

        private Long targetId;

        private Long reportId;
    }
}
