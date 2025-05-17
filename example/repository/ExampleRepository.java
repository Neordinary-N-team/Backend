package neordinary.backend.nteam.example.repository;

import neordinary.backend.nteam.example.entity.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExampleRepository extends JpaRepository<Example, Long> {
} 