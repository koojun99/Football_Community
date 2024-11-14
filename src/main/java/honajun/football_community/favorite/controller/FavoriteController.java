package honajun.football_community.favorite.controller;

import honajun.football_community.favorite.dto.FavoriteRequestDTO;
import honajun.football_community.favorite.dto.FavoriteResponseDTO;
import honajun.football_community.global.annotation.AuthMember;
import honajun.football_community.global.response.CommonResponse;
import honajun.football_community.member.entity.Member;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import honajun.football_community.favorite.service.FavoriteService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/favorites")
public class FavoriteController {

    private final FavoriteService favoriteService;

    @Operation(summary = "즐겨찾기 추가", description = "특정 팀/리그를 즐겨찾기로 추가합니다.")
    @PostMapping
    public CommonResponse<FavoriteResponseDTO.getFavorite> addFavorite(
            @AuthMember Member member,
            @RequestBody FavoriteRequestDTO.addFavorite request) {
        return CommonResponse.onSuccess(favoriteService.addFavorite(member, request));
    }

    @Operation(summary = "즐겨찾기 삭제", description = "특정 즐겨찾기를 삭제합니다.")
    @DeleteMapping("/{favoriteId}")
    public CommonResponse<Void> deleteFavorite(
            @AuthMember Member member,
            @PathVariable Long favoriteId
    ) {
        favoriteService.deleteFavorite(favoriteId);
        return CommonResponse.onSuccess(null);
    }

    @Operation(summary = "즐겨찾기 알림 켜기 및 끄기", description = "특정 즐겨찾기의 알림을 켜거나 끕니다.")
    @PostMapping("/{favoriteId}/notification")
    public CommonResponse<Void> toggleNotification(
            @AuthMember Member member,
            @PathVariable Long favoriteId
    ) {
        favoriteService.toggleNotification(member, favoriteId);
        return CommonResponse.onSuccess(null);
    }

    @Operation(summary = "즐겨찾기 목록 조회", description = "사용자의 즐겨찾기 목록을 조회합니다.")
    @GetMapping
    public CommonResponse<FavoriteResponseDTO.getFavorites> getFavorites(
            @AuthMember Member member
    ) {
        return CommonResponse.onSuccess(favoriteService.getFavorites(member));
    }
}
