package myproject.spektif_agency_application.service.impl;

import lombok.RequiredArgsConstructor;
import myproject.spektif_agency_application.dto.ProjectDTO;
import myproject.spektif_agency_application.dto.ProjectFileDTO;
import myproject.spektif_agency_application.mapper.ProjectMapper;
import myproject.spektif_agency_application.model.Project;
import myproject.spektif_agency_application.model.ProjectStatus;
import myproject.spektif_agency_application.repository.*;
import myproject.spektif_agency_application.service.ProjectService;
import myproject.spektif_agency_application.util.ProjectStatusUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepo;

    @Override
    public ProjectDTO updateProjectStatus(Long projectId, String status) {
        Project project = projectRepo.findById(projectId).orElseThrow();

        project.setStatus(ProjectStatusUtils.parse(status));
        return ProjectMapper.toDTO(projectRepo.save(project));
    }


    @Override
    public Optional<ProjectDTO> findById(Long id) {
        return projectRepo.findById(id).map(ProjectMapper::toDTO);
    }

    @Override
    public List<ProjectDTO> getProjectsByClientId(Long clientId) {
        return projectRepo.findByClientId(clientId)
                .stream()
                .map(ProjectMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProjectDTO> getProjectsByStatus(String status) {
        ProjectStatus parsedStatus = ProjectStatusUtils.parse(status);
        return projectRepo.findByStatus(parsedStatus)
                .stream()
                .map(ProjectMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public String uploadFile(Long projectId, MultipartFile file, String fileType) throws IOException {
        return "";
    }

    @Override
    public List<ProjectFileDTO> getProjectFiles(Long projectId) {
        return List.of();
    }

}
