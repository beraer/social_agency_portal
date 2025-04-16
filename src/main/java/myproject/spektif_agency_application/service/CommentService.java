package myproject.spektif_agency_application.service;

import myproject.spektif_agency_application.dto.CommentDTO;

import java.util.List;

public interface CommentService {
    CommentDTO postComment(CommentDTO dto);
    List<CommentDTO> getCommentsByProject(Long projectId);
}
