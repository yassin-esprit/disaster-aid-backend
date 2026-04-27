package tn.esprit.disasteraidbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DisasterAidBackendApplication {

    public static void main(String[] args) {
        DatabaseBootstrap.ensureDatabaseExists();
        SpringApplication.run(DisasterAidBackendApplication.class, args);
    }

}
