package honajun.football_community.fixture.mapper;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import honajun.football_community.fixture.dto.TransferResponseDTO;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class TransferMapper {

    private static final ObjectMapper objectMapper = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    /**
     * API-Football /transfers 응답을 내부 DTO로 변환합니다.
     *
     * 예상 구조:
     * {
     *   "parameters": { "league": 39, "season": "2024" },
     *   "response": [
     *     {
     *       "player": { "id": ..., "name": "..." },
     *       "transfers": [
     *         {
     *           "date": "2024-01-15",
     *           "type": "Transfer",
     *           "teams": {
     *             "in": { "id": ..., "name": "..." },
     *             "out": { "id": ..., "name": "..." }
     *           },
     *           "duration": "...",  // 존재할 수도, 없을 수도 있음
     *           "fee": "€50m"      // 존재할 수도, 없을 수도 있음
     *         }
     *       ]
     *     }
     *   ]
     * }
     */
    public static TransferResponseDTO.getTransfers toGetTransfers(String jsonResponse) throws IOException {
        JsonNode rootNode = objectMapper.readTree(jsonResponse);
        JsonNode responseNode = rootNode.get("response");
        JsonNode parametersNode = rootNode.get("parameters");

        Long leagueId = null;
        String season = null;

        if (parametersNode != null) {
            if (parametersNode.has("league") && !parametersNode.get("league").isNull()) {
                leagueId = parametersNode.get("league").asLong();
            }
            if (parametersNode.has("season") && !parametersNode.get("season").isNull()) {
                season = parametersNode.get("season").asText();
            }
        }

        List<TransferResponseDTO.Transfer> transfers = new ArrayList<>();

        if (responseNode != null && responseNode.isArray()) {
            for (JsonNode playerWrapper : responseNode) {
                JsonNode playerNode = playerWrapper.get("player");
                JsonNode transfersArray = playerWrapper.get("transfers");

                if (transfersArray == null || !transfersArray.isArray()) {
                    continue;
                }

                Long playerId = playerNode != null && playerNode.has("id") && !playerNode.get("id").isNull()
                        ? playerNode.get("id").asLong()
                        : null;
                String playerName = playerNode != null && playerNode.has("name")
                        ? playerNode.get("name").asText()
                        : null;

                for (JsonNode transferNode : transfersArray) {
                    JsonNode teamsNode = transferNode.get("teams");
                    JsonNode inNode = teamsNode != null ? teamsNode.get("in") : null;
                    JsonNode outNode = teamsNode != null ? teamsNode.get("out") : null;

                    String dateStr = transferNode.has("date") && !transferNode.get("date").isNull()
                            ? transferNode.get("date").asText()
                            : null;
                    LocalDate transferDate = null;
                    if (dateStr != null) {
                        try {
                            transferDate = LocalDate.parse(dateStr);
                        } catch (DateTimeParseException ignored) {
                        }
                    }

                    String type = transferNode.has("type") && !transferNode.get("type").isNull()
                            ? transferNode.get("type").asText()
                            : null;

                    String fee = transferNode.has("fee") && !transferNode.get("fee").isNull()
                            ? transferNode.get("fee").asText()
                            : null;

                    TransferResponseDTO.Transfer transfer = TransferResponseDTO.Transfer.builder()
                            .playerId(playerId)
                            .playerName(playerName)
                            .fromTeamId(outNode != null && outNode.has("id") && !outNode.get("id").isNull()
                                    ? outNode.get("id").asLong()
                                    : null)
                            .fromTeamName(outNode != null && outNode.has("name")
                                    ? outNode.get("name").asText()
                                    : null)
                            .toTeamId(inNode != null && inNode.has("id") && !inNode.get("id").isNull()
                                    ? inNode.get("id").asLong()
                                    : null)
                            .toTeamName(inNode != null && inNode.has("name")
                                    ? inNode.get("name").asText()
                                    : null)
                            .transferDate(transferDate)
                            .transferType(type)
                            .fee(fee)
                            .build();

                    transfers.add(transfer);
                }
            }
        }

        return TransferResponseDTO.getTransfers.builder()
                .leagueId(leagueId)
                .season(season)
                .window(null) // 서비스 레이어에서 현재 윈도우를 설정
                .transfers(transfers)
                .build();
    }
}

