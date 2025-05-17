package neordinary.backend.nteam.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import neordinary.backend.nteam.entity.enums.MealType;

import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DiaryRequestDto {
    @Schema(description = "회원 ID", example = "123e4567-e89b-12d3-a456-426614174000")
    @NotNull
    private UUID memberId;
    
    @Schema(description = "식사 타입", example = "BREAKFAST")
    @NotNull
    private MealType type;
    
    @Schema(description = "재료 목록", example = "당근, 양파, 토마토, 두부, 아보카도")
    @NotNull
    private String ingredients;
} 