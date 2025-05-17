package neordinary.backend.nteam.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
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
} 