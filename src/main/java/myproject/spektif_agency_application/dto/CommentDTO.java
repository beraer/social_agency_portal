package myproject.spektif_agency_application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentDTO {
    private Long id;
    private String content;
    private Long projectId;
    private Long authorId;
    private String authorName;
    private LocalDateTime createdAt;
}
