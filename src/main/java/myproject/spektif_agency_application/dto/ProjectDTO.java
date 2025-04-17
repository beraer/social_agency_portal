package myproject.spektif_agency_application.dto;

import lombok.*;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectDTO {
    private Long id;
    private String name;
    private LocalDate startDate;
    private LocalDate endDate;
    private String status;
    private Long clientId;
    private Long cardId;
}
