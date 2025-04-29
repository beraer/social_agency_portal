package myproject.spektif_agency_application.controller;

import lombok.RequiredArgsConstructor;
import myproject.spektif_agency_application.dto.BoardListDTO;
import myproject.spektif_agency_application.dto.UserDTO;
import myproject.spektif_agency_application.service.BoardListService;
import myproject.spektif_agency_application.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
        List<UserDTO> members = userService.getAllEmployees();

        model.addAttribute("boardLists", boardLists);
        model.addAttribute("username", username);
        model.addAttribute("role", "EMPLOYEE");
        model.addAttribute("boardList", new BoardListDTO());
        model.addAttribute("members", members);

        return "employee/dashboard";
    }

    @PostMapping("/lists/create")
    public String createList(@ModelAttribute("newList") BoardListDTO dto, 
                           @AuthenticationPrincipal UserDetails userDetails) {
        Long ownerId = userService.findByUsername(userDetails.getUsername())
                .orElseThrow()
                .getId();
        dto.setOwnerId(ownerId);
        boardListService.createBoardList(dto);
        return "redirect:/employee/dashboard";
    }
}
