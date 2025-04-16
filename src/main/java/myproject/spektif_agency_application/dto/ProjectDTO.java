package myproject.spektif_agency_application.dto;

import lombok.Data;
import myproject.spektif_agency_application.entity.Deadline;

@Data
public class ProjectDTO {
    private Long id;
    private String title;
    private String description;
    private String status;
    private String deliverablePath;
    private Deadline deadline;
}
