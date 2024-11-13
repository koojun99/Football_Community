package honajun.football_community.wiki.controller;

import honajun.football_community.global.response.CommonResponse;
import honajun.football_community.wiki.dto.WikiResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import honajun.football_community.wiki.service.WikiService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/wiki")
public class WikiController {

    private final WikiService wikiService;

    @Operation(summary = "팀/리그 위키 조회", description = "팀 또는 리그 위키 페이지를 조회합니다.")
    @GetMapping("/{wikiId}")
    public CommonResponse<WikiResponseDTO.getWiki> getWiki(
            @PathVariable Long wikiId
    ) {
        return CommonResponse.onSuccess(wikiService.getWiki(wikiId));
    }
}