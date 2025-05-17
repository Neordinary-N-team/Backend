package neordinary.backend.nteam.dto;

import lombok.Data;
import neordinary.backend.nteam.entity.Member;

@Data
public class DiaryRequestDto {
    private Long id;
    private String image;
    private String ingredients;
    private String comment;
    private Member member;
}
