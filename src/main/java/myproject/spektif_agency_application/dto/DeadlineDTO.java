package myproject.spektif_agency_application.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class DeadlineDTO {
    private Long id;
    private String title;
    private LocalDate dueDate;
    private Long assignedEmployeeId;
}
