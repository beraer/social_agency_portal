package myproject.spektif_agency_application.dto;

import lombok.Data;

@Data
public class CommentDTO {
    private Long id;
    private Long projectId;
    private Long authorId;
    private String content;
}
