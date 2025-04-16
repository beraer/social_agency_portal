package myproject.spektif_agency_application.service;

import myproject.spektif_agency_application.dto.CommentDTO;
import java.util.List;

public interface CommentService {
    CommentDTO addComment(CommentDTO dto);
    List<CommentDTO> getCommentsByProjectId(Long projectId);
    List<CommentDTO> getCommentsByUserId(Long userId);
    void deleteComment(Long commentId, Long userId);
}
