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
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/employee")
@RequiredArgsConstructor
public class EmployeeController {

    private final ProjectService projectService;
    private final UserService userService;
    private final CommentService commentService;

    @GetMapping("/dashboard")
    public String dashboard(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        Long userId = userService.findByUsername(userDetails.getUsername())
                .orElseThrow()
                .getId();
        List<ProjectDTO> projects = projectService.getProjectsByUserId(userId);
        model.addAttribute("projects", projects);
        return "employee/dashboard";
    }

    @PostMapping("/projects/{id}/status")
    public ResponseEntity<?> updateProjectStatus(@PathVariable Long id, @RequestBody Map<String, String> status) {
        try {
            ProjectDTO updated = projectService.updateProjectStatus(id, status.get("status"));
            return ResponseEntity.ok(updated);
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

    @PostMapping("/projects/{id}/upload")
    public ResponseEntity<?> uploadFile(@PathVariable Long id, @RequestParam("file") MultipartFile file,
                                      @RequestParam("fileType") String fileType) {
        try {
            String filePath = projectService.uploadFile(id, file, fileType);
            return ResponseEntity.ok(Map.of("filePath", filePath));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/projects/{id}/files")
    public String viewFiles(@PathVariable Long id, Model model) {
        model.addAttribute("files", projectService.getProjectFiles(id));
        return "employee/files";
    }
}
