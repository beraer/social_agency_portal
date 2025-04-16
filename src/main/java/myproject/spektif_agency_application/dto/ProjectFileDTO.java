package myproject.spektif_agency_application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectFileDTO {
    private Long id;
    private String fileName;
    private String originalFileName;
    private String fileType;
    private LocalDateTime uploadDate;
    private String filePath;
    private Long projectId;
}
