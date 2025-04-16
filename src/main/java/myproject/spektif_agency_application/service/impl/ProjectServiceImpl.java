package myproject.spektif_agency_application.service.impl;

import lombok.RequiredArgsConstructor;
import myproject.spektif_agency_application.dto.ProjectDTO;
import myproject.spektif_agency_application.mapper.ProjectMapper;
import myproject.spektif_agency_application.model.Deadline;
import myproject.spektif_agency_application.model.Project;
import myproject.spektif_agency_application.model.ProjectStatus;
import myproject.spektif_agency_application.model.User;
import myproject.spektif_agency_application.repository.*;
import myproject.spektif_agency_application.service.ProjectService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepo;
    private final UserRepository userRepo;
    private final DeadlineRepository deadlineRepo;

    @Override
    public ProjectDTO createProject(ProjectDTO dto) {
        User user = userRepo.findById(dto.getAssignedToUserId()).orElseThrow();
        Deadline deadline = deadlineRepo.findById(dto.getDeadlineId()).orElseThrow();
        Project entity = ProjectMapper.toEntity(dto, user, deadline);
        return ProjectMapper.toDTO(projectRepo.save(entity));
    }

    @Override
    public ProjectDTO updateProjectStatus(Long projectId, String status) {
        Project project = projectRepo.findById(projectId).orElseThrow();
        project.setStatus(ProjectStatus.valueOf(status));
        return ProjectMapper.toDTO(projectRepo.save(project));
    }

    @Override
    public List<ProjectDTO> getProjectsByUserId(Long userId) {
        return projectRepo.findByAssignedToId(userId)
                .stream()
                .map(ProjectMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProjectDTO> getProjectsByDeadlineId(Long deadlineId) {
        return projectRepo.findByDeadlineId(deadlineId)
                .stream()
                .map(ProjectMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProjectDTO> getProjectsByStatus(String status) {
        return projectRepo.findByStatus(ProjectStatus.valueOf(status))
                .stream()
                .map(ProjectMapper::toDTO)
                .collect(Collectors.toList());
    }
}
