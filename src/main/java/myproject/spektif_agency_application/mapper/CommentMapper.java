package myproject.spektif_agency_application.mapper;

import myproject.spektif_agency_application.dto.CommentDTO;
import myproject.spektif_agency_application.model.Comment;
import myproject.spektif_agency_application.model.Project;
import myproject.spektif_agency_application.model.User;

public class CommentMapper {

    public static CommentDTO toDTO(Comment comment) {
        return new CommentDTO(
            comment.getId(),
            comment.getContent(),
            comment.getProject().getId(),
            comment.getUser().getId(),
            comment.getUser().getUsername(),
            comment.getCreatedAt()
        );
    }

    public static Comment toEntity(CommentDTO dto, User author, Project project) {
        Comment comment = new Comment();
        comment.setContent(dto.getContent());
        comment.setProject(project);
        comment.setUser(author);
        comment.setCreatedAt(dto.getCreatedAt() != null ? dto.getCreatedAt() : java.time.LocalDateTime.now());
        return comment;
    }
}
