package honajun.football_community.wiki;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import honajun.football_community.global.security.jwt.JwtTokenProvider;
import honajun.football_community.wiki.controller.WikiController;
import honajun.football_community.wiki.dto.WikiResponseDTO.getCategory;
import honajun.football_community.wiki.dto.WikiResponseDTO.getWiki;
import honajun.football_community.wiki.service.WikiCommandAdapter;
import honajun.football_community.wiki.service.WikiQueryAdapter;
import honajun.football_community.wiki.service.WikiService;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = WikiController.class, excludeAutoConfiguration = SecurityAutoConfiguration.class)
@MockBean(JpaMetamodelMappingContext.class)
class WikiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WikiService wikiService;

    @MockBean
    private WikiCommandAdapter wikiCommandAdapter;

    @MockBean
    private WikiQueryAdapter wikiQueryAdapter;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @Test
    void getWiki_shouldReturnWikiResponse() throws Exception {
        // Mock 데이터 준비
        Long wikiId = 1L;

        List<getCategory> categories = Arrays.asList(
                getCategory.builder()
                        .id(101L)
                        .name("History")
                        .description("The club was founded in 1878 as Newton Heath.")
                        .build(),
                getCategory.builder()
                        .id(102L)
                        .name("Stadium")
                        .description("Manchester United's stadium is Old Trafford.")
                        .build()
        );

        getWiki mockResponse = getWiki.builder()
                .id(wikiId)
                .title("Manchester United Wiki")
                .categories(categories)
                .build();

        // Mock 동작 정의
        when(wikiService.getWiki(wikiId)).thenReturn(mockResponse);

        // MockMvc로 테스트
        mockMvc.perform(get("/wiki/{wikiId}", wikiId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.title").value("Manchester United Wiki"))
                .andExpect(jsonPath("$.data.categories[0].name").value("History"))
                .andExpect(jsonPath("$.isSuccess").value(true))
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.message").value("성공"));

    }
}
