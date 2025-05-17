package neordinary.backend.nteam.converter;

import neordinary.backend.nteam.dto.MemberRequestDto;
import neordinary.backend.nteam.dto.MemberResponseDto;
import neordinary.backend.nteam.entity.Member;
import neordinary.backend.nteam.entity.enums.VeganLevel;

public class MemberConverter {
    public static Member toEntity(MemberRequestDto dto) {
        if (dto == null) return null;

        return Member.builder()
                .pregDate(dto.getPregDate())
                .height(dto.getHeight())
                .weight(dto.getWeight())
                .bmi(dto.getBmi())
                .diseases(dto.getDiseases())
                .prePregnant(dto.getPrePregnant())
                .hasMorningSickness(dto.getHasMorningSickness())
                .veganLevel(dto.getVeganLevel() != null ? VeganLevel.valueOf(dto.getVeganLevel()) : null)
                .vegProteins(dto.getVegProteins())
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
                .bmi(member.getBmi())
                .diseases(member.getDiseases())
                .prePregnant(member.getPrePregnant())
                .hasMorningSickness(member.getHasMorningSickness())
                .veganLevel(member.getVeganLevel() != null ? member.getVeganLevel().name() : null)
                .vegProteins(member.getVegProteins())
                .bannedVegetables(member.getBannedVegetables())
                .memberLevel(member.getMemberLevel())
                .build();
    }

}
