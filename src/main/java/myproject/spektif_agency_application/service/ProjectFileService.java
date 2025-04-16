package myproject.spektif_agency_application.service;

import myproject.spektif_agency_application.dto.ProjectFileDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ProjectFileService {
    String uploadFile(Long projectId, MultipartFile file, String fileType) throws IOException;
    List<ProjectFileDTO> getProjectFiles(Long projectId);
}
