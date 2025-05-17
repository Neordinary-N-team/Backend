package neordinary.backend.nteam.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import neordinary.backend.nteam.dto.DietRequestDto;
import neordinary.backend.nteam.dto.DietResponseDto;
import neordinary.backend.nteam.entity.enums.MealType;
import neordinary.backend.nteam.service.DietService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class DietControllerTest {

    @Mock
    private DietService dietService;

    @InjectMocks
    private DietController dietController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private UUID memberId;
    private List<DietResponseDto> dietResponseDtoList;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(dietController).build();
        objectMapper = new ObjectMapper();
        memberId = UUID.randomUUID();

        dietResponseDtoList = new ArrayList<>();
        dietResponseDtoList.add(DietResponseDto.builder()
                .date(LocalDate.of(2024, 1, 15))
                .name("퀴노아 샐러드")
                .mealType(MealType.LUNCH)
                .image("image_data")
                .ingredients("퀴노아, 토마토, 오이, 올리브 오일")
                .receipts("1. 퀴노아를 삶습니다. 2. 채소를 썰어 퀴노아와 함께 담습니다.")
                .nutrients("단백질: 12g, 탄수화물: 35g, 지방: 15g")
                .build());
    }

    @Test
    @DisplayName("식단 생성 성공 테스트")
    void createDiet_Success() throws Exception {
        // given
        DietRequestDto requestDto = DietRequestDto.builder()
                .memberId(memberId)
                .build();

        // when, then
        mockMvc.perform(post("/api/diets")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("회원 ID 없이 식단 생성 실패 테스트")
    void createDiet_WithoutMemberId_ThrowsException() throws Exception {
        // given
        DietRequestDto requestDto = new DietRequestDto(null);

        // when, then
        mockMvc.perform(post("/api/diets")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("기간 내 식단 조회 성공 테스트")
    void getDietsByPeriod_Success() throws Exception {
        // given
        LocalDate startDate = LocalDate.of(2024, 1, 1);
        LocalDate endDate = LocalDate.of(2024, 1, 31);
        
        given(dietService.getDietsByPeriod(memberId, startDate, endDate)).willReturn(dietResponseDtoList);

        // when, then
        mockMvc.perform(get("/api/diets")
                .param("memberId", memberId.toString())
                .param("startDate", startDate.toString())
                .param("endDate", endDate.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("퀴노아 샐러드"))
                .andExpect(jsonPath("$[0].mealType").value("LUNCH"))
                .andExpect(jsonPath("$[0].ingredients").value("퀴노아, 토마토, 오이, 올리브 오일"));
    }

    @Test
    @DisplayName("시작일이 종료일보다 늦은 경우 예외 발생 테스트")
    void getDietsByPeriod_StartDateAfterEndDate_ThrowsException() throws Exception {
        // given
        LocalDate startDate = LocalDate.of(2024, 2, 1);
        LocalDate endDate = LocalDate.of(2024, 1, 31);
        
        given(dietService.getDietsByPeriod(memberId, startDate, endDate))
                .willThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST, "시작일이 종료일보다 늦을 수 없습니다."));

        // when, then
        mockMvc.perform(get("/api/diets")
                .param("memberId", memberId.toString())
                .param("startDate", startDate.toString())
                .param("endDate", endDate.toString()))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("조회 기간이 2개월 초과인 경우 예외 발생 테스트")
    void getDietsByPeriod_PeriodExceeding2Months_ThrowsException() throws Exception {
        // given
        LocalDate startDate = LocalDate.of(2024, 1, 1);
        LocalDate endDate = LocalDate.of(2024, 4, 1);
        
        given(dietService.getDietsByPeriod(memberId, startDate, endDate))
                .willThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST, "최대 2개월까지만 조회 가능합니다."));

        // when, then
        mockMvc.perform(get("/api/diets")
                .param("memberId", memberId.toString())
                .param("startDate", startDate.toString())
                .param("endDate", endDate.toString()))
                .andExpect(status().isBadRequest());
    }
} 