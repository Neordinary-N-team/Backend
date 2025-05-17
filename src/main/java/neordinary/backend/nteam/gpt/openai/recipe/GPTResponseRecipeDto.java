package neordinary.backend.nteam.gpt.openai.recipe;

import lombok.Data;

import java.util.List;

@Data
public class GPTResponseRecipeDto {
    private List<String> instructions;

    public String getInstructions() {
        return String.join(", ", instructions);
    }
}
