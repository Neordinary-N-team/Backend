package neordinary.backend.nteam.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EntityScan(basePackages = {"neordinary.backend.nteam.entity"})
@EnableJpaRepositories(basePackages = {"neordinary.backend.nteam.repository"})
public class TestConfig {
} 