package neordinary.backend.nteam.gpt.openai;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GPTApiRequest {
    private String model;
    private List<ChatMessage> messages;
    private double temperature;
}