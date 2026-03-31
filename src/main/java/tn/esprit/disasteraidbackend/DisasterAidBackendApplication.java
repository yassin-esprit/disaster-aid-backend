package tn.esprit.disasteraidbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "tn.esprit.disasteraidbackend")
public class DisasterAidBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(DisasterAidBackendApplication.class, args);
    }

}
