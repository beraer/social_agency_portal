package myproject.spektif_agency_application.mapper;

import myproject.spektif_agency_application.dto.DeadlineDTO;
import myproject.spektif_agency_application.model.Deadline;
import myproject.spektif_agency_application.model.User;

public class DeadlineMapper {

    public static DeadlineDTO toDTO(Deadline deadline) {
        return DeadlineDTO.builder()
                .id(deadline.getId())
                .title(deadline.getTitle())
                .description(deadline.getDescription())
                .dueDate(deadline.getDueDate())
                .assignedToUserId(deadline.getAssignedTo() != null ? deadline.getAssignedTo().getId() : null)
                .build();
    }

    public static Deadline toEntity(DeadlineDTO dto, User assignedUser) {
        Deadline deadline = new Deadline();
        deadline.setId(dto.getId());
        deadline.setTitle(dto.getTitle());
        deadline.setDescription(dto.getDescription());
        deadline.setDueDate(dto.getDueDate());
        deadline.setAssignedTo(assignedUser);
        return deadline;
    }
}
