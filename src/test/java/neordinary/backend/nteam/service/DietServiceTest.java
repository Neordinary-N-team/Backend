package neordinary.backend.nteam.service;

import neordinary.backend.nteam.dto.DietResponseDto;
import neordinary.backend.nteam.entity.Diet;
import neordinary.backend.nteam.entity.Member;
import neordinary.backend.nteam.entity.enums.MealType;
import neordinary.backend.nteam.global.exception.handler.DietHandler;
import neordinary.backend.nteam.global.exception.handler.MemberHandler;
import neordinary.backend.nteam.repository.DietRepository;
import neordinary.backend.nteam.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class DietServiceTest {

    @Mock
    private DietRepository dietRepository;

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private DietService dietService;

    private Member member;
    private UUID memberId;
    private LocalDate targetDate;
    private List<Diet> dietList;

    @BeforeEach
    void setUp() {
        memberId = UUID.randomUUID();
        member = Member.builder().id(memberId).build();
        targetDate = LocalDate.of(2024, 1, 15);

        dietList = new ArrayList<>();
        
        dietList.add(Diet.builder()
                .id(1L)
                .member(member)
                .date(targetDate)
                .name("퀴노아 샐러드")
                .mealType(MealType.LUNCH)
                .image("image_data")
                .ingredients("퀴노아, 토마토, 오이, 올리브 오일")
                .recipe("1. 퀴노아를 삶습니다. 2. 채소를 썰어 퀴노아와 함께 담습니다.")
                .nutrients("단백질: 12g, 탄수화물: 35g, 지방: 15g")
                .build());
    }

    @Test
    @DisplayName("날짜별 식단 조회 성공 테스트")
    void getDietsByDate_Success() {
        // given
        given(memberRepository.findById(memberId)).willReturn(Optional.of(member));
        given(dietRepository.findByMemberIdAndDate(memberId, targetDate))
                .willReturn(dietList);

        // when
        List<DietResponseDto> result = dietService.getDietsByDate(memberId, targetDate);

        // then
        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(1);
        assertThat(result.get(0).getName()).isEqualTo("퀴노아 샐러드");
        assertThat(result.get(0).getMealType()).isEqualTo(MealType.LUNCH);
    }

    @Test
    @DisplayName("존재하지 않는 회원의 식단 조회 실패 테스트")
    void getDietsByDate_MemberNotFound_ThrowsException() {
        // given
        given(memberRepository.findById(memberId)).willReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() -> dietService.getDietsByDate(memberId, targetDate))
                .isInstanceOf(MemberHandler.class)
                .hasMessageContaining("사용자가 없습니다");
    }
} 