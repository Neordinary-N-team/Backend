package neordinary.backend.nteam.gpt.openai;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class GPTResponseNutrientDto {
    private String name;
    private String amount;
}
