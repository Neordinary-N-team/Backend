package neordinary.backend.nteam.gpt;

public class PromptTemplate {
    public static final String MEAL_PLAN_TEMPLATE = """
            당신은 비건 전문 영양사입니다. 다음 정보를 기반으로 임산부를 위한 7일간의 주간 식단을 JSON 배열 형태로 설계해주세요.
            
            정보:
            - 임신 주차: %s주차
            - 입덧 여부: %s
            - 비건 단계: %s
            - 기저 질환: %s
            - 알러지 정보: %s
            - 선호 식재료: %s
            
            요청 사항:
            - 하루 3끼(아침, 점심, 저녁), 총 7일치 식단을 구성해주세요. 총 21개의 식단을 모두 포함해주세요.
            - nutrients로 포함 가능한 성분은 다음과 같습니다. 대표 영양소 4개를 선택해 단위 중량과 함께 표기해주세요. : 단백질, 철분, 비타민 C, 엽산 (비타민 B9), 비타민 B12, 칼슘, 비타민 D, 오메가-3 지방산, 아연, 요오드, 비타민 B2, 탄수화물, 비타민 B6, 마그네슘, 나트륨
            - 각 식사는 아래와 같은 JSON 객체 형식으로 제공되어야 합니다. 다른 설명 문장, 주석, 또는 "..." 등의 생략 표현 없이 순수 JSON 배열만 출력해주세요.
            
            
            ```json
            [
              {
                "day": "월요일",
                "meal_time": "아침",
                "meal_name": "철분 강화 두부 스크램블",
                "ingredients": [
                  {"name": "두부", "alternative": "병아리콩"},
                  {"name": "시금치", "alternative": "케일"},
                  {"name": "강화 시리얼", "alternative": "통밀빵"}
                ],
                "nutrients": [
                  {"name": "protein", "amount": "20g"},
                  {"name": "iron", "amount": "3mg"},
                  {"name": "vitaminC", "amount": "30mg"},
                  {"name": "folate", "amount": "200mcg"}
                ],
                "calories": 320
              },
              ...
            ]
            주의사항:
            
            하루 3끼 × 7일 = 21개의 JSON 객체가 반드시 포함되어야 합니다.
            
            출력 형식은 반드시 JSON 배열입니다. JSON 외의 텍스트는 절대 포함하지 마세요.
            
            각 요리는 요청자 정보에 맞게 조절되어야 하며, 알러지와 기저 질환에 유의하여 식단으로 구성해주세요.
            
            하루당 30분 이내로 조리 가능하도록 구성해주세요.
            
            ---
            
            """;
}
