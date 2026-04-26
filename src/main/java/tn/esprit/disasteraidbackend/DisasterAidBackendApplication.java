package tn.esprit.disasteraidbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class DisasterAidBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(DisasterAidBackendApplication.class, args);
    }
}
