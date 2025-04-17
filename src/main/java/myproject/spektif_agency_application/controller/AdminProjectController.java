package myproject.spektif_agency_application.controller;

import lombok.RequiredArgsConstructor;
import myproject.spektif_agency_application.dto.ProjectDTO;
import myproject.spektif_agency_application.service.ProjectService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/projects")
@RequiredArgsConstructor
public class AdminProjectController {

    private final ProjectService projectService;

    @GetMapping
    public List<ProjectDTO> getAllProjects() {
        return projectService.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjectDTO> getProjectById(@PathVariable Long id) {
        return projectService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/by-client/{clientId}")
    public List<ProjectDTO> getProjectsByClient(@PathVariable Long clientId) {
        return projectService.getProjectsByClientId(clientId);
    }

    @GetMapping(params = "status")
    public List<ProjectDTO> getProjectsByStatus(@RequestParam String status) {
        return projectService.getProjectsByStatus(status);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<ProjectDTO> updateStatus(
            @PathVariable Long id,
            @RequestParam String status
    ) {
        return ResponseEntity.ok(projectService.updateProjectStatus(id, status));
    }
}
