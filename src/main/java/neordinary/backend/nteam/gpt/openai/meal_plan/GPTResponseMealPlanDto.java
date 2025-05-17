package neordinary.backend.nteam.gpt.openai.meal_plan;


import lombok.Data;
import lombok.ToString;

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
}
