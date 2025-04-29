package myproject.spektif_agency_application.dto;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CardDTO {
    private Long id;
    private String title;
    private String description;
    private LocalDateTime deadline;
    private Long listId;
    private String listTitle;
    @Builder.Default
    private List<Long> assignedMemberIds = new ArrayList<>();
    @Builder.Default
    private List<AttachmentDTO> attachments = new ArrayList<>();
    @Builder.Default
    private List<ActivityDTO> activities = new ArrayList<>();
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
