package myproject.spektif_agency_application.repository;

import myproject.spektif_agency_application.model.ProjectFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProjectFileRepository extends JpaRepository<ProjectFile, Long> {
    List<ProjectFile> findByProjectId(Long projectId);
    void deleteByProjectId(Long projectId);
} 