package neordinary.backend.nteam.entity;

import jakarta.persistence.*;
import lombok.*;
import neordinary.backend.nteam.entity.enums.MealType;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Diary extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "image", columnDefinition = "TEXT", length = 100000)
    private String image;

    @Column(name = "ingredients")
    private String ingredients;

    @Column(name = "comment")
    @Setter
    private String comment;

    @Enumerated(EnumType.STRING)
    @Column(name = "meal_type")
    private MealType mealType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    @Setter
    private Member member;
} 