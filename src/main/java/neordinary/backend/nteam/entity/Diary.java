package neordinary.backend.nteam.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    @Column(name = "satisfied_comment")
    private String satisfiedComment;

    @Column(name = "dissatisfied_comment")
    private String dissatisfiedComment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;
} 