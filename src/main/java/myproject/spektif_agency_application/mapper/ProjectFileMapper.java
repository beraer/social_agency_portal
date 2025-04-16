package myproject.spektif_agency_application.mapper;

import myproject.spektif_agency_application.dto.ProjectFileDTO;
import myproject.spektif_agency_application.model.ProjectFile;

public class ProjectFileMapper {
    public static ProjectFileDTO toDTO(ProjectFile file) {
        return new ProjectFileDTO(
                file.getId(),
                file.getFileName(),
                file.getOriginalFileName(),
                file.getFileType(),
                file.getUploadDate(),
                file.getFilePath(),
                file.getProject() != null ? file.getProject().getId() : null
        );
    }
}
