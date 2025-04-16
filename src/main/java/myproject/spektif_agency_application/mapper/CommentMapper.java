package myproject.spektif_agency_application.mapper;

import myproject.spektif_agency_application.dto.CommentDTO;
import myproject.spektif_agency_application.entity.*;

public class CommentMapper {

    public static CommentDTO toDTO(Comment comment) {
        return CommentDTO.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .createdAt(comment.getCreatedAt())
                .authorId(comment.getAuthor() != null ? comment.getAuthor().getId() : null)
                .projectId(comment.getProject() != null ? comment.getProject().getId() : null)
                .build();
    }

    public static Comment toEntity(CommentDTO dto, User author, Project project) {
        return Comment.builder()
                .id(dto.getId())
                .content(dto.getContent())
                .createdAt(dto.getCreatedAt())
                .author(author)
                .project(project)
                .build();
    }
}
