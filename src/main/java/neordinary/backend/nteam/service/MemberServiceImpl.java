package neordinary.backend.nteam.service;

import neordinary.backend.nteam.dto.MemberDto;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static neordinary.backend.nteam.dto.MemberDto.toResponse;
import static neordinary.backend.nteam.dto.MemberDto.toUpdatedResponse;

@Service
public class MemberServiceImpl implements MemberService {

    @Override
    public MemberDto.MemberResponse createMember(MemberDto.MemberCreateRequest request) {
        UUID newId = UUID.randomUUID();
        return toResponse(request, newId);
    }

    @Override
    public MemberDto.MemberResponse updateMember(UUID id, MemberDto.MemberUpdateRequest request) {
        // 기존 회원 정보 불러오는 부분을 Mock 처리
        MemberDto.MemberResponse existing = toResponse(new MemberDto.MemberCreateRequest("MockUser", "mock@example.com"), id);
        return toUpdatedResponse(id, request, existing);
    }

    @Override
    public MemberDto.MemberResponse getMember(UUID id) {
        return toResponse(new MemberDto.MemberCreateRequest("MockUser", "mock@example.com"), id);
    }

    @Override
    public MemberDto.MemberResponse upgradeMemberLevel(UUID id) {
        MemberDto.MemberResponse member = toResponse(
                new MemberDto.MemberCreateRequest("MockUser", "mock@example.com"), id);
        Integer currentLevel = member.getMemberLevel();
        if (currentLevel == null) {
            currentLevel = 1;
        }
        member.setMemberLevel(currentLevel + 1);  // 1씩 증가
        return member;
    }
}
