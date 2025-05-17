package neordinary.backend.nteam.service;

import neordinary.backend.nteam.dto.MemberDto;

import java.util.UUID;

public interface MemberService {

    MemberDto.MemberResponse createMember(MemberDto.MemberCreateRequest request);

    MemberDto.MemberResponse updateMember(UUID id, MemberDto.MemberUpdateRequest request);

    MemberDto.MemberResponse getMember(UUID id);

    MemberDto.MemberResponse upgradeMemberLevel(UUID id);

}
