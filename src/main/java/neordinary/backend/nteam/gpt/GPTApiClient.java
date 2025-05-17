package neordinary.backend.nteam.gpt;

import neordinary.backend.nteam.entity.Diet;
import neordinary.backend.nteam.entity.Member;
import neordinary.backend.nteam.gpt.openai.meal_plan.GPTResponseMealPlanDto;
import neordinary.backend.nteam.gpt.openai.recipe.GPTResponseRecipeDto;

import java.util.List;

public interface GPTApiClient {
    // Generates a meal plan based on the member's preferences and dietary restrictions.
    List<GPTResponseMealPlanDto> generateMealPlan(Member member);

    GPTResponseRecipeDto generateRecipe(Member member, Diet diet);
}
