package neordinary.backend.nteam.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import neordinary.backend.nteam.dto.DiaryRequestDto;
import neordinary.backend.nteam.dto.DiaryResponseDto;
import neordinary.backend.nteam.entity.enums.MealType;
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
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(diaryController).build();
        memberId = UUID.randomUUID();
        date = LocalDate.of(2024, 1, 15);
        objectMapper = new ObjectMapper();

        diaryResponseDtoList = new ArrayList<>();
        diaryResponseDtoList.add(DiaryResponseDto.builder()
                .id(1L)
                .image("image_data")
                .ingredients("당근, 양파, 토마토")
                .comment("코멘트 내용")
                .createdAt(LocalDateTime.of(2024, 1, 15, 10, 0))
                .build());
    }

    @Test
    @DisplayName("일기 생성 성공 테스트")
    void createDiary_Success() throws Exception {
        // given
        DiaryRequestDto diaryRequest = DiaryRequestDto.builder()
                .memberId(memberId)
                .type(MealType.BREAKFAST)
                .ingredients("당근, 양파, 토마토")
                .build();
        
        DiaryResponseDto responseDto = DiaryResponseDto.builder()
                .id(1L)
                .image("image_data")
                .ingredients("당근, 양파, 토마토")
                .comment("코멘트 내용")
                .build();
                
        given(diaryService.createDiary(any(DiaryRequestDto.class), any(String.class)))
                .willReturn(responseDto);
        
        MockMultipartFile image = new MockMultipartFile(
                "image", 
                "test.jpg", 
                MediaType.IMAGE_JPEG_VALUE, 
                "test image content".getBytes());
                
        MockMultipartFile diaryRequestFile = new MockMultipartFile(
                "diaryRequest",
                "",
                MediaType.APPLICATION_JSON_VALUE,
                objectMapper.writeValueAsString(diaryRequest).getBytes());

        // when, then
        mockMvc.perform(multipart("/api/diaries")
                .file(image)
                .file(diaryRequestFile)
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.result.id").exists())
                .andExpect(jsonPath("$.result.image").exists())
                .andExpect(jsonPath("$.result.ingredients").exists());
    }

    @Test
    @DisplayName("일기 조회 성공 테스트")
    void getDiaries_Success() throws Exception {
        // given
        LocalDate targetDate = LocalDate.of(2024, 1, 15);
        
        given(diaryService.getDiariesByDate(any(UUID.class), any(LocalDate.class)))
                .willReturn(diaryResponseDtoList);

        // when, then
        mockMvc.perform(get("/api/diaries/daily")
                .param("memberId", memberId.toString())
                .param("date", targetDate.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result[0].id").value(1))
                .andExpect(jsonPath("$.result[0].ingredients").value("당근, 양파, 토마토"));
    }

    @Test
    @DisplayName("유효하지 않은 날짜 조회시 예외 발생 테스트")
    void getDiaries_InvalidDate_ThrowsException() throws Exception {
        // given
        LocalDate invalidDate = LocalDate.of(2024, 2, 1);
        
        given(diaryService.getDiariesByDate(any(UUID.class), any(LocalDate.class)))
                .willThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST, "유효하지 않은 날짜입니다."));

        // when, then
        mockMvc.perform(get("/api/diaries/daily")
                .param("memberId", memberId.toString())
                .param("date", invalidDate.toString()))
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