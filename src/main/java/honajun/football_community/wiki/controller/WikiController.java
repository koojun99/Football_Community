package honajun.football_community.wiki.controller;

import honajun.football_community.global.annotation.AuthMember;
import honajun.football_community.global.response.CommonResponse;
import honajun.football_community.wiki.WikiRequestDTO;
import honajun.football_community.wiki.dto.WikiResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import honajun.football_community.wiki.service.WikiService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/wikis")
public class WikiController {

    private final WikiService wikiService;

    @Operation(summary = "팀/리그 위키 조회", description = "팀 또는 리그 위키 페이지를 조회합니다.")
    @GetMapping("/{wikiId}")
    public CommonResponse<WikiResponseDTO.getWiki> getWiki(
            @PathVariable Long wikiId
    ) {
        return CommonResponse.onSuccess(wikiService.getWiki(wikiId));
    }

    @Operation(summary = "카테고리 생성", description = "위키 카테고리를 생성합니다.")
    @PostMapping("/{wikiId}/categories")
    public CommonResponse<Void> createCategory (
            @PathVariable Long wikiId,
            @RequestBody WikiRequestDTO.createCategory request
    ) {
        wikiService.createCategory(wikiId, request);
        return CommonResponse.onNoContent();
    }

    @Operation(summary = "위키 수정", description = "위키를 수정합니다.")
    @PutMapping("/{wikiId}/categories/{wikiCategoryId}")
    public CommonResponse<Void> updateWiki (
            @PathVariable Long wikiCategoryId,
            @RequestBody WikiRequestDTO.updateWiki request
    ) {
        wikiService.updateWiki(wikiCategoryId, request);
        return CommonResponse.onNoContent();
    }
}