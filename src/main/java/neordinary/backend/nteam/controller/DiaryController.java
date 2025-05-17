package neordinary.backend.nteam.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import neordinary.backend.nteam.dto.DiaryCommentResponseDto;
import neordinary.backend.nteam.dto.DiaryRequestDto;
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
            @RequestPart(value = "image", required = true) 
            @io.swagger.v3.oas.annotations.media.Schema(description = "식단 이미지")
            MultipartFile image,
            @RequestPart(value = "diaryRequest", required = true) 
            @io.swagger.v3.oas.annotations.media.Schema(description = "일기 정보 (회원ID, 식사타입, 재료목록)")
            @Valid DiaryRequestDto diaryRequest) {

        String contentType = image.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new DiaryHandler(ErrorStatus.BAD_IMAGE_FORMAT);
        }

        String imageData;
        try {
            imageData = convertImageToBase64(image);
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "이미지 처리 중 오류가 발생했습니다", e);
        }

        DiaryResponseDto response = diaryService.createDiary(diaryRequest, imageData);
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

    @Operation(summary = "선택된 날짜의 코멘트 조회", description = "회원 ID와 날짜로 해당 일의 아침/점심/저녁 코멘트를 조회합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "코멘트 조회 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    @GetMapping("/by-date")
    public ApiResponse<List<DiaryCommentResponseDto>> getDietByDate(
            @RequestParam @NotNull UUID memberId,
            @RequestParam @NotNull @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        List<DiaryCommentResponseDto> diarys = diaryService.getDiarysByDate(memberId, date);
        return ApiResponse.onSuccess(diarys);
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

    private String convertImageToBase64(MultipartFile image) throws IOException {
        String contentType = image.getContentType();
        byte[] imageBytes = image.getBytes();
        String base64Image = java.util.Base64.getEncoder().encodeToString(imageBytes);
        return "data:" + contentType + ";base64," + base64Image;
    }
} 
