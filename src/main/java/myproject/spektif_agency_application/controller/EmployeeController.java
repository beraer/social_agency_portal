package myproject.spektif_agency_application.controller;

import lombok.RequiredArgsConstructor;
import myproject.spektif_agency_application.dto.BoardListDTO;
import myproject.spektif_agency_application.service.BoardListService;
import myproject.spektif_agency_application.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/employee")
@RequiredArgsConstructor
public class EmployeeController {

    private final BoardListService boardListService;
    private final UserService userService;

    @GetMapping("/dashboard")
    public String showDashboard(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        String username = userDetails.getUsername();
        List<BoardListDTO> boardLists = boardListService.getAllLists();

        model.addAttribute("boardLists", boardLists);
        model.addAttribute("username", username);
        model.addAttribute("role", "EMPLOYEE");

        return "employee/dashboard";
    }
}
