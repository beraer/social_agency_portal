package myproject.spektif_agency_application.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import myproject.spektif_agency_application.model.Role;
import myproject.spektif_agency_application.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @GetMapping("/")
    public String rootRedirect(Authentication authentication) {
        if (authentication == null) {
            return "redirect:/login";
        }

        Role role = Role.valueOf(authentication.getAuthorities().iterator().next().getAuthority().replace("ROLE_", ""));
        return switch (role) {
            case ADMIN -> "redirect:/admin/dashboard";
            case EMPLOYEE -> "redirect:/employee/dashboard";
            case CLIENT -> "redirect:/client/dashboard";
        };
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/logout-success")
    public String logoutPage() {
        return "redirect:/login?logout";
    }
}
