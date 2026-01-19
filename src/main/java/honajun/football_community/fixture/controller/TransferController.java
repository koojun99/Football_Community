package honajun.football_community.fixture.controller;

import honajun.football_community.fixture.dto.TransferResponseDTO;
import honajun.football_community.fixture.service.TransferService;
import honajun.football_community.global.response.CommonResponse;
import io.swagger.v3.oas.annotations.Operation;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 이적시장(Transfers) 정보를 조회하는 컨트롤러
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/transfers")
public class TransferController {

    private final TransferService transferService;

    @Operation(summary = "이적시장 정보 조회",
            description = "오늘 날짜를 기준으로 현재 이적시장(겨울/여름)에 해당하는 이적 정보만 조회합니다. 리그별 필터링이 가능합니다.")
    @GetMapping
    public CommonResponse<TransferResponseDTO.getTransfers> getCurrentWindowTransfers(
            @RequestParam(required = false) Long leagueId) throws IOException {
        return CommonResponse.onSuccess(transferService.getCurrentWindowTransfers(leagueId));
    }
}

