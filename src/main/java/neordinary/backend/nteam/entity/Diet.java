package neordinary.backend.nteam.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import neordinary.backend.nteam.entity.enums.DietDifficulty;
import neordinary.backend.nteam.entity.enums.MealType;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Diet extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "name")
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "meal_type")
    private MealType mealType;

    @Setter
    @Schema(description = "이미지 URL")
    @Column(name = "image", columnDefinition = "TEXT", length = 100000)
    private String image;

    @Column(name = "ingredients")
    private String ingredients;

    @Setter
    @Column(name = "receipts")
    private String recipe;

    @Column(name = "nutrients")
    private String nutrients;

    @Column(name = "calories")
    private int calories;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "difficulty")
    private DietDifficulty difficulty;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;
}