package neordinary.backend.nteam.service;

import neordinary.backend.nteam.dto.MemberRequestDto;
import neordinary.backend.nteam.dto.MemberResponseDto;
import neordinary.backend.nteam.entity.Member;
import neordinary.backend.nteam.entity.enums.MorningSickness;
import neordinary.backend.nteam.global.exception.handler.MemberHandler;
import neordinary.backend.nteam.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class MemberServiceImplTest {

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private MemberServiceImpl memberService;

    private UUID memberId;
    private MemberRequestDto memberRequestDto;
    private Member member;

    @BeforeEach
    void setUp() {
        memberId = UUID.randomUUID();
        
        memberRequestDto = MemberRequestDto.builder()
                .pregDate(LocalDate.of(2024, 5, 17))
                .height(160)
                .weight(60)
                .diseases("고혈압")
                .prePregnant(1)
                .hasMorningSickness(MorningSickness.NAUSEA)
                .allowedVeganFoods(Collections.singletonList("과일,채소"))
                .bannedVegetables("오이")
                .memberLevel(35)
                .build();

        member = Member.builder()
                .pregDate(LocalDate.of(2024, 5, 17))
                .height(160)
                .weight(60)
                .diseases("고혈압")
                .prePregnant(1)
                .hasMorningSickness(MorningSickness.NAUSEA)
                .allowedVeganFoods(Collections.singletonList("과일,채소"))
                .bannedVegetables("오이")
                .memberLevel(35)
                .build();
    }

    @Test
    @DisplayName("회원 생성 성공 테스트")
    void createMember_Success() {
        // given
        given(memberRepository.save(any(Member.class))).willReturn(member);

        // when
        MemberResponseDto result = memberService.createMember(memberRequestDto);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(memberId);
        assertThat(result.getHeight()).isEqualTo(160);
        assertThat(result.getWeight()).isEqualTo(60);
        assertThat(result.getAllowedVeganFoods()).isEqualTo("과일,채소");
        verify(memberRepository, times(1)).save(any(Member.class));
    }

    @Test
    @DisplayName("회원 정보 업데이트 성공 테스트")
    void updateMember_Success() {
        // given
        given(memberRepository.findById(memberId)).willReturn(Optional.of(member));
        
        MemberRequestDto updateRequestDto = MemberRequestDto.builder()
                .pregDate(LocalDate.of(2024, 5, 17))
                .height(160)
                .weight(60)
                .diseases("고혈압")
                .prePregnant(1)
                .hasMorningSickness(MorningSickness.NAUSEA)
                .allowedVeganFoods(Collections.singletonList("과일,채소"))
                .bannedVegetables("계란")
                .memberLevel(35)
                .build();
                
        Member updatedMember = Member.builder()
                .pregDate(LocalDate.of(2024, 5, 17))
                .height(160)
                .weight(60)
                .diseases("고혈압")
                .prePregnant(1)
                .hasMorningSickness(MorningSickness.NAUSEA)
                .allowedVeganFoods(Collections.singletonList("과일,채소"))
                .bannedVegetables("오이")
                .memberLevel(36)
                .build();
                
        given(memberRepository.save(any(Member.class))).willReturn(updatedMember);

        // when
        MemberResponseDto result = memberService.updateMember(memberId, updateRequestDto);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(memberId);
        assertThat(result.getHeight()).isEqualTo(160);
        assertThat(result.getWeight()).isEqualTo(60);
        assertThat(result.getDiseases()).isEqualTo("고혈압");
        assertThat(result.getPrePregnant()).isEqualTo(1);
        assertThat(result.getHasMorningSickness()).isEqualTo("NAUSEA");
        assertThat(result.getAllowedVeganFoods()).isEqualTo("과일,채소");
        assertThat(result.getBannedVegetables()).isEqualTo("오이");
        assertThat(result.getMemberLevel()).isEqualTo(35);
        verify(memberRepository, times(1)).findById(memberId);
        verify(memberRepository, times(1)).save(any(Member.class));
    }

    @Test
    @DisplayName("존재하지 않는 회원 정보 업데이트 실패 테스트")
    void updateMember_MemberNotFound_ThrowsException() {
        // given
        given(memberRepository.findById(memberId)).willReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() -> memberService.updateMember(memberId, memberRequestDto))
                .isInstanceOf(MemberHandler.class)
                .hasMessageContaining("사용자가 없습니다");
    }

    @Test
    @DisplayName("회원 조회 성공 테스트")
    void getMember_Success() {
        // given
        given(memberRepository.findById(memberId)).willReturn(Optional.of(member));

        // when
        MemberResponseDto result = memberService.getMember(memberId);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(memberId);
        assertThat(result.getAllowedVeganFoods()).isEqualTo("과일,채소");
    }

    @Test
    @DisplayName("존재하지 않는 회원 조회 실패 테스트")
    void getMember_MemberNotFound_ThrowsException() {
        // given
        given(memberRepository.findById(memberId)).willReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() -> memberService.getMember(memberId))
                .isInstanceOf(MemberHandler.class)
                .hasMessageContaining("사용자가 없습니다");
    }

    @Test
    @DisplayName("회원 레벨 업그레이드 성공 테스트")
    void upgradeMemberLevel_Success() {
        // given
        given(memberRepository.findById(memberId)).willReturn(Optional.of(member));

        // when
        MemberResponseDto result = memberService.upgradeMemberLevel(memberId);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(memberId);
        assertThat(result.getMemberLevel()).isEqualTo(36);
    }

    @Test
    @DisplayName("존재하지 않는 회원 레벨 업그레이드 실패 테스트")
    void upgradeMemberLevel_MemberNotFound_ThrowsException() {
        // given
        given(memberRepository.findById(memberId)).willReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() -> memberService.upgradeMemberLevel(memberId))
                .isInstanceOf(MemberHandler.class)
                .hasMessageContaining("사용자가 없습니다");
    }

    @Test
    @DisplayName("회원 레벨이 null일 때 업그레이드 성공 테스트")
    void upgradeMemberLevel_NullLevel_Success() {
        // given
        Member memberWithNullLevel = Member.builder()
                .id(memberId)
                .pregDate(LocalDate.of(2024, 5, 17))
                .height(160)
                .weight(60)
                .memberLevel(null)
                .build();
        
        given(memberRepository.findById(memberId)).willReturn(Optional.of(memberWithNullLevel));

        // when
        MemberResponseDto result = memberService.upgradeMemberLevel(memberId);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getMemberLevel()).isEqualTo(36);
    }
} 