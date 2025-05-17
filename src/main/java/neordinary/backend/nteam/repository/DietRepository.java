package neordinary.backend.nteam.repository;

import neordinary.backend.nteam.entity.Diet;
import neordinary.backend.nteam.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface DietRepository extends JpaRepository<Diet, Long> {
    List<Diet> findByMember(Member member);

    List<Diet> findByMemberIdAndDate(UUID memberId, LocalDate date);

    List<Diet> findAllByMemberIdAndDate(UUID memberId, LocalDate date);

}
