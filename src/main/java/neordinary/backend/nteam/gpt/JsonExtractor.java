package neordinary.backend.nteam.gpt;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonExtractor {
    public static String extractJson(String response) {
        try {
            // 백틱 제거 및 JSON 문자열 정리
            String cleanedResponse = response
                    .replaceAll("(?s)```json", "") // 시작 백틱 제거
                    .replaceAll("(?s)```", "")    // 종료 백틱 제거
                    .trim();

            // JSON 유효성 검사
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.readTree(cleanedResponse); // 유효하지 않으면 예외 발생

            return cleanedResponse; // 정리된 JSON 반환
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("JSON 추출 실패: " + e.getMessage());
        }
    }

//    public static void main(String[] args) {
//        String response = """
//            ```json
//            [
//              {
//                "day": "월요일",
//                "meal_time": "아침",
//                "meal_name": "철분 강화 두부 스크램블",
//                "ingredients": [
//                  {"name": "두부", "alternative": "병아리콩"},
//                  {"name": "시금치", "alternative": "케일"},
//                  {"name": "강화 시리얼", "alternative": "통밀빵"}
//                ],
//                "nutrients": [
//                  {"name": "protein", "amount": "20g"},
//                  {"name": "iron", "amount": "3mg"},
//                  {"name": "vitaminC", "amount": "30mg"},
//                  {"name": "folate", "amount": "200mcg"}
//                ],
//                "calories": 320
//              }
//            ]
//            ```
//            """;
//
//        String jsonContent = extractJson(response);
//        System.out.println(jsonContent);
//    }
}
