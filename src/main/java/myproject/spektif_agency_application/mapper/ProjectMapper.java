package myproject.spektif_agency_application.mapper;

import myproject.spektif_agency_application.dto.ProjectDTO;
import myproject.spektif_agency_application.model.Project;
import myproject.spektif_agency_application.model.ProjectStatus;
import myproject.spektif_agency_application.model.User;

public class ProjectMapper {

    public static ProjectDTO toDTO(Project entity) {
        return ProjectDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .startDate(entity.getStartDate())
                .endDate(entity.getEndDate())
                .status(entity.getStatus().name())
                .clientId(entity.getClient() != null ? entity.getClient().getId() : null)
                .cardId(entity.getCard() != null ? entity.getCard().getId() : null)
                .build();
    }

    public static Project toEntity(ProjectDTO dto) {
        return Project.builder()
                .id(dto.getId())
                .name(dto.getName())
                .startDate(dto.getStartDate())
                .endDate(dto.getEndDate())
                .status(ProjectStatus.valueOf(dto.getStatus()))
                .build();
    }

    public static Project toEntity(ProjectDTO dto, User client) {
        return Project.builder()
                .id(dto.getId())
                .name(dto.getName())
                .startDate(dto.getStartDate())
                .endDate(dto.getEndDate())
                .status(ProjectStatus.valueOf(dto.getStatus()))
                .client(client)
                .build();
    }
}
