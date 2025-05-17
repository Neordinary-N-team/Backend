package neordinary.backend.nteam.entity.enums;

public enum VeganLevel {
    FRUITARIAN("프루테리언"),
    VEGAN("비건"),
    LACTO("락토"),
    OVO("오보"),
    LACTO_OVO("락토오보"),
    PESCO("페스코"),
    POLLO("폴로"),
    FLEXITARIAN("플렉시테리언");

    private final String koreanName;

    VeganLevel(String koreanName) {
        this.koreanName = koreanName;
    }

    public String getKoreanName() {
        return koreanName;
    }
} 