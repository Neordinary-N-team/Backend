package neordinary.backend.nteam.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DietRequestDto {
    @NotNull(message = "회원 ID는 필수 입력 값입니다.")
    private UUID memberId;
} 