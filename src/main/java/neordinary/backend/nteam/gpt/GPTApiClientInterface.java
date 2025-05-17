package neordinary.backend.nteam.gpt;

import neordinary.backend.nteam.entity.Member;
import neordinary.backend.nteam.gpt.openai.GPTResponseMealPlanDto;

import java.util.List;

public interface GPTApiClientInterface {
    // Generates a meal plan based on the member's preferences and dietary restrictions.
    List<GPTResponseMealPlanDto> generateMealPlan(Member member);
}
