package neordinary.backend.nteam.repository;

import neordinary.backend.nteam.entity.Diary;
import neordinary.backend.nteam.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DiaryRepository extends JpaRepository<Diary, Long> {
    List<Diary> findByMember(Member member);
}
