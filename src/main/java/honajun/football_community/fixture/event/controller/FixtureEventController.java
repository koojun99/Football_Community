package honajun.football_community.fixture.event.controller;

import honajun.football_community.fixture.event.service.FixtureEventService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/fixtures/events")
@RequiredArgsConstructor
public class FixtureEventController {

    private final FixtureEventService fixtureEventService;

    @Operation(summary = "실시간 경기 이벤트 구독", description = "특정 경기의 실시간 이벤트를 SSE로 구독합니다. Follow된 경기만 구독 가능합니다.")
    @GetMapping(value = "/{fixtureId}/subscribe", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter subscribeToFixtureEvents(@PathVariable Long fixtureId) {
        return fixtureEventService.subscribe(fixtureId);
    }
}

