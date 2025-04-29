package myproject.spektif_agency_application.mapper;

import myproject.spektif_agency_application.dto.ActivityDTO;
import myproject.spektif_agency_application.dto.CommentDTO;
import myproject.spektif_agency_application.model.Comment;

public class CommentMapper {
    
    public static ActivityDTO toActivityDTO(Comment comment) {
        if (comment == null) return null;
        
        return ActivityDTO.builder()
                .id(comment.getId())
                .type("COMMENT")
                .cardId(comment.getCard() != null ? comment.getCard().getId() : null)
                .createdBy(comment.getUser() != null ? comment.getUser().getUsername() : null)
                .content(comment.getContent())
                .createdAt(comment.getCreatedAt())
                .build();
    }
    
    public static CommentDTO toDTO(Comment comment) {
        if (comment == null) return null;
        
        return CommentDTO.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .userId(comment.getUser() != null ? comment.getUser().getId() : null)
                .username(comment.getUser() != null ? comment.getUser().getUsername() : null)
                .cardId(comment.getCard() != null ? comment.getCard().getId() : null)
                .createdAt(comment.getCreatedAt())
                .build();
    }
    
    public static Comment toEntity(CommentDTO dto) {
        if (dto == null) return null;
        
        Comment comment = new Comment();
        comment.setId(dto.getId());
        comment.setContent(dto.getContent());
        comment.setCreatedAt(dto.getCreatedAt());
        
        return comment;
    }
} 