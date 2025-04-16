package myproject.spektif_agency_application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeadlineDTO {
    private Long id;
    private String title;
    private String description;
    private LocalDate dueDate;
    private Long assignedToUserId;
}
