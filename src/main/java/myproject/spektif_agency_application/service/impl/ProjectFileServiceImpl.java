package myproject.spektif_agency_application.service.impl;

import lombok.RequiredArgsConstructor;
import myproject.spektif_agency_application.dto.ProjectFileDTO;
import myproject.spektif_agency_application.mapper.ProjectFileMapper;
import myproject.spektif_agency_application.model.Project;
import myproject.spektif_agency_application.model.ProjectFile;
import myproject.spektif_agency_application.repository.ProjectFileRepository;
import myproject.spektif_agency_application.repository.ProjectRepository;
import myproject.spektif_agency_application.service.ProjectFileService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProjectFileServiceImpl implements ProjectFileService {

    private final ProjectRepository projectRepo;
    private final ProjectFileRepository fileRepo;

    private final String UPLOAD_DIR = "uploads";

    @Override
    public String uploadFile(Long projectId, MultipartFile file, String fileType) throws IOException {
        Project project = projectRepo.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found"));

        Path uploadPath = Paths.get(UPLOAD_DIR);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        String originalFilename = file.getOriginalFilename();
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String newFilename = UUID.randomUUID().toString() + extension;

        Path filePath = uploadPath.resolve(newFilename);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        ProjectFile projectFile = new ProjectFile();
        projectFile.setProject(project);
        projectFile.setFileName(newFilename);
        projectFile.setOriginalFileName(originalFilename); // ✅ Now this is safely supported
        projectFile.setFileType(fileType);
        projectFile.setUploadDate(LocalDateTime.now());
        projectFile.setFilePath(filePath.toAbsolutePath().toString());

        fileRepo.save(projectFile);

        return newFilename;
    }


    @Override
    public List<ProjectFileDTO> getProjectFiles(Long projectId) {
        return fileRepo.findByProjectId(projectId)
                .stream()
                .map(ProjectFileMapper::toDTO)
                .collect(Collectors.toList());
    }
}
