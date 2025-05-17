package neordinary.backend.nteam.repository;

import neordinary.backend.nteam.config.TestConfig;
import neordinary.backend.nteam.entity.Member;
import neordinary.backend.nteam.entity.enums.MorningSickness;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(TestConfig.class)
@ActiveProfiles("test")
class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Test
    @DisplayName("회원 저장 테스트")
    void save_Member_Success() {
        // given
        Member member = Member.builder()
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

        // when
        Member savedMember = memberRepository.save(member);

        // then
        assertThat(savedMember.getId()).isNotNull();
        assertThat(savedMember.getHeight()).isEqualTo(160);
        assertThat(savedMember.getWeight()).isEqualTo(60);
        assertThat(savedMember.getHasMorningSickness()).isEqualTo(MorningSickness.NAUSEA);
    }

    @Test
    @DisplayName("ID로 회원 조회 테스트")
    void findById_ExistingMember_ReturnsMember() {
        // given
        Member member = Member.builder()
                .pregDate(LocalDate.of(2024, 5, 17))
                .height(160)
                .weight(60)
                .allowedVeganFoods(Collections.singletonList("과일,채소"))
                .memberLevel(1)
                .build();
                
        Member savedMember = memberRepository.save(member);
        UUID memberId = savedMember.getId();

        // when
        Optional<Member> foundMember = memberRepository.findById(memberId);

        // then
        assertThat(foundMember).isPresent();
        assertThat(foundMember.get().getId()).isEqualTo(memberId);
        assertThat(foundMember.get().getHeight()).isEqualTo(160);
    }

    @Test
    @DisplayName("존재하지 않는 ID로 회원 조회 테스트")
    void findById_NonExistingMember_ReturnsEmpty() {
        // given
        UUID nonExistingId = UUID.randomUUID();

        // when
        Optional<Member> foundMember = memberRepository.findById(nonExistingId);

        // then
        assertThat(foundMember).isEmpty();
    }

    @Test
    @DisplayName("회원 삭제 테스트")
    void delete_ExistingMember_RemovesMember() {
        // given
        Member member = Member.builder()
                .pregDate(LocalDate.of(2024, 5, 17))
                .height(160)
                .weight(60)
                .build();
                
        Member savedMember = memberRepository.save(member);
        UUID memberId = savedMember.getId();

        // when
        memberRepository.delete(savedMember);
        Optional<Member> foundMember = memberRepository.findById(memberId);

        // then
        assertThat(foundMember).isEmpty();
    }

    @Test
    @DisplayName("전체 회원 조회 테스트")
    void findAll_WithMembers_ReturnsMemberList() {
        // given
        memberRepository.deleteAll(); // 기존 데이터 정리
        
        Member member1 = Member.builder()
                .pregDate(LocalDate.of(2024, 5, 17))
                .height(160)
                .weight(60)
                .allowedVeganFoods(Collections.singletonList("과일,채소"))
                .build();
                
        Member member2 = Member.builder()
                .pregDate(LocalDate.of(2024, 6, 15))
                .height(165)
                .weight(65)
                .allowedVeganFoods(Collections.singletonList("과일,채소"))
                .build();
                
        memberRepository.save(member1);
        memberRepository.save(member2);

        // when
        List<Member> allMembers = memberRepository.findAll();

        // then
        assertThat(allMembers).hasSize(2);
    }
} 