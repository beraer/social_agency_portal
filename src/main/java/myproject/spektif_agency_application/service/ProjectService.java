package myproject.spektif_agency_application.service;

import myproject.spektif_agency_application.dto.ProjectDTO;
import myproject.spektif_agency_application.dto.ProjectFileDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface ProjectService {
    ProjectDTO createProject(ProjectDTO dto);
    ProjectDTO updateProjectStatus(Long projectId, String status);
    Optional<ProjectDTO> findById(Long id);
    List<ProjectDTO> getProjectsByUserId(Long userId);
    List<ProjectDTO> getProjectsByClientId(Long clientId);
    List<ProjectDTO> getProjectsByDeadlineId(Long deadlineId);
    List<ProjectDTO> getProjectsByStatus(String status);
    String uploadFile(Long projectId, MultipartFile file, String fileType) throws IOException;
    List<ProjectFileDTO> getProjectFiles(Long projectId);
}
