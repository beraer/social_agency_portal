package myproject.spektif_agency_application.repository;

import myproject.spektif_agency_application.model.Project;
import myproject.spektif_agency_application.model.ProjectStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    List<Project> findByAssignedToId(Long userId);
    List<Project> findByStatus(ProjectStatus status);
    List<Project> findByDeadlineId(Long deadlineId);
}
