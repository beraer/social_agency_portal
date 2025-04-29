package myproject.spektif_agency_application.service;

import myproject.spektif_agency_application.dto.CommentDTO;

public interface CommentService {
    CommentDTO addComment(Long cardId, String content, String username);
    void deleteComment(Long commentId);
} 