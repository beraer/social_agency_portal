package myproject.spektif_agency_application.controller;

import lombok.RequiredArgsConstructor;
import myproject.spektif_agency_application.dto.ProjectDTO;
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
}
