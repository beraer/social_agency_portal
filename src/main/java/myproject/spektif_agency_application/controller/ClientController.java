package myproject.spektif_agency_application.controller;

import lombok.RequiredArgsConstructor;
import myproject.spektif_agency_application.dto.CommentDTO;
import myproject.spektif_agency_application.dto.ProjectDTO;
import myproject.spektif_agency_application.service.CommentService;
import myproject.spektif_agency_application.service.ProjectService;
import myproject.spektif_agency_application.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/client")
@RequiredArgsConstructor
public class ClientController {

    private final ProjectService projectService;
    private final UserService userService;
    private final CommentService commentService;

    @GetMapping("/dashboard")
    public String dashboard(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        Long userId = userService.findByUsername(userDetails.getUsername())
                .orElseThrow()
                .getId();
        List<ProjectDTO> projects = projectService.getProjectsByClientId(userId);
        model.addAttribute("projects", projects);
        return "client/dashboard";
    }

    @GetMapping("/projects/{id}")
    public ResponseEntity<?> getProject(@PathVariable Long id) {
        try {
            ProjectDTO project = projectService.findById(id)
                    .orElseThrow(() -> new RuntimeException("Project not found"));
            return ResponseEntity.ok(project);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/projects/{id}/comments")
    public ResponseEntity<?> addComment(@PathVariable Long id, @RequestBody CommentDTO commentDTO,
                                      @AuthenticationPrincipal UserDetails userDetails) {
        try {
            Long userId = userService.findByUsername(userDetails.getUsername())
                    .orElseThrow()
                    .getId();
            commentDTO.setProjectId(id);
            commentDTO.setAuthorId(userId);
            CommentDTO saved = commentService.addComment(commentDTO);
            return ResponseEntity.ok(saved);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/projects/{id}/files")
    public ResponseEntity<?> getProjectFiles(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(projectService.getProjectFiles(id));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
