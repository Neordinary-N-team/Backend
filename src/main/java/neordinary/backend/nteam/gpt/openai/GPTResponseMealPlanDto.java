package neordinary.backend.nteam.gpt.openai;


import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@ToString
public class GPTResponseMealPlanDto {
    private String day;
    private String mealTime;
    private String mealName;
    private List<GPTResponseIngredientDto> ingredients;
    private List<GPTResponseNutrientDto> nutrients;
    private int calories;
}
