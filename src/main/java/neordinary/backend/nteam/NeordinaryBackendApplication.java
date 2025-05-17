package neordinary.backend.nteam;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

//@OpenAPIDefinition(servers = {@Server(url = "https://neohack.duckdns.org", description = "도메인 설명")})
@SpringBootApplication
@EnableJpaAuditing
@EnableScheduling
public class NeordinaryBackendApplication {
    public static void main(String[] args) {
        SpringApplication.run(NeordinaryBackendApplication.class, args);
    }
}
