package neordinary.backend.nteam.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import neordinary.backend.nteam.converter.MemberConverter;
import neordinary.backend.nteam.dto.MemberRequestDto;
import neordinary.backend.nteam.dto.MemberResponseDto;
import neordinary.backend.nteam.entity.Member;
import neordinary.backend.nteam.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;


@Service
@RequiredArgsConstructor
@Transactional
public class MemberServiceImpl implements MemberService {
    private final MemberRepository memberRepository;

    @Override
    public MemberResponseDto createMember(MemberRequestDto requestDto) {
        Member member = MemberConverter.toEntity(requestDto);
        Member saved = memberRepository.save(member);
        return MemberConverter.toDto(saved);
    }

    @Override
    public MemberResponseDto updateMember(UUID id, MemberRequestDto requestDto) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("회원을 찾을 수 없습니다."));

        member.updateFrom(requestDto);

        return MemberConverter.toDto(member);
    }

    @Override
    public MemberResponseDto getMember(UUID id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("회원을 찾을 수 없습니다."));

        return MemberConverter.toDto(member);
    }

    @Override
    public MemberResponseDto upgradeMemberLevel(UUID id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("회원을 찾을 수 없습니다."));

        Integer currentLevel = member.getMemberLevel();
        if (currentLevel == null) {
            currentLevel = 1;
        }
        member.setMemberLevel(currentLevel + 1);

        return MemberConverter.toDto(member);
    }
}
