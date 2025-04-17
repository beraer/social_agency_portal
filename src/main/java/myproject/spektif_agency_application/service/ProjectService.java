package myproject.spektif_agency_application.service;

import myproject.spektif_agency_application.dto.ProjectDTO;

import java.util.List;
import java.util.Optional;

public interface ProjectService {

    Optional<ProjectDTO> findById(Long id);

    List<ProjectDTO> getAll();

    List<ProjectDTO> getProjectsByClientId(Long clientId);

    List<ProjectDTO> getProjectsByStatus(String status);

    ProjectDTO updateProjectStatus(Long projectId, String status);
}
