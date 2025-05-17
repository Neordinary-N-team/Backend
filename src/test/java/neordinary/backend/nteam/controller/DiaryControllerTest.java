package neordinary.backend.nteam.controller;

import neordinary.backend.nteam.dto.DiaryResponseDto;
import neordinary.backend.nteam.service.DiaryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class DiaryControllerTest {

    @Mock
    private DiaryService diaryService;

    @InjectMocks
    private DiaryController diaryController;

    private MockMvc mockMvc;
    private UUID memberId;
    private LocalDate date;
    private List<DiaryResponseDto> diaryResponseDtoList;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(diaryController).build();
        memberId = UUID.randomUUID();
        date = LocalDate.of(2024, 1, 15);

        diaryResponseDtoList = new ArrayList<>();
        diaryResponseDtoList.add(DiaryResponseDto.builder()
                .id(1L)
                .memberId(memberId)
                .image("image_data")
                .ingredients("당근, 양파, 토마토")
                .satisfiedComment("만족 코멘트")
                .dissatisfiedComment("불만족 코멘트")
                .createdAt(LocalDateTime.of(2024, 1, 15, 10, 0))
                .build());
    }

    @Test
    @DisplayName("일기 생성 성공 테스트")
    void createDiary_Success() throws Exception {
        // given
        MockMultipartFile image = new MockMultipartFile(
                "image", 
                "test.jpg", 
                MediaType.IMAGE_JPEG_VALUE, 
                "test image content".getBytes());

        // when, then
        mockMvc.perform(multipart("/api/diaries")
                .file(image)
                .param("memberId", memberId.toString())
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.memberId").value(memberId.toString()))
                .andExpect(jsonPath("$.image").exists())
                .andExpect(jsonPath("$.ingredients").exists());
    }

    @Test
    @DisplayName("기간 내 일기 조회 성공 테스트")
    void getDiariesByPeriod_Success() throws Exception {
        // given
        LocalDate startDate = LocalDate.of(2024, 1, 1);
        LocalDate endDate = LocalDate.of(2024, 1, 31);
        
        given(diaryService.getDiariesByPeriod(memberId, startDate, endDate)).willReturn(diaryResponseDtoList);

        // when, then
        mockMvc.perform(get("/api/diaries")
                .param("memberId", memberId.toString())
                .param("startDate", startDate.toString())
                .param("endDate", endDate.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].memberId").value(memberId.toString()))
                .andExpect(jsonPath("$[0].ingredients").value("당근, 양파, 토마토"));
    }

    @Test
    @DisplayName("시작일이 종료일보다 늦은 경우 예외 발생 테스트")
    void getDiariesByPeriod_StartDateAfterEndDate_ThrowsException() throws Exception {
        // given
        LocalDate startDate = LocalDate.of(2024, 2, 1);
        LocalDate endDate = LocalDate.of(2024, 1, 31);
        
        given(diaryService.getDiariesByPeriod(memberId, startDate, endDate))
                .willThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST, "시작일이 종료일보다 늦을 수 없습니다."));

        // when, then
        mockMvc.perform(get("/api/diaries")
                .param("memberId", memberId.toString())
                .param("startDate", startDate.toString())
                .param("endDate", endDate.toString()))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("조회 기간이 2개월 초과인 경우 예외 발생 테스트")
    void getDiariesByPeriod_PeriodExceeding2Months_ThrowsException() throws Exception {
        // given
        LocalDate startDate = LocalDate.of(2024, 1, 1);
        LocalDate endDate = LocalDate.of(2024, 4, 1);
        
        given(diaryService.getDiariesByPeriod(memberId, startDate, endDate))
                .willThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST, "최대 2개월까지만 조회 가능합니다."));

        // when, then
        mockMvc.perform(get("/api/diaries")
                .param("memberId", memberId.toString())
                .param("startDate", startDate.toString())
                .param("endDate", endDate.toString()))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("일기 삭제 성공 테스트")
    void deleteDiary_Success() throws Exception {
        // when, then
        mockMvc.perform(delete("/api/diaries")
                .param("memberId", memberId.toString())
                .param("date", date.toString()))
                .andExpect(status().isNoContent());
                
        verify(diaryService).deleteDiary(memberId, date);
    }

    @Test
    @DisplayName("존재하지 않는 일기 삭제 실패 테스트")
    void deleteDiary_DiaryNotFound_ThrowsException() throws Exception {
        // given
        doThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 날짜의 일기를 찾을 수 없습니다."))
                .when(diaryService).deleteDiary(memberId, date);

        // when, then
        mockMvc.perform(delete("/api/diaries")
                .param("memberId", memberId.toString())
                .param("date", date.toString()))
                .andExpect(status().isNotFound());
    }
} 