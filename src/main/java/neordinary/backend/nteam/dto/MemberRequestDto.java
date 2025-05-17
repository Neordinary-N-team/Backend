package neordinary.backend.nteam.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberRequestDto {
    private LocalDate pregDate;
    private Integer height;
    private Integer weight;
    private Float bmi;
    private String diseases;
    private Boolean prePregnant;
    private Boolean hasMorningSickness;
    private String veganLevel;
    private String vegProteins;
    private String bannedVegetables;
    private Integer memberLevel;
}
