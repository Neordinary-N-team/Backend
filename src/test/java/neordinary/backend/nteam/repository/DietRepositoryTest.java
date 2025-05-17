package neordinary.backend.nteam.repository;

import neordinary.backend.nteam.config.TestConfig;
import neordinary.backend.nteam.entity.Diet;
import neordinary.backend.nteam.entity.Member;
import neordinary.backend.nteam.entity.enums.MealType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(TestConfig.class)
@ActiveProfiles("test")
class DietRepositoryTest {

    @Autowired
    private DietRepository dietRepository;
    
    @Autowired
    private MemberRepository memberRepository;
    
    private Member member;

    @BeforeEach
    void setUp() {
        // 테스트 전에 회원 생성
        member = Member.builder()
                .pregDate(LocalDate.of(2024, 5, 17))
                .height(160)
                .weight(60)
                .allowedVeganFoods(Collections.singletonList("과일,채소"))
                .memberLevel(1)
                .build();
        member = memberRepository.save(member);
    }

    @Test
    @DisplayName("식단 저장 테스트")
    void save_Diet_Success() {
        // given
        Diet diet = Diet.builder()
                .member(member)
                .date(LocalDate.of(2024, 1, 15))
                .name("퀴노아 샐러드")
                .mealType(MealType.LUNCH)
                .image("image_data")
                .ingredients("퀴노아, 토마토, 오이, 올리브 오일")
                .recipe("1. 퀴노아를 삶습니다. 2. 채소를 썰어 퀴노아와 함께 담습니다.")
                .nutrients("단백질: 12g, 탄수화물: 35g, 지방: 15g")
                .build();

        // when
        Diet savedDiet = dietRepository.save(diet);

        // then
        assertThat(savedDiet.getId()).isNotNull();
        assertThat(savedDiet.getMember().getId()).isEqualTo(member.getId());
        assertThat(savedDiet.getName()).isEqualTo("퀴노아 샐러드");
        assertThat(savedDiet.getMealType()).isEqualTo(MealType.LUNCH);
    }

    @Test
    @DisplayName("회원별 식단 조회 테스트")
    void findByMember_WithDiets_ReturnsDietList() {
        // given
        Diet diet1 = Diet.builder()
                .member(member)
                .date(LocalDate.of(2024, 1, 15))
                .name("퀴노아 샐러드")
                .mealType(MealType.LUNCH)
                .build();
                
        Diet diet2 = Diet.builder()
                .member(member)
                .date(LocalDate.of(2024, 1, 15))
                .name("버섯 리소토")
                .mealType(MealType.DINNER)
                .build();
                
        dietRepository.save(diet1);
        dietRepository.save(diet2);

        // when
        List<Diet> diets = dietRepository.findByMember(member);

        // then
        assertThat(diets).hasSize(2);
        assertThat(diets).extracting(Diet::getName)
                .containsExactlyInAnyOrder("퀴노아 샐러드", "버섯 리소토");
    }

    @Test
    @DisplayName("날짜별 회원 식단 조회 테스트")
    void findByMemberIdAndDate_WithDiets_ReturnsDietList() {
        // given
        LocalDate targetDate = LocalDate.of(2024, 1, 15);
        
        // 1월 15일 식단들
        Diet diet1 = Diet.builder()
                .member(member)
                .date(targetDate)
                .name("애플 베리 오트밀")
                .mealType(MealType.BREAKFAST)
                .build();
                
        Diet diet2 = Diet.builder()
                .member(member)
                .date(targetDate)
                .name("퀴노아 샐러드")
                .mealType(MealType.LUNCH)
                .build();
                
        Diet diet3 = Diet.builder()
                .member(member)
                .date(targetDate)
                .name("버섯 리소토")
                .mealType(MealType.DINNER)
                .build();
        
        // 2월 5일 식단 (다른 날짜)
        Diet diet4 = Diet.builder()
                .member(member)
                .date(LocalDate.of(2024, 2, 5))
                .name("두부 스테이크")
                .mealType(MealType.DINNER)
                .build();
        
        dietRepository.save(diet1);
        dietRepository.save(diet2);
        dietRepository.save(diet3);
        dietRepository.save(diet4);

        // when
        List<Diet> diets = dietRepository.findByMemberIdAndDate(member.getId(), targetDate);

        // then
        assertThat(diets).hasSize(3);
        assertThat(diets).extracting(Diet::getName)
                .containsExactlyInAnyOrder("애플 베리 오트밀", "퀴노아 샐러드", "버섯 리소토");
    }
} 