package neordinary.backend.nteam.gpt.openai.meal_plan;


import lombok.Data;
import lombok.ToString;
import neordinary.backend.nteam.entity.Diet;
import neordinary.backend.nteam.entity.enums.DietDifficulty;
import neordinary.backend.nteam.entity.enums.MealType;
import neordinary.backend.nteam.utils.WeekDateCalculator;

import java.time.LocalDate;
import java.util.List;

@Data
@ToString
public class GPTResponseMealPlanDto {
    private String day;
    private String mealTime;
    private String mealName;
    private String difficulty;
    private List<GPTResponseIngredientDto> ingredients;
    private List<GPTResponseNutrientDto> nutrients;
    private int calories;

    public Diet toEntity() {
        LocalDate date = WeekDateCalculator.calculateDateOfDayOfWeek(day);
        String ingredientString = "";
        for (GPTResponseIngredientDto ingredient : ingredients) {
            ingredientString += ingredient.toDtoString() + ", ";
        }

        String nutrientString = "";
        for (GPTResponseNutrientDto nutrient : nutrients) {
            nutrientString += nutrient.toDtoString() + ", ";
        }

        return Diet.builder()
                .date(date)
                .name(mealName)
                .mealType(MealType.valueOf(mealTime))
                .ingredients(ingredientString)
                .nutrients(nutrientString)
                .calories(calories)
                .difficulty(DietDifficulty.valueOf(difficulty))
                .build();
    }
}
