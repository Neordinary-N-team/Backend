package neordinary.backend.nteam.repository;

import neordinary.backend.nteam.entity.Diary;
import neordinary.backend.nteam.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
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
            
    @Query("SELECT d FROM Diary d WHERE d.member = :member AND FUNCTION('DATE', d.createdAt) = :date ORDER BY d.createdAt ASC")
    List<Diary> findByMemberAndDate(Member member, LocalDate date);
}
