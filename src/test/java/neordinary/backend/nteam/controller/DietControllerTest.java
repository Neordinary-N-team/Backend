package neordinary.backend.nteam.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import neordinary.backend.nteam.dto.DietRequestDto;
import neordinary.backend.nteam.dto.DietResponseDto;
import neordinary.backend.nteam.entity.enums.MealType;
import neordinary.backend.nteam.global.apiPayload.code.status.ErrorStatus;
import neordinary.backend.nteam.global.exception.handler.DietHandler;
import neordinary.backend.nteam.service.DietService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

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
                .id(1L)
                .date(LocalDate.of(2024, 1, 15))
                .name("퀴노아 샐러드")
                .mealType(MealType.LUNCH)
                .image("image_data")
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
    @DisplayName("날짜별 식단 조회 성공 테스트")
    void getDietsByDate_Success() throws Exception {
        // given
        LocalDate date = LocalDate.of(2024, 1, 15);
        
        given(dietService.getDietsByDate(memberId, date)).willReturn(dietResponseDtoList);

        // when, then
        mockMvc.perform(get("/api/diets")
                .param("memberId", memberId.toString())
                .param("date", date.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result[0].name").value("퀴노아 샐러드"))
                .andExpect(jsonPath("$.result[0].mealType").value("LUNCH"));
    }
} 