package neordinary.backend.nteam.dto;

import lombok.*;
import neordinary.backend.nteam.entity.enums.MorningSickness;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberResponseDto {
    private UUID id;
    private LocalDate pregDate;
    private Integer height;
    private Integer weight;
    private String diseases;
    private int prePregnant;
    private MorningSickness hasMorningSickness;
    private List<String> allowedVeganFoods;
    private String bannedVegetables;
    private Integer memberLevel;
}
