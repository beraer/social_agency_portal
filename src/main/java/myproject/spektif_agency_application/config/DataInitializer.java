package myproject.spektif_agency_application.config;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import myproject.spektif_agency_application.model.*;
import myproject.spektif_agency_application.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final BoardListRepository boardListRepository;
    private final CardRepository cardRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) {
        if (userRepository.count() == 0) {
            // Create users
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRole(Role.ADMIN);
            userRepository.save(admin);

            User employee = new User();
            employee.setUsername("employee");
            employee.setPassword(passwordEncoder.encode("emp123"));
            employee.setRole(Role.EMPLOYEE);
            userRepository.save(employee);

            User client = new User();
            client.setUsername("client");
            client.setPassword(passwordEncoder.encode("cli123"));
            client.setRole(Role.CLIENT);
            userRepository.save(client);

            BoardList tasarim = new BoardList();
            tasarim.setTitle("TASARIM");
            tasarim.setDescription("Design-related tasks");
            tasarim.setOwner(employee);
            boardListRepository.save(tasarim);

            BoardList yazar = new BoardList();
            yazar.setTitle("YAZAR");
            yazar.setDescription("Content planning");
            yazar.setOwner(employee);
            boardListRepository.save(yazar);

    
            Card card1 = new Card();
            card1.setTitle("Vox İncek 3'lü Feed Tasarımı");
            card1.setDescription("Feed tasarımı yapılacak");
            card1.setList(tasarim);
            card1.setAssignedMembers(Arrays.asList(employee));
            card1.setDeadline(LocalDateTime.now().plusDays(7));
            cardRepository.save(card1);

            Card card2 = new Card();
            card2.setTitle("Meydan Yapı Tek Post");
            card2.setDescription("Tek post tasarımı yapılacak");
            card2.setList(tasarim);
            card2.setAssignedMembers(Arrays.asList(employee));
            card2.setDeadline(LocalDateTime.now().plusDays(3));
            cardRepository.save(card2);

            Card card3 = new Card();
            card3.setTitle("Müft Unallar Mayıs İçerik Planı");
            card3.setDescription("Mayıs ayı içerik planı hazırlanacak");
            card3.setList(yazar);
            card3.setAssignedMembers(Arrays.asList(employee));
            card3.setDeadline(LocalDateTime.now().plusDays(14));
            cardRepository.save(card3);
        }
    }
} 