package neordinary.backend.nteam.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import neordinary.backend.nteam.dto.DietDetailsResponseDto;
import neordinary.backend.nteam.dto.DietRequestDto;
import neordinary.backend.nteam.dto.DietResponseDto;
import neordinary.backend.nteam.entity.Diet;
import neordinary.backend.nteam.entity.enums.MealType;
import neordinary.backend.nteam.global.apiPayload.ApiResponse;
import neordinary.backend.nteam.service.DietService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/diets")
@RequiredArgsConstructor
@Validated
@Tag(name = "Diet API", description = "비건 식단 관련 API")
public class DietController {
    private final DietService dietService;

    @Operation(summary = "비건 식단 생성", description = "비건 식단을 생성합니다. LLM이 자동으로 처리합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "비건 식단 생성 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<?> createDiet(@Valid @RequestBody DietRequestDto requestDto) {
        UUID memberId = dietService.createDiet(requestDto.getMemberId());

        return ApiResponse.onSuccess(memberId);
    }

    @Operation(summary = "날짜 기준 비건 식단 조회", description = "지정된 날짜의 모든 비건 식단을 조회합니다. 이미지는 Base64 인코딩 형식입니다. 아침, 점심, 저녁의 식단 정보를 반환합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "비건 식단 조회 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    @GetMapping
    public ApiResponse<List<DietResponseDto>> getDietsByPeriod(
            @RequestParam @NotNull(message = "회원 ID는 필수 입력 값입니다.") UUID memberId,
            @RequestParam @NotNull(message = "날짜는 필수 입력 값입니다.") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        List<DietResponseDto> responseDtos = dietService.getDietsByDate(memberId, date);
        return ApiResponse.onSuccess(responseDtos);
    }

    @Operation(summary = "비건 식단 상세 조회", description = "지정된 식단의 상세 정보를 반환합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "비건 식단 조회 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    @GetMapping("/details")
    public ApiResponse<DietDetailsResponseDto> getDietDetails(
            @RequestParam @NotNull(message = "회원 ID는 필수 입력 값입니다.") UUID memberId,
            @RequestParam @NotNull(message = "식단 ID는 필수 입력 값입니다.") Long dietId
    ) {
        DietDetailsResponseDto dietsDetails = dietService.getDietsDetails(memberId, dietId);
        return ApiResponse.onSuccess(dietsDetails);
    }
} 