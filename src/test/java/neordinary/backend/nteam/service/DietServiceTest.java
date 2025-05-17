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
    private LocalDate startDate;
    private LocalDate endDate;
    private List<Diet> dietList;

    @BeforeEach
    void setUp() {
        memberId = UUID.randomUUID();
        member = Member.builder().id(memberId).build();
        startDate = LocalDate.of(2024, 1, 1);
        endDate = LocalDate.of(2024, 1, 31);

        dietList = new ArrayList<>();
        LocalDate dietDate = LocalDate.of(2024, 1, 15);
        
        dietList.add(Diet.builder()
                .id(1L)
                .member(member)
                .date(dietDate)
                .name("퀴노아 샐러드")
                .mealType(MealType.LUNCH)
                .image("image_data")
                .ingredients("퀴노아, 토마토, 오이, 올리브 오일")
                .receipts("1. 퀴노아를 삶습니다. 2. 채소를 썰어 퀴노아와 함께 담습니다.")
                .nutrients("단백질: 12g, 탄수화물: 35g, 지방: 15g")
                .build());
    }

    @Test
    @DisplayName("기간 내 식단 조회 성공 테스트")
    void getDietsByPeriod_Success() {
        // given
        given(memberRepository.findById(memberId)).willReturn(Optional.of(member));
        given(dietRepository.findByMemberAndDateBetweenOrderByDateAscMealTypeAsc(member, startDate, endDate))
                .willReturn(dietList);

        // when
        List<DietResponseDto> result = dietService.getDietsByPeriod(memberId, startDate, endDate);

        // then
        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(1);
        assertThat(result.get(0).getName()).isEqualTo("퀴노아 샐러드");
        assertThat(result.get(0).getIngredients()).isEqualTo("퀴노아, 토마토, 오이, 올리브 오일");
    }

    @Test
    @DisplayName("시작일이 종료일보다 늦은 경우 예외 발생 테스트")
    void getDietsByPeriod_StartDateAfterEndDate_ThrowsException() {
        // given
        LocalDate invalidStartDate = LocalDate.of(2024, 2, 1);

        // when, then
        assertThatThrownBy(() -> dietService.getDietsByPeriod(memberId, invalidStartDate, startDate))
                .isInstanceOf(DietHandler.class)
                .hasMessageContaining("시작일이 종료일보다 늦을 수 없습니다");
    }

    @Test
    @DisplayName("조회 기간이 2개월 초과인 경우 예외 발생 테스트")
    void getDietsByPeriod_PeriodExceeding2Months_ThrowsException() {
        // given
        LocalDate farEndDate = LocalDate.of(2024, 4, 1); // 3개월 차이

        // when, then
        assertThatThrownBy(() -> dietService.getDietsByPeriod(memberId, startDate, farEndDate))
                .isInstanceOf(DietHandler.class)
                .hasMessageContaining("기간 설정 기준을 초과하였습니다");
    }

    @Test
    @DisplayName("존재하지 않는 회원의 식단 조회 실패 테스트")
    void getDietsByPeriod_MemberNotFound_ThrowsException() {
        // given
        given(memberRepository.findById(memberId)).willReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() -> dietService.getDietsByPeriod(memberId, startDate, endDate))
                .isInstanceOf(MemberHandler.class)
                .hasMessageContaining("사용자가 없습니다");
    }
} 