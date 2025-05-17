package neordinary.backend.nteam.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
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
@Tag(name = "Diary API", description = "사용자의 일일 식단을 효과적으로 관리하고, 각 식단에 대한 유용한 피드백을 받을 수 있도록 지원하는 Diary API 입니다.")
public class DiaryController {
    private final DiaryService diaryService;

    @Operation(
            summary = "일기(식단 기록) 생성 API",
            description = """
                    사용자가 섭취한 식단에 대한 새로운 일기(기록)를 생성합니다.
                    """
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "일기 생성 성공. 생성된 일기의 상세 정보 (`DiaryResponseDto`)를 반환합니다.",
                    content = @Content(schema = @Schema(implementation = DiaryResponseDto.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = """
                    잘못된 요청입니다. 요청 파라미터를 확인해주세요. 오류 발생 가능한 경우:
                    - `image` 파트가 누락되었거나, 파일이 유효한 이미지 형식이 아닌 경우 (예: 'image/jpeg', 'image/png'가 아닌 경우).
                    - `diaryRequest` 파트가 누락되었거나, JSON 형식이 아닌 경우.
                    - `diaryRequest` 내부 필드 유효성 검사 실패:
                        - `memberId`가 누락되었거나 유효한 UUID 형식이 아닌 경우.
                        - `mealType`이 누락되었거나 허용된 값(BREAKFAST, LUNCH, DINNER)이 아닌 경우.
                        - `ingredients`가 누락되었거나 빈 목록인 경우.""",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버 내부 오류입니다.",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<DiaryResponseDto> createDiary(
            @Parameter(name = "image", description = "업로드할 식단 이미지 파일 (필수). `multipart/form-data` 형식으로 전송해야 합니다. 지원되는 이미지 형식: JPEG, PNG 등.", required = true,
                    content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE))
            @RequestPart(value = "image", required = true)
            MultipartFile image,
            @Parameter(name = "diaryRequest", description = """
                    일기 생성에 필요한 정보 (JSON 형식, 필수). `multipart/form-data` 형식으로 전송해야 합니다.
                    **필수 필드:**
                    - `memberId` (UUID): 사용자 고유 ID. (예: "33510094-ff12-46ff-907c-8689f184d36d")
                    - `mealType` (String): 식사 타입. (예: "BREAKFAST", "LUNCH", "DINNER", "SNACK")
                    - `ingredients` (List<String>): 식단 재료 목록. (예: ["사과 1개", "고구마 100g"])""", required = true,
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = DiaryRequestDto.class)))
            @RequestPart(value = "diaryRequest", required = true)
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

    @Operation(
            summary = "특정 날짜의 일기(식단 기록) 목록 조회 API",
            description = """
                    지정된 사용자의 특정 날짜에 해당하는 모든 일기(식단 기록) 목록을 조회합니다.
                    사용자가 하루 동안 기록한 아침, 점심, 저녁, 간식 등의 모든 식단 기록을 시간 순서 또는 다른 정렬 기준에 따라 받아볼 수 있습니다.
                    """
    )
    @Parameters({
            @Parameter(name = "memberId", description = "조회할 사용자의 ID (UUID 형식, 필수). 해당 ID의 사용자가 존재해야 합니다.", required = true, example = "33510094-ff12-46ff-907c-8689f184d36d"),
            @Parameter(name = "date", description = "조회할 날짜 (YYYY-MM-DD 형식, 필수). (예: '2024-07-26')", required = true, example = "2024-07-26")
    })
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "일기 목록 조회 성공. 해당 날짜의 일기 목록 (`List<DiaryResponseDto>`)을 반환합니다. 기록이 없는 경우 빈 리스트가 반환됩니다.",
                    content = @Content(schema = @Schema(implementation = DiaryResponseDto.class))), // 스키마는 단일 객체지만, 실제로는 리스트로 나감
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = """
                    잘못된 요청입니다. 요청 파라미터를 확인해주세요. 오류 발생 가능한 경우:
                    - `memberId`가 누락되었거나 유효한 UUID 형식이 아닌 경우.
                    - `date`가 누락되었거나 'YYYY-MM-DD' 형식이 아닌 경우.""",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    @GetMapping("/daily")
    public ApiResponse<List<DiaryResponseDto>> getDiariesByDate(
            @RequestParam @NotNull(message = "회원 ID는 필수 입력 값입니다.") UUID memberId,
            @RequestParam @NotNull(message = "날짜는 필수 입력 값입니다.") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        List<DiaryResponseDto> responseDtos = diaryService.getDiariesByDate(memberId, date);
        return ApiResponse.onSuccess(responseDtos);
    }
    @Operation(
            summary = "특정 날짜의 식단 코멘트(피드백) 목록 조회 API",
            description = """
                    지정된 사용자의 특정 날짜에 기록된 각 식단(예: 아침, 점심, 저녁)에 대한 AI 기반 코멘트 또는 전문가 피드백 목록을 조회합니다.
                    """
    )
    @Parameters({
            @Parameter(name = "memberId", description = "코멘트를 조회할 사용자의 ID (UUID 형식, 필수).", required = true, example = "33510094-ff12-46ff-907c-8689f184d36d"),
            @Parameter(name = "date", description = "코멘트를 조회할 날짜 (YYYY-MM-DD 형식, 필수). (예: '2024-07-26')", required = true, example = "2024-07-26")
    })
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "코멘트 조회 성공. 해당 날짜의 식단 코멘트 목록 (`List<DiaryCommentResponseDto>`)을 반환합니다. 코멘트가 없는 경우 빈 리스트가 반환됩니다.",
                    content = @Content(schema = @Schema(implementation = DiaryCommentResponseDto.class))), // 스키마는 단일 객체지만, 실제로는 리스트로 나감
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = """
                    잘못된 요청입니다. 요청 파라미터를 확인해주세요. 오류 발생 가능한 경우:
                    - `memberId`가 누락되었거나 유효한 UUID 형식이 아닌 경우.
                    - `date`가 누락되었거나 'YYYY-MM-DD' 형식이 아닌 경우.""",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    @GetMapping("/by-date")
    public ApiResponse<List<DiaryCommentResponseDto>> getDietByDate(
            @RequestParam @NotNull UUID memberId,
            @RequestParam @NotNull @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        List<DiaryCommentResponseDto> diarys = diaryService.getDiarysByDate(memberId, date);
        return ApiResponse.onSuccess(diarys);
    }

    private String convertImageToBase64(MultipartFile image) throws IOException {
        String contentType = image.getContentType();
        byte[] imageBytes = image.getBytes();
        String base64Image = java.util.Base64.getEncoder().encodeToString(imageBytes);
        return "data:" + contentType + ";base64," + base64Image;
    }
} 
