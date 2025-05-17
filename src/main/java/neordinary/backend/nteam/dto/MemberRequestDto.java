package neordinary.backend.nteam.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberRequestDto {
    @Schema(example = "2025-05-17")
    private LocalDate pregDate;
    @Schema(example = "160")
    private Integer height;
    @Schema(example = "60")
    private Integer weight;
    @Schema(example = "21.5")
    private Float bmi;
    @Schema(example = "고혈압")
    private String diseases;
    @Schema(example = "true")
    private Boolean prePregnant;
    @Schema(example = "false")
    private Boolean hasMorningSickness;
    @Schema(example = "OVO")
    private String veganLevel;
    @Schema(example = "두부")
    private String vegProteins;
    @Schema(example = "오이")
    private String bannedVegetables;
    @Schema(example = "1")
    private Integer memberLevel;
}
