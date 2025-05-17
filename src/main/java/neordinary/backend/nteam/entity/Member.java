package neordinary.backend.nteam.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import neordinary.backend.nteam.dto.MemberRequestDto;
import neordinary.backend.nteam.entity.enums.VeganLevel;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Member extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @Column(name = "preg_date")
    private LocalDate pregDate;

    @Column(name = "height")
    private Integer height;

    @Column(name = "weight")
    private Integer weight;

    @Column(name = "bmi")
    private Float bmi;

    @Column(name = "diseases")
    private String diseases;

    @Column(name = "pre_pregnant")
    private Boolean prePregnant;

    @Column(name = "has_morning_sickness")
    private Boolean hasMorningSickness;

    @Enumerated(EnumType.STRING)
    @Column(name = "vegan_level")
    private VeganLevel veganLevel;

    @Column(name = "veg_proteins")
    private String vegProteins;

    @Column(name = "banned_vegetables")
    private String bannedVegetables;

    @Column(name = "member_level")
    private Integer memberLevel;

    public void setMemberLevel(Integer memberLevel) {
        this.memberLevel = memberLevel;
    }

    public void updateFrom(MemberRequestDto dto) {
        this.pregDate = dto.getPregDate();
        this.height = dto.getHeight();
        this.weight = dto.getWeight();
        this.bmi = dto.getBmi();
        this.diseases = dto.getDiseases();
        this.prePregnant = dto.getPrePregnant();
        this.hasMorningSickness = dto.getHasMorningSickness();
        this.veganLevel = VeganLevel.valueOf(dto.getVeganLevel());
        this.vegProteins = dto.getVegProteins();
        this.bannedVegetables = dto.getBannedVegetables();
        this.memberLevel = dto.getMemberLevel();
    }
}