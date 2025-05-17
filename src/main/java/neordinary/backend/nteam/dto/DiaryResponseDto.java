package neordinary.backend.nteam.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import neordinary.backend.nteam.entity.enums.MealType;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DiaryResponseDto {
    private Long id;
    private String image;
    private String ingredients;
    private String comment;
    private MealType mealType;
    private LocalDateTime createdAt;
} 