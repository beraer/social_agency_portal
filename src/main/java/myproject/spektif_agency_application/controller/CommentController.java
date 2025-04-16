package myproject.spektif_agency_application.controller;

import lombok.RequiredArgsConstructor;
import myproject.spektif_agency_application.dto.CommentDTO;
import myproject.spektif_agency_application.service.CommentService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<CommentDTO> addComment(
            @RequestBody Map<String, String> payload,
            @AuthenticationPrincipal UserDetails userDetails) {
        Long projectId = Long.parseLong(payload.get("projectId"));
        String content = payload.get("content");
        Long userId = Long.parseLong(userDetails.getUsername());
        
        CommentDTO dto = new CommentDTO();
        dto.setProjectId(projectId);
        dto.setContent(content);
        dto.setAuthorId(userId);
        
        CommentDTO saved = commentService.addComment(dto);
        return ResponseEntity.ok(saved);
    }

    @GetMapping("/project/{projectId}")
    public ResponseEntity<List<CommentDTO>> getProjectComments(@PathVariable Long projectId) {
        List<CommentDTO> comments = commentService.getCommentsByProjectId(projectId);
        return ResponseEntity.ok(comments);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<CommentDTO>> getUserComments(@PathVariable Long userId) {
        List<CommentDTO> comments = commentService.getCommentsByUserId(userId);
        return ResponseEntity.ok(comments);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(
            @PathVariable Long commentId,
            @AuthenticationPrincipal UserDetails userDetails) {
        Long userId = Long.parseLong(userDetails.getUsername());
        commentService.deleteComment(commentId, userId);
        return ResponseEntity.ok().build();
    }
}
