package myproject.spektif_agency_application.service;

import myproject.spektif_agency_application.dto.ProjectDTO;

import java.util.List;

public interface ProjectService {
    ProjectDTO createProject(ProjectDTO dto);
    ProjectDTO updateProjectStatus(Long projectId, String status);
    List<ProjectDTO> getProjectsByUserId(Long userId);
    List<ProjectDTO> getProjectsByDeadlineId(Long deadlineId);
    List<ProjectDTO> getProjectsByStatus(String status);
}
