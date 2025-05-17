package neordinary.backend.nteam.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import neordinary.backend.nteam.entity.Diet;
import neordinary.backend.nteam.entity.enums.MealType;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DietResponseDto {
    private LocalDate date;
    private String name;
    private MealType mealType;
    
    @Schema(description = "Base64로 인코딩된 이미지 데이터", example = "data:image/jpeg;base64,/9j/4AAQSkZ...")
    private String image;
    
    private String ingredients;
    private String receipts;
    private String nutrients;
    
    public static DietResponseDto fromEntity(Diet diet) {
        return DietResponseDto.builder()
                .date(diet.getDate())
                .name(diet.getName())
                .mealType(diet.getMealType())
                .image(diet.getImage())
                .ingredients(diet.getIngredients())
                .receipts(diet.getReceipts())
                .nutrients(diet.getNutrients())
                .build();
    }
} 