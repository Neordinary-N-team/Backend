package neordinary.backend.nteam.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import neordinary.backend.nteam.dto.DiaryResponseDto;
import neordinary.backend.nteam.global.apiPayload.ApiResponse;
import neordinary.backend.nteam.global.exception.handler.DiaryHandler;
import neordinary.backend.nteam.global.apiPayload.code.status.ErrorStatus;
import neordinary.backend.nteam.service.DiaryService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/diaries")
@RequiredArgsConstructor
@Validated
@Tag(name = "Diary API", description = "일기 관련 API")
public class DiaryController {
    private static final int MAX_PERIOD_MONTHS = 2;
    private final DiaryService diaryService;

    @Operation(summary = "일기 생성")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "일기 생성 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<DiaryResponseDto> createDiary(
            @RequestPart(value = "image") MultipartFile image,
            @RequestParam(value = "memberId") @NotNull UUID memberId) {

        String contentType = image.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new DiaryHandler(ErrorStatus.BAD_IMAGE_FORMAT);
        }

        String mockImageBase64 = "data:image/jpeg;base64,/9j/4AAQSkZJRgABAQEAYABgAAD/2wBDAAMCAgICAgMCAgIDAwMDBAYEBAQEBAgGBgUGCQgKCgkICQkKDA8MCgsOCwkJDRENDg8QEBEQCgwSExIQEw8QEBD/2wBDAQMDAwQDBAgEBAgQCwkLEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBD/wAARCAAyADIDASIAAhEBAxEB/8QAHwAAAQUBAQEBAQEAAAAAAAAAAAECAwQFBgcICQoL/8QAtRAAAgEDAwIEAwUFBAQAAAF9AQIDAAQRBRIhMUEGE1FhByJxFDKBkaEII0KxwRVS0fAkM2JyggkKFhcYGRolJicoKSo0NTY3ODk6Q0RFRkdISUpTVFVWV1hZWmNkZWZnaGlqc3R1dnd4eXqDhIWGh4iJipKTlJWWl5iZmqKjpKWmp6ipqrKztLW2t7i5usLDxMXGx8jJytLT1NXW19jZ2uHi4+Tl5ufo6erx8vP09fb3+Pn6/8QAHwEAAwEBAQEBAQEBAQAAAAAAAAECAwQFBgcICQoL";

        DiaryResponseDto response = DiaryResponseDto.builder()
                .id(1L)
                .memberId(memberId)
                .image(mockImageBase64)
                .ingredients("당근, 양파, 토마토, 두부, 아보카도, 견과류")
                .satisfiedComment("두부와 견과류에서 양질의 단백질, 아보카도에서 불포화 지방산 충분히 섭취했습니다.")
                .dissatisfiedComment("철분과 칼슘이 부족하니 녹색 채소와 두유를 추가하세요.")
                .createdAt(LocalDateTime.now())
                .build();

        return ApiResponse.onSuccess(response);
    }
    
    @GetMapping
    @Operation(summary = "기간별 일기 조회", description = "지정된 기간 내의 모든 일기를 조회합니다. 최대 2개월까지 조회 가능합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "일기 조회 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    public ResponseEntity<List<DiaryResponseDto>> getDiariesByPeriod(
            @RequestParam @NotNull(message = "회원 ID는 필수 입력 값입니다.") UUID memberId,
            @RequestParam @NotNull(message = "시작일은 필수 입력 값입니다.") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @NotNull(message = "종료일은 필수 입력 값입니다.") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        List<DiaryResponseDto> responseDtos = diaryService.getDiariesByPeriod(memberId, startDate, endDate);
        
        return ResponseEntity.ok(responseDtos);
    }
    
    @DeleteMapping
    @Operation(summary = "일기 삭제", description = "특정 회원의 특정 날짜 일기를 삭제합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "204", description = "일기 삭제 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "일기를 찾을 수 없음")
    })
    public ResponseEntity<Void> deleteDiary(
            @RequestParam @NotNull(message = "회원 ID는 필수 입력 값입니다.") UUID memberId,
            @RequestParam @NotNull(message = "날짜는 필수 입력 값입니다.") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        if (memberId == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "회원 ID는 필수 입력 값입니다.");
        }
        
        if (date == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "날짜는 필수 입력 값입니다.");
        }
        
        diaryService.deleteDiary(memberId, date);
        
        return ResponseEntity.noContent().build();
    }
    
    private DiaryResponseDto createMockDiaryResponse(LocalDate date, UUID memberId) {
        return DiaryResponseDto.builder()
                .id(1L)
                .memberId(memberId)
                .image("data:image/jpeg;base64,/9j/4AAQSkZJRgABAQEAYABgAAD/2wBDAAMCAgICAgMCAgIDAwMDBAYEBAQEBAgGBgUGCQgKCgkICQkKDA8MCgsOCwkJDRENDg8QEBEQCgwSExIQEw8QEBD/2wBDAQMDAwQDBAgEBAgQCwkLEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBD/wAARCAAyADIDASIAAhEBAxEB/8QAHwAAAQUBAQEBAQEAAAAAAAAAAAECAwQFBgcICQoL/8QAtRAAAgEDAwIEAwUFBAQAAAF9AQIDAAQRBRIHMUEGE1FhByJxFDKBkaEII0KxwRVS0fAkM2JyggkKFhcYGRolJicoKSo0NTY3ODk6Q0RFRkdISUpTVFVWV1hZWmNkZWZnaGlqc3R1dnd4eXqDhIWGh4iJipKTlJWWl5iZmqKjpKWmp6ipqrKztLW2t7i5usLDxMXGx8jJytLT1NXW19jZ2uHi4+Tl5ufo6erx8vP09fb3+Pn6/8QAHwEAAwEBAQEBAQEBAQAAAAAAAAECAwQFBgcICQoL")
                .ingredients("당근, 양파, 토마토, 두부, 아보카도, 견과류")
                .satisfiedComment("두부와 견과류에서 양질의 단백질, 아보카도에서 불포화 지방산 충분히 섭취했습니다.")
                .dissatisfiedComment("철분과 칼슘이 부족하니 녹색 채소와 두유를 추가하세요.")
                .createdAt(LocalDateTime.of(date.getYear(), date.getMonth(), date.getDayOfMonth(), 20, 30))
                .build();
    }

    private String saveImage(MultipartFile image) {
        try {
            String originalFilename = image.getOriginalFilename();
            String extension = originalFilename != null ? originalFilename.substring(originalFilename.lastIndexOf(".")) : ".jpg";
            String filename = UUID.randomUUID() + extension;
            
            Path uploadPath = Path.of("uploads");
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            
            Path filePath = uploadPath.resolve(filename);
            Files.copy(image.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            
            return "/uploads/" + filename;
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "이미지 저장에 실패했습니다", e);
        }
    }
} 