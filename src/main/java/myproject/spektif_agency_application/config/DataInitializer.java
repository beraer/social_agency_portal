package myproject.spektif_agency_application.config;

import lombok.RequiredArgsConstructor;
import myproject.spektif_agency_application.model.*;
import myproject.spektif_agency_application.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Configuration
@RequiredArgsConstructor
public class DataInitializer {

    private final PasswordEncoder passwordEncoder;


    @Bean
    CommandLineRunner initUsers(UserRepository userRepository,
                                PasswordEncoder encoder,
                                CardRepository cardRepository,
                                BoardListRepository boardListRepository,
                                DeadlineRepository deadlineRepository,
                                ProjectRepository projectRepository) {
        return args -> {

            User admin = userRepository.findByUsername("admin")
                    .orElseGet(() -> userRepository.save(new User(null, "admin", encoder.encode("admin123"), Role.ADMIN)));

            User emp = userRepository.findByUsername("employee")
                    .orElseGet(() -> userRepository.save(new User(null, "employee", encoder.encode("emp123"), Role.EMPLOYEE)));

            User client = userRepository.findByUsername("client")
                    .orElseGet(() -> userRepository.save(new User(null, "client", encoder.encode("cli123"), Role.CLIENT)));

            // Seed board lists & cards if not already present
            if (boardListRepository.count() == 0) {

                // Board List: TASARIM
                BoardList tasarim = new BoardList();
                tasarim.setTitle("TASARIM");
                tasarim.setDescription("Design-related tasks");
                tasarim.setOwner(emp);
                boardListRepository.save(tasarim);

                // Board List: YAZAR
                BoardList yazar = new BoardList();
                yazar.setTitle("YAZAR");
                yazar.setDescription("Content planning");
                yazar.setOwner(emp);
                boardListRepository.save(yazar);

                // Card 1
                Card card1 = new Card();
                card1.setTitle("Vox İncek 3'lü Feed Tasarımı");
                card1.setBoardList(tasarim);
                card1.setAssignedMembers(List.of(emp));
                cardRepository.save(card1);

                // Card 2
                Card card2 = new Card();
                card2.setTitle("Meydan Yapı Tek Post");
                card2.setBoardList(tasarim);
                card2.setAssignedMembers(List.of(emp));
                cardRepository.save(card2);

                // Card 3
                Card card3 = new Card();
                card3.setTitle("Muft Unallar Mayıs İçerik Planı");
                card3.setBoardList(yazar);
                card3.setAssignedMembers(List.of(emp));
                cardRepository.save(card3);
            }
        };
    }

} 