package tn.esprit.disasteraidbackend.config;

import org.n52.jackson.datatype.jts.JtsModule;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

@Configuration
public class JacksonConfig {

    @Bean
    public ObjectMapper objectMapper(Jackson2ObjectMapperBuilder builder) {
        System.out.println(">>> JacksonConfig: registering JtsModule");
        ObjectMapper mapper = builder.createXmlMapper(false).build();
        mapper.registerModule(new JtsModule());
        return mapper;
    }
}