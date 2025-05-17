//package neordinary.backend.nteam.Member;
//
//import neordinary.backend.nteam.entity.Member;
//import neordinary.backend.nteam.dto.MemberRequestDto;
//import neordinary.backend.nteam.dto.MemberResponseDto;
//import neordinary.backend.nteam.repository.MemberRepository;
//import neordinary.backend.nteam.service.MemberServiceImpl;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.time.LocalDate;
//import java.util.Optional;
//import java.util.UUID;
//
//import static org.assertj.core.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//class MemberServiceImplTest {
//
//    @Mock
//    private MemberRepository memberRepository;
//
//    @InjectMocks
//    private MemberServiceImpl memberService;
//
//    private Member member;
//    private UUID memberId;
//
//    @BeforeEach
//    void setUp() {
//        memberId = UUID.randomUUID();
//        member = new Member(
//                UUID.randomUUID(), LocalDate.now(), 170, 70,
//                25.1, "당뇨", false, false, 
//                "OVO", "두부", "오이", 1);
//    }
//
//    @Test
//    void createMember_성공() {
//        // given
//        MemberRequestDto requestDto = new MemberRequestDto("김도연", "do@example.com");
//        when(memberRepository.save(any(Member.class))).thenReturn(member);
//
//        // when
//        MemberResponseDto response = memberService.createMember(requestDto);
//
//        // then
//        assertThat(response.getId()).isEqualTo(memberId);
//        assertThat(response.getName()).isEqualTo("김도연");
//    }
//
//    @Test
//    void getMember_성공() {
//        // given
//        when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));
//
//        // when
//        MemberResponseDto response = memberService.getMember(memberId);
//
//        // then
//        assertThat(response.getId()).isEqualTo(memberId);
//        assertThat(response.getName()).isEqualTo("김도연");
//    }
//
//    @Test
//    void getMember_실패_존재하지않음() {
//        // given
//        when(memberRepository.findById(memberId)).thenReturn(Optional.empty());
//
//        // when & then
//        assertThatThrownBy(() -> memberService.getMember(memberId))
//                .isInstanceOf(IllegalArgumentException.class)  // 예외 타입은 실제 구현에 따라 다름
//                .hasMessageContaining("회원이 존재하지 않습니다");
//    }
//
//    @Test
//    void updateMember_성공() {
//        // given
//        MemberRequestDto requestDto = new MemberRequestDto("업데이트된 이름", "new@example.com");
//        when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));
//        when(memberRepository.save(any(Member.class))).thenReturn(member);
//
//        // when
//        MemberResponseDto response = memberService.updateMember(memberId, requestDto);
//
//        // then
//        assertThat(response.getName()).isEqualTo("업데이트된 이름");
//    }
//
//    @Test
//    void upgradeMemberLevel_성공() {
//        // given
//        when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));
//        member.upgradeLevel(); // 직접 로직 실행 (실제 구현에 따라 수정)
//        when(memberRepository.save(any(Member.class))).thenReturn(member);
//
//        // when
//        MemberResponseDto response = memberService.upgradeMemberLevel(memberId);
//
//        // then
//        assertThat(response.getLevel()).isEqualTo(member.getLevel());
//    }
//}
