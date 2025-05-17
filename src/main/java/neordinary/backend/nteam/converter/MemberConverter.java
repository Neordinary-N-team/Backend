package neordinary.backend.nteam.converter;

import neordinary.backend.nteam.dto.MemberRequestDto;
import neordinary.backend.nteam.dto.MemberResponseDto;
import neordinary.backend.nteam.entity.Member;

public class MemberConverter {
    public static Member toEntity(MemberRequestDto dto) {
        if (dto == null) return null;

        return Member.builder()
                .pregDate(dto.getPregDate())
                .height(dto.getHeight())
                .weight(dto.getWeight())
                .diseases(dto.getDiseases())
                .prePregnant(dto.getPrePregnant())
                .hasMorningSickness(dto.getHasMorningSickness())
                .allowedVeganFoods(dto.getAllowedVeganFoods())
                .bannedVegetables(dto.getBannedVegetables())
                .memberLevel(dto.getMemberLevel())
                .build();
    }

    public static MemberResponseDto toDto(Member member) {
        if (member == null) return null;

        return MemberResponseDto.builder()
                .id(member.getId())
                .pregDate(member.getPregDate())
                .height(member.getHeight())
                .weight(member.getWeight())
                .diseases(member.getDiseases())
                .prePregnant(member.getPrePregnant())
                .hasMorningSickness(member.getHasMorningSickness())
                .allowedVeganFoods(member.getAllowedVeganFoods())
                .bannedVegetables(member.getBannedVegetables())
                .memberLevel(member.getMemberLevel())
                .build();
    }

}
