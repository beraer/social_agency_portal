package myproject.spektif_agency_application.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import myproject.spektif_agency_application.dto.ProjectDTO;
import myproject.spektif_agency_application.mapper.ProjectMapper;
import myproject.spektif_agency_application.model.Project;
import myproject.spektif_agency_application.model.ProjectStatus;
import myproject.spektif_agency_application.repository.ProjectRepository;
import myproject.spektif_agency_application.service.ProjectService;
import myproject.spektif_agency_application.util.ProjectStatusUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;

    @Override
    @Transactional
    public Optional<ProjectDTO> findById(Long id) {
        return projectRepository.findById(id)
                .map(ProjectMapper::toDTO);
    }

    @Override
    @Transactional
    public List<ProjectDTO> getAll() {
        return projectRepository.findAll().stream()
                .map(ProjectMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public List<ProjectDTO> getProjectsByClientId(Long clientId) {
        return projectRepository.findByClientId(clientId).stream()
                .map(ProjectMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public List<ProjectDTO> getProjectsByStatus(String status) {
        ProjectStatus projectStatus = ProjectStatusUtils.parse(status);
        return projectRepository.findByStatus(projectStatus).stream()
                .map(ProjectMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ProjectDTO updateProjectStatus(Long projectId, String status) {
        Project project = projectRepository.findById(projectId).orElseThrow();
        project.setStatus(ProjectStatusUtils.parse(status));
        return ProjectMapper.toDTO(projectRepository.save(project));
    }
}
