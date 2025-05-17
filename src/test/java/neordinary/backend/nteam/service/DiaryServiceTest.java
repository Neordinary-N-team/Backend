package neordinary.backend.nteam.service;

import neordinary.backend.nteam.dto.DiaryResponseDto;
import neordinary.backend.nteam.entity.Diary;
import neordinary.backend.nteam.entity.Member;
import neordinary.backend.nteam.global.exception.handler.DiaryHandler;
import neordinary.backend.nteam.global.exception.handler.MemberHandler;
import neordinary.backend.nteam.repository.DiaryRepository;
import neordinary.backend.nteam.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class DiaryServiceTest {

    @Mock
    private DiaryRepository diaryRepository;

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private DiaryService diaryService;

    private Member member;
    private UUID memberId;
    private LocalDate startDate;
    private LocalDate endDate;
    private List<Diary> diaryList;

    @BeforeEach
    void setUp() {
        memberId = UUID.randomUUID();
        member = Member.builder().id(memberId).build();
        startDate = LocalDate.of(2024, 1, 1);
        endDate = LocalDate.of(2024, 1, 31);

        diaryList = new ArrayList<>();
        LocalDateTime diaryDate = LocalDateTime.of(2024, 1, 15, 10, 0);
        
        diaryList.add(Diary.builder()
                .id(1L)
                .member(member)
                .image("image_data")
                .ingredients("ingredients_data")
                .satisfiedComment("satisfied_comment")
                .dissatisfiedComment("dissatisfied_comment")
                .build());
    }

    @Test
    @DisplayName("기간 내 다이어리 조회 성공 테스트")
    void getDiariesByPeriod_Success() {
        // given
        given(memberRepository.findById(memberId)).willReturn(Optional.of(member));
        given(diaryRepository.findByMemberAndCreatedAtBetweenOrderByCreatedAtAsc(
                any(Member.class),
                any(LocalDateTime.class),
                any(LocalDateTime.class)
        )).willReturn(diaryList);

        // when
        List<DiaryResponseDto> result = diaryService.getDiariesByPeriod(memberId, startDate, endDate);

        // then
        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(1);
        assertThat(result.get(0).getIngredients()).isEqualTo("ingredients_data");
    }

    @Test
    @DisplayName("시작일이 종료일보다 늦은 경우 예외 발생 테스트")
    void getDiariesByPeriod_StartDateAfterEndDate_ThrowsException() {
        // given
        LocalDate invalidStartDate = LocalDate.of(2024, 2, 1);

        // when, then
        assertThatThrownBy(() -> diaryService.getDiariesByPeriod(memberId, invalidStartDate, startDate))
                .isInstanceOf(DiaryHandler.class)
                .hasMessageContaining("시작일이 종료일보다 늦을 수 없습니다");
    }

    @Test
    @DisplayName("조회 기간이 2개월 초과인 경우 예외 발생 테스트")
    void getDiariesByPeriod_PeriodExceeding2Months_ThrowsException() {
        // given
        LocalDate farEndDate = LocalDate.of(2024, 4, 1); // 3개월 차이

        // when, then
        assertThatThrownBy(() -> diaryService.getDiariesByPeriod(memberId, startDate, farEndDate))
                .isInstanceOf(DiaryHandler.class)
                .hasMessageContaining("기간 설정 기준을 초과하였습니다.");
    }

    @Test
    @DisplayName("존재하지 않는 회원의 다이어리 조회 실패 테스트")
    void getDiariesByPeriod_MemberNotFound_ThrowsException() {
        // given
        given(memberRepository.findById(memberId)).willReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() -> diaryService.getDiariesByPeriod(memberId, startDate, endDate))
                .isInstanceOf(MemberHandler.class)
                .hasMessageContaining("사용자가 없습니다.");
    }

    @Test
    @DisplayName("다이어리 삭제 성공 테스트")
    void deleteDiary_Success() {
        // given
        LocalDate date = LocalDate.of(2024, 1, 15);
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(LocalTime.MAX);
        
        given(memberRepository.findById(memberId)).willReturn(Optional.of(member));
        given(diaryRepository.findByMemberAndCreatedAtBetween(member, startOfDay, endOfDay))
                .willReturn(Optional.of(diaryList.get(0)));

        // when
        diaryService.deleteDiary(memberId, date);

        // then
        verify(diaryRepository, times(1)).delete(diaryList.get(0));
    }

    @Test
    @DisplayName("존재하지 않는 회원의 다이어리 삭제 실패 테스트")
    void deleteDiary_MemberNotFound_ThrowsException() {
        // given
        LocalDate date = LocalDate.of(2024, 1, 15);
        given(memberRepository.findById(memberId)).willReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() -> diaryService.deleteDiary(memberId, date))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("회원을 찾을 수 없습니다");
    }

    @Test
    @DisplayName("존재하지 않는 다이어리 삭제 실패 테스트")
    void deleteDiary_DiaryNotFound_ThrowsException() {
        // given
        LocalDate date = LocalDate.of(2024, 1, 15);
        given(memberRepository.findById(memberId)).willReturn(Optional.of(member));
        given(diaryRepository.findByMemberAndCreatedAtBetween(
                any(Member.class),
                any(LocalDateTime.class),
                any(LocalDateTime.class)
        )).willReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() -> diaryService.deleteDiary(memberId, date))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("해당 날짜의 일기를 찾을 수 없습니다");
    }
} 