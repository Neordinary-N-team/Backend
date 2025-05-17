package neordinary.backend.nteam.gpt.openai;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ChatMessage {
    private String role;
    private String content;
}
