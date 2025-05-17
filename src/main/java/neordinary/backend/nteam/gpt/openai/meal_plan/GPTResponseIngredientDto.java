package neordinary.backend.nteam.gpt.openai.meal_plan;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class GPTResponseIngredientDto {
    private String name;
    private String alternative;

    public String toDtoString() {
        return name + " (alternative ingredient) " + alternative;
    }
}
