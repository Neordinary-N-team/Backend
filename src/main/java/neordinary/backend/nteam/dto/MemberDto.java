package neordinary.backend.nteam.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MemberDto {
    @Getter
    public static class MemberCreateRequest {
        private String pregDate;
        private int height;
        private int weight;
        private float bmi;
        private String diseases;
        private boolean prePregnant;
        private boolean hasMorningSickness;
        private String veganLevel;
        private String vegProteins;
        private String bannedVegetables;
    }

    @Getter
    public static class MemberUpdateRequest {
        private String pregDate;
        private int height;
        private int weight;
        private float bmi;
        private String diseases;
        private boolean prePregnant;
        private boolean hasMorningSickness;
        private String veganLevel;
        private String vegProteins;
        private String bannedVegetables;
    }

    @Getter
    @Builder
    public static class MemberResponse {
        private Long id;
        private String pregDate;
        private int height;
        private int weight;
        private float bmi;
        private String diseases;
        private boolean prePregnant;
        private boolean hasMorningSickness;
        private String veganLevel;
        private String vegProteins;
        private String bannedVegetables;
        private String createdAt;
        private String updatedAt;
    }

    public static MemberResponse toResponse(MemberCreateRequest request, long id) {
        return MemberResponse.builder()
                .id(id)
                .pregDate(request.getPregDate())
                .height(request.getHeight())
                .weight(request.getWeight())
                .bmi(request.getBmi())
                .diseases(request.getDiseases())
                .prePregnant(request.isPrePregnant())
                .hasMorningSickness(request.isHasMorningSickness())
                .veganLevel(request.getVeganLevel())
                .vegProteins(request.getVegProteins())
                .bannedVegetables(request.getBannedVegetables())
                .createdAt(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .build();
    }

    public static MemberResponse toUpdatedResponse(Long id, MemberUpdateRequest request, MemberResponse existing) {
        return MemberResponse.builder()
                .id(id)
                .pregDate(request.getPregDate())
                .height(request.getHeight())
                .weight(request.getWeight())
                .bmi(request.getBmi())
                .diseases(request.getDiseases())
                .prePregnant(request.isPrePregnant())
                .hasMorningSickness(request.isHasMorningSickness())
                .veganLevel(request.getVeganLevel())
                .vegProteins(request.getVegProteins())
                .bannedVegetables(request.getBannedVegetables())
                .createdAt(existing.getCreatedAt())
                .updatedAt(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .build();
    }
}
