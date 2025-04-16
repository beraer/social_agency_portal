package myproject.spektif_agency_application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import myproject.spektif_agency_application.entity.Deadline;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectDTO {
    private Long id;
    private String title;
    private String description;
    private String status;
    private String deliverablePath;
    private Long assignedToUserId;
    private Long deadlineId;
}
