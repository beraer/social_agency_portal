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

@Controller
@RequestMapping("/employee/boardlists")
@RequiredArgsConstructor
public class BoardListController {

    private final BoardListService boardListService;
    private final UserService userService;

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("boardList", new BoardListDTO());
        return "employee/boardlist-form";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute("boardList") BoardListDTO dto, 
                        @AuthenticationPrincipal UserDetails userDetails) {
        try {
            Long ownerId = userService.findByUsername(userDetails.getUsername())
                    .orElseThrow(() -> new RuntimeException("User not found"))
                    .getId();
            dto.setOwnerId(ownerId);
            boardListService.createBoardList(dto);
            return "redirect:/employee/dashboard";
        } catch (Exception e) {
            return "redirect:/employee/boardlists/create?error=" + e.getMessage();
        }
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        BoardListDTO boardList = boardListService.getById(id)
                .orElseThrow(() -> new RuntimeException("List not found"));
        model.addAttribute("boardList", boardList);
        return "employee/boardlist-form";
    }

    @PostMapping("/edit/{id}")
    public String update(@PathVariable Long id, 
                        @ModelAttribute("boardList") BoardListDTO dto) {
        try {
            boardListService.updateBoardList(id, dto);
            return "redirect:/employee/dashboard";
        } catch (Exception e) {
            return "redirect:/employee/boardlists/edit/" + id + "?error=" + e.getMessage();
        }
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        try {
            boardListService.deleteBoardList(id);
            return "redirect:/employee/dashboard";
        } catch (Exception e) {
            return "redirect:/employee/dashboard?error=" + e.getMessage();
        }
    }
}
