package neordinary.backend.nteam.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.*;


import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberRequestDto {
    @Schema(example = "2025-05-17")
    @NotNull(message = "pregDate는 필수입니다.")
    private LocalDate pregDate;

    @Schema(example = "160")
    @NotNull(message = "height는 필수입니다.")
    @Min(value = 50, message = "height는 50 이상이어야 합니다.")
    @Max(value = 250, message = "height는 250 이하이어야 합니다.")
    private Integer height;

    @Schema(example = "60")
    @NotNull(message = "weight는 필수입니다.")
    @Min(value = 20, message = "weight는 20 이상이어야 합니다.")
    @Max(value = 300, message = "weight는 300 이하이어야 합니다.")
    private Integer weight;

    @Schema(example = "21.5")
    @NotNull(message = "bmi는 필수입니다.")
    @Max(value = 50, message = "weight는 50 이하이어야 합니다.")
    private Float bmi;

    @Schema(example = "고혈압")
    @Size(max = 50, message = "diseases는 최대 50자까지 입력 가능합니다.")
    private String diseases;

    @Schema(example = "true")
    @NotNull(message = "prePregnant는 필수입니다.")
    private Boolean prePregnant;

    @Schema(example = "false")
    @NotNull(message = "hasMorningSickness는 필수입니다.")
    private Boolean hasMorningSickness;

    @Schema(example = "OVO")
    @Size(max = 50, message = "veganLevel은 최대 50자까지 입력 가능합니다.")
    private String veganLevel;

    @Schema(example = "두부")
    @Size(max = 50, message = "vegProteins는 최대 50자까지 입력 가능합니다.")
    private String vegProteins;

    @Schema(example = "오이")
    @Size(max = 50, message = "bannedVegetables는 최대 50자까지 입력 가능합니다.")
    private String bannedVegetables;

    @Schema(example = "1")
    @NotNull(message = "memberLevel은 필수입니다.")
    @Min(value = 1, message = "memberLevel은 1 이상이어야 합니다.")
    private Integer memberLevel;
}
