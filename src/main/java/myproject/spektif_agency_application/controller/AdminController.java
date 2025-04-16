package myproject.spektif_agency_application.controller;

import lombok.RequiredArgsConstructor;
import myproject.spektif_agency_application.dto.*;
import myproject.spektif_agency_application.model.Role;
import myproject.spektif_agency_application.service.*;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {

    private final DeadlineService deadlineService;
    private final ProjectService projectService;
    private final UserService userService;

    @GetMapping("/dashboard")
    public String adminDashboard(Model model) {
        List<DeadlineDTO> deadlines = deadlineService.getAllDeadlines();
        List<ProjectDTO> completedProjects = projectService.getProjectsByStatus("COMPLETED");
        List<UserDTO> employees = userService.getAllUsers();

        model.addAttribute("deadlines", deadlines);
        model.addAttribute("completedProjects", completedProjects);
        model.addAttribute("employees", employees);

        return "admin/dashboard";
    }

    @GetMapping("/deadlines/create")
    public String createDeadlineForm(Model model) {
        model.addAttribute("deadline", new DeadlineDTO());
        model.addAttribute("employees", userService.getAllUsers());
        return "create_deadline";
    }

    @PostMapping("/deadlines/create")
    public String createDeadlineSubmit(@ModelAttribute DeadlineDTO deadlineDTO) {
        deadlineService.createDeadline(deadlineDTO);
        return "redirect:/admin/dashboard";
    }

    @PostMapping("/users")
    public ResponseEntity<?> createUser(@RequestBody UserDTO userDTO) {
        try {
            UserDTO savedUser = userService.saveUser(userDTO);
            return ResponseEntity.ok(savedUser);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody UserDTO userDTO) {
        try {
            userDTO.setId(id);
            UserDTO updatedUser = userService.updateUser(userDTO);
            return ResponseEntity.ok(updatedUser);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        try {
            userService.deleteUser(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/users/{id}/promote")
    public ResponseEntity<?> promoteToAdmin(@PathVariable Long id) {
        try {
            UserDTO userDTO = userService.findById(id).orElseThrow();
            userDTO.setRole(Role.ADMIN.name());
            UserDTO updatedUser = userService.updateUser(userDTO);
            return ResponseEntity.ok(updatedUser);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/history")
    public String viewHistory(Model model) {
        // Add project history and user activity
        model.addAttribute("projects", userService.getAllProjectHistory());
        return "admin/history";
    }
}
