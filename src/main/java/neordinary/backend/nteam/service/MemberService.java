package neordinary.backend.nteam.service;

import neordinary.backend.nteam.dto.MemberRequestDto;
import neordinary.backend.nteam.dto.MemberResponseDto;

import java.util.UUID;

public interface MemberService {
    MemberResponseDto createMember(MemberRequestDto requestDto);
    MemberResponseDto updateMember(UUID id, MemberRequestDto requestDto);
    MemberResponseDto getMember(UUID id);
    MemberResponseDto upgradeMemberLevel(UUID id);
}
