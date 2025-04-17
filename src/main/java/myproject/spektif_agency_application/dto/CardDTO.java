package myproject.spektif_agency_application.dto;

import lombok.*;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CardDTO {
    private Long id;
    private String title;
    private String description;
    private LocalDate dueDate;
    private boolean isProject;
    private Long boardListId;
    private ProjectDTO projectDetails;
    private List<CardAttachmentDTO> attachments;
}
