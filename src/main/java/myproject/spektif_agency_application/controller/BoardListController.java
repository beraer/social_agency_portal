package myproject.spektif_agency_application.controller;

import lombok.RequiredArgsConstructor;
import myproject.spektif_agency_application.dto.BoardListDTO;
import myproject.spektif_agency_application.service.BoardListService;
import myproject.spektif_agency_application.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/employee/boardlists")
@RequiredArgsConstructor
public class BoardListController {

    private final BoardListService boardListService;
    private final UserService userService;

    @GetMapping("/dashboard")
    public String dashboard(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        String currentUsername = userDetails.getUsername();
        List<BoardListDTO> boardLists = boardListService.getAllLists();

        model.addAttribute("boardLists", boardLists);
        model.addAttribute("currentUser", currentUsername); // optional usage in Thymeleaf
        return "employee/dashboard";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("boardList", new BoardListDTO());
        model.addAttribute("users", userService.getAllUsers());
        return "employee/boardlist-form";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute("boardList") BoardListDTO dto, @AuthenticationPrincipal UserDetails userDetails) {
        Long ownerId = userService.findByUsername(userDetails.getUsername())
                .orElseThrow()
                .getId();
        dto.setOwnerId(ownerId);
        boardListService.createBoardList(dto);
        return "redirect:/employee/boardlists/dashboard";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        BoardListDTO boardList = boardListService.getById(id).orElseThrow();
        model.addAttribute("boardList", boardList);
        model.addAttribute("users", userService.getAllUsers());
        return "employee/boardlist-form";
    }

    @PostMapping("/edit/{id}")
    public String update(@PathVariable Long id, @ModelAttribute("boardList") BoardListDTO dto) {
        boardListService.updateBoardList(id, dto);
        return "redirect:/employee/boardlists/dashboard";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        boardListService.deleteBoardList(id);
        return "redirect:/employee/boardlists/dashboard";
    }
}
