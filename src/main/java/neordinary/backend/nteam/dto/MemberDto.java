//package neordinary.backend.nteam.dto;
//
//import lombok.Builder;
//import lombok.Getter;
//
//import java.time.LocalDateTime;
//import java.time.format.DateTimeFormatter;
//import java.util.UUID;
//
//public class MemberDto {
//
//    @Getter
//    public static class MemberCreateRequest {
//        private String pregDate;
//        private int height;
//        private int weight;
//        private float bmi;
//        private String diseases;
//        private boolean prePregnant;
//        private boolean hasMorningSickness;
//        private String veganLevel;
//        private String vegProteins;
//        private String bannedVegetables;
//        private Integer memberLevel;
//
//        public MemberCreateRequest(String mockUser, String mail) {
//        }
//    }
//
//    @Getter
//    public static class MemberUpdateRequest {
//        private String pregDate;
//        private int height;
//        private int weight;
//        private float bmi;
//        private String diseases;
//        private boolean prePregnant;
//        private boolean hasMorningSickness;
//        private String veganLevel;
//        private String vegProteins;
//        private String bannedVegetables;
//        private Integer memberLevel;
//    }
//
//    @Getter
//    @Builder
//    public static class MemberResponse {
//        private UUID id;
//        private String pregDate;
//        private int height;
//        private int weight;
//        private float bmi;
//        private String diseases;
//        private boolean prePregnant;
//        private boolean hasMorningSickness;
//        private String veganLevel;
//        private String vegProteins;
//        private String bannedVegetables;
//        private Integer memberLevel;
//        private String createdAt;
//        private String updatedAt;
//
//        public void setMemberLevel(Integer memberLevel) {
//            this.memberLevel = memberLevel;
//        }
//
//        public Integer getMemberLevel() {
//            return memberLevel;
//        }
//    }
//
//    public static MemberResponse toResponse(MemberCreateRequest request, UUID id) {
//        String now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
//        return MemberResponse.builder()
//                .id(id)
//                .pregDate(request.getPregDate())
//                .height(request.getHeight())
//                .weight(request.getWeight())
//                .bmi(request.getBmi())
//                .diseases(request.getDiseases())
//                .prePregnant(request.isPrePregnant())
//                .hasMorningSickness(request.isHasMorningSickness())
//                .veganLevel(request.getVeganLevel())
//                .vegProteins(request.getVegProteins())
//                .bannedVegetables(request.getBannedVegetables())
//                .memberLevel(1)
//                .createdAt(now)
//                .updatedAt(now)
//                .build();
//    }
//
//    public static MemberResponse toUpdatedResponse(UUID id, MemberUpdateRequest request, MemberResponse existing) {
//        String now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
//        return MemberResponse.builder()
//                .id(id)
//                .pregDate(request.getPregDate())
//                .height(request.getHeight())
//                .weight(request.getWeight())
//                .bmi(request.getBmi())
//                .diseases(request.getDiseases())
//                .prePregnant(request.isPrePregnant())
//                .hasMorningSickness(request.isHasMorningSickness())
//                .veganLevel(request.getVeganLevel())
//                .vegProteins(request.getVegProteins())
//                .bannedVegetables(request.getBannedVegetables())
//                .memberLevel(request.getMemberLevel())
//                .createdAt(existing.getCreatedAt())
//                .updatedAt(now)
//                .build();
//    }
//}
