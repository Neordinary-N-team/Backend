package neordinary.backend.nteam.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import neordinary.backend.nteam.entity.Diet;
import neordinary.backend.nteam.entity.enums.DietDifficulty;
import neordinary.backend.nteam.entity.enums.MealType;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DietDetailsResponseDto {
    private Long id;
    private String name;
    private String nutrients;
    private String receipts;
    private String ingredients;
    private MealType mealType;

    @Schema(description = "Base64로 인코딩된 이미지 데이터", example = "data:image/jpeg;base64,/9j/4AAQSkZ...")
    private String image;

    private int calories;
    private DietDifficulty difficulty;

    public static DietDetailsResponseDto fromEntity(Diet diet) {
        return DietDetailsResponseDto.builder()
                .id(diet.getId())
                .name(diet.getName())
                .nutrients(diet.getNutrients())
                .receipts(diet.getReceipts())
                .ingredients(diet.getIngredients())
                .mealType(diet.getMealType())
                .image(diet.getImage())
                .calories(diet.getCalories())
                .difficulty(diet.getDifficulty())
                .build();
    }
} 