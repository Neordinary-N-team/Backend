package neordinary.backend.nteam.gpt;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import neordinary.backend.nteam.entity.Diary;
import neordinary.backend.nteam.entity.Diet;
import neordinary.backend.nteam.entity.Member;
import neordinary.backend.nteam.gpt.openai.ChatMessage;
import neordinary.backend.nteam.gpt.openai.GPTApiRequest;
import neordinary.backend.nteam.gpt.openai.GPTApiResponse;
import neordinary.backend.nteam.gpt.openai.diary.GPTResponseDiaryCommentDto;
import neordinary.backend.nteam.gpt.openai.meal_plan.GPTResponseMealPlanDto;
import neordinary.backend.nteam.gpt.openai.recipe.GPTResponseRecipeDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class GPTApiClientImpl implements GPTApiClient {

    @Value("${openai.api.key}")
    private String apiKey;

    @Value("${openai.api.url}")
    private String apiUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public List<GPTResponseMealPlanDto> generateMealPlan(Member member) {
        // 메시지 구성
        String customPrompt = String.format(PromptTemplate.MEAL_PLAN_TEMPLATE,
                member.getPregnancyWeek(),
                member.getHasMorningSickness(),
                member.getVeganLevel().getKoreanName(),
                member.getDiseases(),
                member.getBannedVegetables(),
                member.getVegProteins()
        );

        List<ChatMessage> messages = List.of(
                new ChatMessage("system", "너는 영양사이며, 비건 임산부를 위한 맞춤형 식단을 제공하는 역할이야."),
                new ChatMessage("user", customPrompt)
        );

        String json = askGPTAndGetResponseJson(messages);

        return convertJsonToDto(json);
    }

    @Override
    public GPTResponseRecipeDto generateRecipe(Member member, Diet diet) {
        // 메시지 구성
        String customPrompt = String.format(PromptTemplate.RECIPE_TEMPLATE,
                diet.getName(),
                member.getPregnancyWeek(),
                member.getHasMorningSickness(),
                member.getVeganLevel().getKoreanName(),
                member.getDiseases(),
                member.getBannedVegetables(),
                diet.getIngredients(),
                diet.getNutrients()
        );

        List<ChatMessage> messages = List.of(
                new ChatMessage("system", "너는 영양사이며, 비건 임산부를 위한 맞춤형 레시피를 제공하는 역할이야."),
                new ChatMessage("user", customPrompt)
        );

        String json = askGPTAndGetResponseJson(messages);

        return convertJsonToRecipeDto(json);
    }

    @Override
    public GPTResponseDiaryCommentDto generateDiaryComment(Diary diary) {
        // 메시지 구성
        String customPrompt = String.format(PromptTemplate.COMMENT_TEMPLATE,
                diary.getComment()
        );

        List<ChatMessage> messages = List.of(
                new ChatMessage("system", "너는 영양사이며, 비건 임산부를 위해 코멘트를 달아주는 역할이야."),
                new ChatMessage("user", customPrompt)
        );

        String json = askGPTAndGetResponseJson(messages);


        return convertJsonToCommentDto(json);
    }


    // == GPT 요청을 위한 Method ==

    private List<GPTResponseMealPlanDto> convertJsonToDto(String json) {
        try {
            // snake_case를 camelCase로 자동 변환
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);

            // JSON 배열을 List<GPTResponseMealPlanDto>로 변환
            return objectMapper.readValue(json, new TypeReference<>() {});
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("JSON 변환 실패");
        }
    }

    private GPTResponseRecipeDto convertJsonToRecipeDto(String json) {
        try {
            // snake_case를 camelCase로 자동 변환
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);

            // JSON 배열을 List<GPTResponseMealPlanDto>로 변환
            return objectMapper.readValue(json, new TypeReference<>() {});
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("JSON 변환 실패");
        }
    }

    private GPTResponseDiaryCommentDto convertJsonToCommentDto(String json) {
        try {
            // snake_case를 camelCase로 자동 변환
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);

            // JSON 배열을 List<GPTResponseMealPlanDto>로 변환
            return objectMapper.readValue(json, new TypeReference<>() {});
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("JSON 변환 실패");
        }
    }

    private String askGPTAndGetResponseJson(List<ChatMessage> messages) {
        // 요청 객체
        GPTApiRequest request = new GPTApiRequest("gpt-3.5-turbo-1106", messages, 0.7);

        // 헤더
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        HttpEntity<GPTApiRequest> entity = new HttpEntity<>(request, headers);

        // 요청 전송
        ResponseEntity<GPTApiResponse> response = restTemplate.postForEntity(
                apiUrl, entity, GPTApiResponse.class
        );

        // 응답 추출
        if (response.getStatusCode() == HttpStatus.OK) {
            GPTApiResponse gptResponse = response.getBody();
            if (gptResponse != null && !gptResponse.getChoices().isEmpty()) {
                String apiResponse = gptResponse.getChoices().get(0).getMessage().getContent();
                return JsonExtractor.extractJson(apiResponse);
            }
        }

        throw new RuntimeException("API 요청 실패");
    }
}

