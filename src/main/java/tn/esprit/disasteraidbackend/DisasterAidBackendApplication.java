package tn.esprit.disasteraidbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableCaching
public class DisasterAidBackendApplication {
    public static void main(String[] args) {
        DatabaseBootstrap.ensureDatabaseExists();
        SpringApplication.run(DisasterAidBackendApplication.class, args);
    }

}
