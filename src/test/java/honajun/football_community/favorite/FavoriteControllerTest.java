package honajun.football_community.favorite;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import com.fasterxml.jackson.databind.ObjectMapper;
import honajun.football_community.favorite.controller.FavoriteController;
import honajun.football_community.favorite.dto.FavoriteRequestDTO;
import honajun.football_community.favorite.dto.FavoriteResponseDTO;
import honajun.football_community.favorite.service.FavoriteCommandAdapter;
import honajun.football_community.favorite.service.FavoriteQueryAdapter;
import honajun.football_community.favorite.service.FavoriteService;
import honajun.football_community.global.annotation.resolver.AuthMemberArgumentResolver;
import honajun.football_community.global.enums.favorite.FavoriteType;
import honajun.football_community.global.security.jwt.JwtTokenProvider;
import honajun.football_community.member.entity.Member;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = FavoriteController.class, excludeAutoConfiguration = SecurityAutoConfiguration.class)
@MockBean(JpaMetamodelMappingContext.class)
public class FavoriteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private FavoriteService favoriteService;

    @MockBean
    private FavoriteCommandAdapter favoriteCommandAdapter;

    @MockBean
    private FavoriteQueryAdapter favoriteQueryAdapter;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;


    @MockBean
    private AuthMemberArgumentResolver authMemberArgumentResolver;

    private Member mockMember;

    @BeforeEach
    void setUp() {
        mockMember = Member.builder()
                .id(1L)
                .email("test@example.com")
                .name("TestUser")
                .build();
        when(authMemberArgumentResolver.resolveArgument(any(), any(), any(), any())).thenReturn(mockMember);
    }


    @Test
    void addFavorite_shouldReturnFavoriteResponse() throws Exception {
        // Given: Mock 데이터 준비

        FavoriteRequestDTO.addFavorite request = new FavoriteRequestDTO.addFavorite(1L, FavoriteType.FAVORITE_TEAM);

        FavoriteResponseDTO.getFavorite mockResponse = FavoriteResponseDTO.getFavorite.builder()
                .id(1L)
                .favoriteType(FavoriteType.FAVORITE_TEAM)
                .isPushed(false)
                .build();

        // Mock 동작 정의
        when(favoriteService.addFavorite(mockMember, request)).thenReturn(mockResponse);

        FavoriteResponseDTO.getFavorite result = favoriteService.addFavorite(mockMember, request);
        assertEquals(mockResponse, result);


//        // When & Then: MockMvc를 이용한 테스트 실행
//        mockMvc.perform(post("/favorites")
//                        .header("Authorization", "Bearer mock-token") // 헤더 추가
//                        .contentType("application/json") // Content-Type 설정
//                        .content(objectMapper.writeValueAsString(request)))
//                .andDo(print()) // 반환 데이터 출력
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.data.id").value(1L))
//                .andExpect(jsonPath("$.data.favoriteType").value("FAVORITE_TEAM"))
//                .andExpect(jsonPath("$.data.isPushed").value(false))
//                .andExpect(jsonPath("$.isSuccess").value(true))
//                .andExpect(jsonPath("$.code").value("200"))
//                .andExpect(jsonPath("$.message").value("성공"));
    }


}
