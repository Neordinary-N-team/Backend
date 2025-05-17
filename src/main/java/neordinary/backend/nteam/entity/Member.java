package neordinary.backend.nteam.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import neordinary.backend.nteam.converter.StringListConverter;
import neordinary.backend.nteam.dto.MemberRequestDto;
import neordinary.backend.nteam.entity.enums.MorningSickness;

import java.time.LocalDate;
import java.util.List;
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

    @Column(name = "diseases")
    private String diseases;

    @Column(name = "pre_pregnant")
    private int prePregnant;

    @Enumerated(EnumType.STRING)
    @Column(name = "has_morning_sickness")
    private MorningSickness hasMorningSickness;

    @Convert(converter = StringListConverter.class)
    @Column(name = "allowed_vegan_foods")
    private List<String> allowedVeganFoods;

    @Column(name = "banned_vegetables")
    private String bannedVegetables;

    @Column(name = "member_level")
    private Integer memberLevel = 35;

    public void setMemberLevel(Integer memberLevel) {
        this.memberLevel = memberLevel;
    }

    public int getPregnancyWeek() {
        if (pregDate == null) {
            return 0;
        }
        LocalDate today = LocalDate.now();
        return (int) (today.toEpochDay() - pregDate.toEpochDay()) / 7;
    }

    public void updateFrom(MemberRequestDto dto) {
        this.pregDate = dto.getPregDate();
        this.height = dto.getHeight();
        this.weight = dto.getWeight();
        this.diseases = dto.getDiseases();
        this.prePregnant = dto.getPrePregnant();
        this.hasMorningSickness = dto.getHasMorningSickness();
        this.allowedVeganFoods = dto.getAllowedVeganFoods();
        this.bannedVegetables = dto.getBannedVegetables();
        this.memberLevel = dto.getMemberLevel();
    }
}
