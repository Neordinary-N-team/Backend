package neordinary.backend.nteam.dto;

import lombok.Builder;
import lombok.Getter;
import neordinary.backend.nteam.entity.enums.MealType;

@Getter
@Builder
public class DiaryCommentResponseDto {
    private Long id;
    private MealType mealType;
    private String comment;
}
