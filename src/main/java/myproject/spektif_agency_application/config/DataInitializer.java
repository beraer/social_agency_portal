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
    public CommandLineRunner initData(UserRepository userRepository, ProjectRepository projectRepository, DeadlineRepository deadlineRepository) {
        return args -> {
            // Create admin
            User admin = User.builder()
                    .username("admin")
                    .password(passwordEncoder.encode("admin123"))
                    .role(Role.ADMIN)
                    .build();

            // Create employees
            User employee1 = User.builder()
                    .username("employee1")
                    .password(passwordEncoder.encode("emp123"))
                    .role(Role.EMPLOYEE)
                    .build();

            User employee2 = User.builder()
                    .username("employee2")
                    .password(passwordEncoder.encode("emp123"))
                    .role(Role.EMPLOYEE)
                    .build();

            // Create clients
            User client1 = User.builder()
                    .username("client1")
                    .password(passwordEncoder.encode("client123"))
                    .role(Role.CLIENT)
                    .build();

            User client2 = User.builder()
                    .username("client2")
                    .password(passwordEncoder.encode("client123"))
                    .role(Role.CLIENT)
                    .build();

            // Save users
            userRepository.saveAll(Set.of(admin, employee1, employee2, client1, client2));

            // Create deadlines
            Deadline deadline1 = Deadline.builder()
                    .title("Social Media Campaign Deadline")
                    .description("Deadline for Instagram and Facebook content")
                    .dueDate(LocalDate.now().plusDays(7))
                    .assignedTo(employee1)
                    .build();

            Deadline deadline2 = Deadline.builder()
                    .title("Video Production Deadline")
                    .description("Deadline for promotional video")
                    .dueDate(LocalDate.now().plusDays(14))
                    .assignedTo(employee2)
                    .build();

            // Save deadlines
            deadlineRepository.saveAll(Set.of(deadline1, deadline2));

            // Create projects
            Project project1 = Project.builder()
                    .title("Social Media Campaign")
                    .description("Create content for Instagram and Facebook")
                    .status(ProjectStatus.ON_PROGRESS)
                    .assignedTo(employee1)
                    .client(client1)
                    .deadline(deadline1)
                    .build();

            Project project2 = Project.builder()
                    .title("Video Production")
                    .description("Create promotional video for new product")
                    .status(ProjectStatus.ON_PROGRESS)
                    .assignedTo(employee2)
                    .client(client2)
                    .deadline(deadline2)
                    .build();

            // Save projects
            projectRepository.saveAll(Set.of(project1, project2));
        };
    }
} 