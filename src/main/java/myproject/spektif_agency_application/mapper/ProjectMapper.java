package myproject.spektif_agency_application.mapper;

import myproject.spektif_agency_application.dto.ProjectDTO;
import myproject.spektif_agency_application.entity.*;

public class ProjectMapper {

    public static ProjectDTO toDTO(Project project) {
        return ProjectDTO.builder()
                .id(project.getId())
                .title(project.getTitle())
                .description(project.getDescription())
                .status(project.getStatus().name())
                .deliverablePath(project.getDeliverablePath())
                .assignedToUserId(project.getAssignedTo() != null ? project.getAssignedTo().getId() : null)
                .deadlineId(project.getDeadline() != null ? project.getDeadline().getId() : null)
                .build();
    }

    public static Project toEntity(ProjectDTO dto, User assignedUser, Deadline deadline) {
        return Project.builder()
                .id(dto.getId())
                .title(dto.getTitle())
                .description(dto.getDescription())
                .deliverablePath(dto.getDeliverablePath())
                .status(ProjectStatus.valueOf(dto.getStatus()))
                .assignedTo(assignedUser)
                .deadline(deadline)
                .build();
    }
}

