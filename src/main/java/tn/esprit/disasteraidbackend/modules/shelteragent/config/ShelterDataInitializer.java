package tn.esprit.disasteraidbackend.modules.shelteragent.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
public class ShelterDataInitializer {

    private static final Logger log = LoggerFactory.getLogger(ShelterDataInitializer.class);

    @Value("${app.init-data.enabled:false}")
    private boolean enabled;

    @Bean
    public CommandLineRunner initShelterData(JdbcTemplate jdbcTemplate) {
        return args -> {
            if (!enabled) {
                log.info("Data initialization is disabled.");
                return;
            }

            log.info("Starting shelter data initialization...");

            // 1. Ensure at least one manager exists for shelter seed rows.
            // Note: Assuming 'users' table exists as per V3__seed_data.sql
            jdbcTemplate.execute("""
                INSERT INTO users (name)
                SELECT 'System Manager'
                WHERE NOT EXISTS (SELECT 1 FROM users)
                """);

            // 2. Insert shelters into shelteragent table
            jdbcTemplate.execute("""
                INSERT INTO shelteragent (
                    name,
                    location_url,
                    address,
                    capacity,
                    current_occupancy,
                    manager_id,
                    status,
                    city,
                    latitude,
                    longitude,
                    shelter_type,
                    has_accessibility,
                    accepts_pets,
                    phone,
                    opened_at
                )
                SELECT
                    s.name,
                    s.location_url,
                    s.address,
                    s.capacity,
                    s.current_occupancy,
                    (SELECT user_id FROM users ORDER BY user_id LIMIT 1),
                    s.status,
                    s.city,
                    s.latitude,
                    s.longitude,
                    s.shelter_type,
                    s.has_accessibility,
                    s.accepts_pets,
                    s.phone,
                    NOW()
                FROM (
                    VALUES
                    ('Tunis Central School Shelter', 'https://maps.google.com/?q=36.8065,10.1815', '10 Avenue Habib Bourguiba', 120, 83, 'OPEN', 'Tunis', 36.8065, 10.1815, 'LOCAL', TRUE, FALSE, '+21670010001'),
                    ('Bab Saadoun Medical Center Shelter', 'https://maps.google.com/?q=36.8108,10.1597', '4 Rue de la Sante', 90, 62, 'OPEN', 'Tunis', 36.8108, 10.1597, 'MEDICAL', TRUE, FALSE, '+21670010002'),
                    ('Lafayette Community Hall', 'https://maps.google.com/?q=36.8091,10.1723', '22 Rue de Marseille', 80, 80, 'FULL', 'Tunis', 36.8091, 10.1723, 'LOCAL', TRUE, TRUE, '+21670010003'),
                    ('Charles Nicolle Emergency Shelter', 'https://maps.google.com/?q=36.8160,10.1519', '1 Rue Charles Nicolle', 140, 99, 'OPEN', 'Tunis', 36.8160, 10.1519, 'MEDICAL', TRUE, FALSE, '+21670010004'),
                    ('Bardo Sports Complex Shelter', 'https://maps.google.com/?q=36.8098,10.1330', '15 Avenue de l Environnement', 160, 120, 'OPEN', 'Manouba', 36.8098, 10.1330, 'LOCAL', TRUE, TRUE, '+21670010005'),
                    ('Ariana Civil Protection Hub', 'https://maps.google.com/?q=36.8625,10.1956', '8 Rue de l Independence', 110, 74, 'OPEN', 'Ariana', 36.8625, 10.1956, 'LOCAL', TRUE, TRUE, '+21670010006'),
                    ('Ariana Regional Medical Shelter', 'https://maps.google.com/?q=36.8661,10.2011', '2 Avenue des Medecins', 70, 70, 'FULL', 'Ariana', 36.8661, 10.2011, 'MEDICAL', TRUE, FALSE, '+21670010007'),
                    ('La Marsa Youth Center Shelter', 'https://maps.google.com/?q=36.8787,10.3247', '31 Rue de La Marsa', 100, 45, 'OPEN', 'La Marsa', 36.8787, 10.3247, 'LOCAL', FALSE, TRUE, '+21670010008'),
                    ('La Goulette Port Hall Shelter', 'https://maps.google.com/?q=36.8184,10.3050', '5 Rue du Port', 90, 20, 'CLOSED', 'La Goulette', 36.8184, 10.3050, 'LOCAL', FALSE, FALSE, '+21670010009'),
                    ('Ben Arous Medical Relief Shelter', 'https://maps.google.com/?q=36.7532,10.2189', '14 Avenue de la Republique', 130, 101, 'OPEN', 'Ben Arous', 36.7532, 10.2189, 'MEDICAL', TRUE, FALSE, '+21670010010'),
                    ('Rades Municipal Shelter', 'https://maps.google.com/?q=36.7687,10.2718', '7 Rue des Orangers', 95, 95, 'FULL', 'Ben Arous', 36.7687, 10.2718, 'LOCAL', TRUE, TRUE, '+21670010011'),
                    ('Ezzahra Public School Shelter', 'https://maps.google.com/?q=36.7419,10.3074', '13 Rue de l Unite', 85, 40, 'OPEN', 'Ben Arous', 36.7419, 10.3074, 'LOCAL', FALSE, TRUE, '+21670010012'),
                    ('Menzah Clinic Shelter', 'https://maps.google.com/?q=36.8489,10.1758', '19 Avenue de la Liberte', 60, 10, 'CLOSED', 'Ariana', 36.8489, 10.1758, 'MEDICAL', TRUE, FALSE, '+21670010013'),
                    ('Carthage Cultural Center Shelter', 'https://maps.google.com/?q=36.8529,10.3230', '2 Route de Carthage', 150, 112, 'OPEN', 'Carthage', 36.8529, 10.3230, 'LOCAL', TRUE, TRUE, '+21670010014'),
                    ('Sidi Bou Said School Shelter', 'https://maps.google.com/?q=36.8707,10.3415', '11 Rue des Jasmins', 75, 52, 'OPEN', 'Sidi Bou Said', 36.8707, 10.3415, 'LOCAL', FALSE, FALSE, '+21670010015')
                ) AS s(name, location_url, address, capacity, current_occupancy, status, city, latitude, longitude, shelter_type, has_accessibility, accepts_pets, phone)
                WHERE NOT EXISTS (SELECT 1 FROM shelteragent x WHERE x.name = s.name)
                """);

            // 3. Seed reviews idempotently
            jdbcTemplate.execute("""
                INSERT INTO shelter_reviews (shelter_agent_id, rating, comment, author_name)
                SELECT sh.shelter_agent_id, rv.rating, rv.comment, rv.author_name
                FROM (
                    VALUES
                    ('Tunis Central School Shelter', 4, 'Staff were organized and responsive.', 'Nadia'),
                    ('Tunis Central School Shelter', 5, 'Clean place and quick check-in.', 'Karim'),
                    ('Bab Saadoun Medical Center Shelter', 5, 'Medical team helped quickly.', 'Hatem'),
                    ('Charles Nicolle Emergency Shelter', 5, 'Doctors available on site.', 'Rim'),
                    ('Ariana Regional Medical Shelter', 3, 'No capacity left.', 'Amir'),
                    ('La Marsa Youth Center Shelter', 4, 'Accepted pets and clear guidance.', 'Hela'),
                    ('Ben Arous Medical Relief Shelter', 5, 'Great medical support.', 'Ines'),
                    ('Rades Municipal Shelter', 2, 'No room left when we arrived.', 'Rania'),
                    ('Carthage Cultural Center Shelter', 5, 'Well-managed shelter.', 'Radhia'),
                    ('Sidi Bou Said School Shelter', 3, 'Basic but acceptable.', 'Dorra')
                ) AS rv(shelter_name, rating, comment, author_name)
                JOIN shelteragent sh ON sh.name = rv.shelter_name
                WHERE NOT EXISTS (
                    SELECT 1
                    FROM shelter_reviews r
                    WHERE r.shelter_agent_id = sh.shelter_agent_id
                      AND r.author_name = rv.author_name
                      AND r.comment = rv.comment
                )
                """);

            log.info("Shelter data initialization completed.");
        };
    }
}
