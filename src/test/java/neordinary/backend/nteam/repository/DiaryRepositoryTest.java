package neordinary.backend.nteam.repository;

import neordinary.backend.nteam.config.TestConfig;
import neordinary.backend.nteam.entity.Diary;
import neordinary.backend.nteam.entity.Member;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(TestConfig.class)
@ActiveProfiles("test")
class DiaryRepositoryTest {

    @Autowired
    private DiaryRepository diaryRepository;
    
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
    @DisplayName("다이어리 저장 테스트")
    void save_Diary_Success() {
        // given
        Diary diary = Diary.builder()
                .member(member)
                .image("image_data")
                .ingredients("ingredients_data")
                .comment("test comment")
                .build();

        // when
        Diary savedDiary = diaryRepository.save(diary);

        // then
        assertThat(savedDiary.getId()).isNotNull();
        assertThat(savedDiary.getMember().getId()).isEqualTo(member.getId());
        assertThat(savedDiary.getIngredients()).isEqualTo("ingredients_data");
    }

    @Test
    @DisplayName("회원별 다이어리 조회 테스트")
    void findByMember_WithDiaries_ReturnsDiaryList() {
        // given
        Diary diary1 = Diary.builder()
                .member(member)
                .image("image_data1")
                .ingredients("ingredients_data1")
                .build();
                
        Diary diary2 = Diary.builder()
                .member(member)
                .image("image_data2")
                .ingredients("ingredients_data2")
                .build();
                
        diaryRepository.save(diary1);
        diaryRepository.save(diary2);

        // when
        List<Diary> diaries = diaryRepository.findByMember(member);

        // then
        assertThat(diaries).hasSize(2);
        assertThat(diaries).extracting(Diary::getIngredients)
                .containsExactlyInAnyOrder("ingredients_data1", "ingredients_data2");
    }
} 