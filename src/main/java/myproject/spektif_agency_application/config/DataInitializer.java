package myproject.spektif_agency_application.config;

import lombok.RequiredArgsConstructor;
import myproject.spektif_agency_application.model.*;
import myproject.spektif_agency_application.repository.DeadlineRepository;
import myproject.spektif_agency_application.repository.ProjectRepository;
import myproject.spektif_agency_application.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.Set;

@Configuration
@RequiredArgsConstructor
public class DataInitializer {

    private final PasswordEncoder passwordEncoder;

    @Bean
    CommandLineRunner initUsers(UserRepository userRepository, PasswordEncoder encoder) {
        return args -> {
            if (userRepository.findByUsername("admin").isEmpty()) {
                userRepository.save(new User(null, "admin", encoder.encode("admin123"), Role.ADMIN));
            }

            if (userRepository.findByUsername("employee").isEmpty()) {
                userRepository.save(new User(null, "employee", encoder.encode("emp123"), Role.EMPLOYEE));
            }

            if (userRepository.findByUsername("client").isEmpty()) {
                userRepository.save(new User(null, "client", encoder.encode("cli123"), Role.CLIENT));
            }
        };
    }
} 