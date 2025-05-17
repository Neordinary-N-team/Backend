package neordinary.backend.nteam.service;

import lombok.RequiredArgsConstructor;
import neordinary.backend.nteam.converter.MemberConverter;
import neordinary.backend.nteam.dto.MemberRequestDto;
import neordinary.backend.nteam.dto.MemberResponseDto;
import neordinary.backend.nteam.entity.Member;
import neordinary.backend.nteam.global.apiPayload.code.status.ErrorStatus;
import neordinary.backend.nteam.global.exception.handler.MemberHandler;
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
        try {
            Member member = MemberConverter.toEntity(requestDto);
            Member saved = memberRepository.save(member);
            return MemberConverter.toDto(saved);
        } catch (Exception e) {
            throw new MemberHandler(ErrorStatus.CREATE_MEMBER_FAILED);
        }
    }

    @Override
    public MemberResponseDto updateMember(UUID id, MemberRequestDto requestDto) {
        Member member = memberRepository.findById(id)
            .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

        try {
            member.updateFrom(requestDto);
            Member updatedMember = memberRepository.save(member);  // 변경 후 저장 필수
            return MemberConverter.toDto(updatedMember);
        } catch (IllegalArgumentException e) {
            throw new MemberHandler(ErrorStatus._BAD_REQUEST);  // BAD_REQUEST 같은 상태를 추가해도 좋고
        } catch (Exception e) {
            throw new MemberHandler(ErrorStatus._INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public MemberResponseDto getMember(UUID id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

        return MemberConverter.toDto(member);
    }

    @Override
    public MemberResponseDto upgradeMemberLevel(UUID id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

        Integer currentLevel = member.getMemberLevel();
        if (currentLevel == null) {
            currentLevel = 35;
        }
        member.setMemberLevel(currentLevel + 1);

        return MemberConverter.toDto(member);
    }
}
