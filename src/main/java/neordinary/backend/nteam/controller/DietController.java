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
    private static final int MAX_PERIOD_MONTHS = 2;
    private final DietService dietService;

    @Operation(summary = "비건 식단 생성", description = "비건 식단을 생성합니다. LLM이 자동으로 처리합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "비건 식단 생성 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    @PostMapping
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
    @GetMapping
    public ApiResponse<DietDetailsResponseDto> getDietDetails(
            @RequestParam @NotNull(message = "회원 ID는 필수 입력 값입니다.") Long dietId
    ) {
        DietDetailsResponseDto dietsDetails = dietService.getDietsDetails(dietId);
        return ApiResponse.onSuccess(dietsDetails);
    }
    
    private List<Diet> createMockDiets(LocalDate date) {
        List<Diet> mockDiets = new ArrayList<>();
        
        mockDiets.add(Diet.builder()
                .id(1L)
                .date(date)
                .name("애플 베리 오트밀")
                .mealType(MealType.BREAKFAST)
                .image("data:image/jpeg;base64,/9j/4AAQSkZJRgABAQEAYABgAAD/2wBDAAMCAgICAgMCAgIDAwMDBAYEBAQEBAgGBgUGCQgKCgkICQkKDA8MCgsOCwkJDRENDg8QEBEQCgwSExIQEw8QEBD/2wBDAQMDAwQDBAgEBAgQCwkLEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBD/wAARCAAyADIDASIAAhEBAxEB/8QAHwAAAQUBAQEBAQEAAAAAAAAAAAECAwQFBgcICQoL/8QAtRAAAgEDAwIEAwUFBAQAAAF9AQIDAAQRBRIhMUEGE1FhByJxFDKBkaEII0KxwRVS0fAkM2JyggkKFhcYGRolJicoKSo0NTY3ODk6Q0RFRkdISUpTVFVWV1hZWmNkZWZnaGlqc3R1dnd4eXqDhIWGh4iJipKTlJWWl5iZmqKjpKWmp6ipqrKztLW2t7i5usLDxMXGx8jJytLT1NXW19jZ2uHi4+Tl5ufo6erx8vP09fb3+Pn6/8QAHwEAAwEBAQEBAQEBAQAAAAAAAAECAwQFBgcICQoL")
                .ingredients("오트밀, 사과, 블루베리, 아몬드 밀크")
                .receipts("1. 오트밀에 아몬드 밀크를 넣고 끓입니다. 2. 사과와 블루베리를 올려 완성합니다.")
                .nutrients("단백질: 8g, 탄수화물: 45g, 지방: 5g")
                .build());
        
        mockDiets.add(Diet.builder()
                .id(2L)
                .date(date)
                .name("퀴노아 샐러드")
                .mealType(MealType.LUNCH)
                .image("data:image/jpeg;base64,/9j/4AAQSkZJRgABAQEAYABgAAD/2wBDAAMCAgICAgMCAgIDAwMDBAYEBAQEBAgGBgUGCQgKCgkICQkKDA8MCgsOCwkJDRENDg8QEBEQCgwSExIQEw8QEBD/2wBDAQMDAwQDBAgEBAgQCwkLEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBD/wAARCAAyADIDASIAAhEBAxEB/8QAHwAAAQUBAQEBAQEAAAAAAAAAAAECAwQFBgcICQoL/8QAtRAAAgEDAwIEAwUFBAQAAAF9AQIDAAQRBRIhMUEGE1FhByJxFDKBkaEII0KxwRVS0fAkM2JyggkKFhcYGRolJicoKSo0NTY3ODk6Q0RFRkdISUpTVFVWV1hZWmNkZWZnaGlqc3R1dnd4eXqDhIWGh4iJipKTlJWWl5iZmqKjpKWmp6ipqrKztLW2t7i5usLDxMXGx8jJytLT1NXW19jZ2uHi4+Tl5ufo6erx8vP09fb3+Pn6/8QAHwEAAwEBAQEBAQEBAQAAAAAAAAECAwQFBgcICQoL")
                .ingredients("퀴노아, 아보카도, 토마토, 오이, 올리브 오일")
                .receipts("1. 퀴노아를 삶습니다. 2. 채소를 썰어 퀴노아와 함께 담습니다. 3. 올리브 오일로 간을 합니다.")
                .nutrients("단백질: 12g, 탄수화물: 35g, 지방: 15g")
                .build());
        
        mockDiets.add(Diet.builder()
                .id(3L)
                .date(date)
                .name("버섯 리소토")
                .mealType(MealType.DINNER)
                .image("data:image/jpeg;base64,/9j/4AAQSkZJRgABAQEAYABgAAD/2wBDAAMCAgICAgMCAgIDAwMDBAYEBAQEBAgGBgUGCQgKCgkICQkKDA8MCgsOCwkJDRENDg8QEBEQCgwSExIQEw8QEBD/2wBDAQMDAwQDBAgEBAgQCwkLEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBD/wAARCAAyADIDASIAAhEBAxEB/8QAHwAAAQUBAQEBAQEAAAAAAAAAAAECAwQFBgcICQoL/8QAtRAAAgEDAwIEAwUFBAQAAAF9AQIDAAQRBRIhMUEGE1FhByJxFDKBkaEII0KxwRVS0fAkM2JyggkKFhcYGRolJicoKSo0NTY3ODk6Q0RFRkdISUpTVFVWV1hZWmNkZWZnaGlqc3R1dnd4eXqDhIWGh4iJipKTlJWWl5iZmqKjpKWmp6ipqrKztLW2t7i5usLDxMXGx8jJytLT1NXW19jZ2uHi4+Tl5ufo6erx8vP09fb3+Pn6/8QAHwEAAwEBAQEBAQEBAQAAAAAAAAECAwQFBgcICQoL")
                .ingredients("현미, 버섯, 양파, 식물성 치즈, 채소 스톡")
                .receipts("1. 양파를 볶습니다. 2. 현미를 넣고 볶다가 버섯을 넣습니다. 3. 채소 스톡을 넣고 끓입니다. 4. 식물성 치즈를 올려 완성합니다.")
                .nutrients("단백질: 10g, 탄수화물: 50g, 지방: 8g")
                .build());
        
        return mockDiets;
    }
} 