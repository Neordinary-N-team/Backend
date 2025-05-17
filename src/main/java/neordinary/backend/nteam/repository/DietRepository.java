package neordinary.backend.nteam.repository;

import neordinary.backend.nteam.entity.Diet;
import neordinary.backend.nteam.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface DietRepository extends JpaRepository<Diet, Long> {
    List<Diet> findByMember(Member member);
    
    @Query("SELECT d FROM Diet d WHERE d.member = :member AND d.date BETWEEN :startDate AND :endDate ORDER BY d.date ASC, " + 
           "CASE d.mealType " + 
           "WHEN 'BREAKFAST' THEN 0 " + 
           "WHEN 'LUNCH' THEN 1 " + 
           "WHEN 'DINNER' THEN 2 " + 
           "END ASC")
    List<Diet> findByMemberAndDateBetweenOrderByDateAscMealTypeAsc(
            @Param("member") Member member, 
            @Param("startDate") LocalDate startDate, 
            @Param("endDate") LocalDate endDate);
}
