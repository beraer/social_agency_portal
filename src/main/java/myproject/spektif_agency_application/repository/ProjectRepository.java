package myproject.spektif_agency_application.repository;

import myproject.spektif_agency_application.model.Project;
import myproject.spektif_agency_application.model.ProjectStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    List<Project> findByAssignedToId(Long userId);
    List<Project> findByClientId(Long clientId);
    List<Project> findByStatus(ProjectStatus status);
    List<Project> findByDeadlineId(Long deadlineId);
}
