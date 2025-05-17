package neordinary.backend.nteam.repository;

import neordinary.backend.nteam.entity.Diary;
import neordinary.backend.nteam.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface DiaryRepository extends JpaRepository<Diary, Long> {
    List<Diary> findByMember(Member member);
    
    List<Diary> findByMemberAndCreatedAtBetweenOrderByCreatedAtAsc(
            Member member, 
            LocalDateTime startDateTime, 
            LocalDateTime endDateTime);
    
    Optional<Diary> findByMemberAndCreatedAtBetween(
            Member member, 
            LocalDateTime startOfDay, 
            LocalDateTime endOfDay);

    @Query("SELECT d FROM Diary d WHERE d.member.id = :memberId AND d.createdAt = :date")
    List<Diary> findByDiariesByMemberIdAndDate(UUID memberId, LocalDate date);

    @Query("SELECT d FROM Diary d WHERE d.member.id = :memberId AND DATE(d.createdAt) = :date")
    List<Diary> findAllByMemberIdAndCreatedDate(@Param("memberId") UUID memberId,
                                                @Param("date") LocalDate date);

}
