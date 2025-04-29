package myproject.spektif_agency_application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ActivityDTO {
    private Long id;
    
    @NotNull(message = "Card ID is required")
    private Long cardId;
    
    @NotBlank(message = "Content is required")
    private String content;
    
    @NotBlank(message = "Activity type is required")
    private String type;
    
    @NotBlank(message = "Creator is required")
    private String createdBy;
    
    private LocalDateTime createdAt;
} 