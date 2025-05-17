package neordinary.backend.nteam.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DiaryResponseDto {
    private Long id;
    private UUID memberId;
    private String image;
    private String ingredients;
    private String satisfiedComment;
    private String dissatisfiedComment;
    private LocalDateTime createdAt;
} 