package myproject.spektif_agency_application.controller;

import lombok.RequiredArgsConstructor;
import myproject.spektif_agency_application.dto.CardDTO;
import myproject.spektif_agency_application.service.BoardListService;
import myproject.spektif_agency_application.service.CardService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/employee/cards")
@RequiredArgsConstructor
public class CardController {

    private final CardService cardService;
    private final BoardListService boardListService;

    @GetMapping("/list/{boardListId}")
    public String viewCards(@PathVariable Long boardListId, Model model) {
        List<CardDTO> cards = cardService.getCardsByBoardListId(boardListId);
        model.addAttribute("cards", cards);
        model.addAttribute("boardListId", boardListId);
        return "employee/card-list";
    }

    @GetMapping("/create/{boardListId}")
    public String showCreateCardForm(@PathVariable Long boardListId, Model model) {
        CardDTO card = new CardDTO();
        card.setBoardListId(boardListId);
        model.addAttribute("card", card);
        return "employee/card-form";
    }

    @PostMapping("/create")
    public String createCard(@ModelAttribute("card") CardDTO cardDTO) {
        cardService.createCard(cardDTO);
        return "redirect:/employee/cards/list/" + cardDTO.getBoardListId();
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        CardDTO card = cardService.getCardById(id).orElseThrow();
        model.addAttribute("card", card);
        return "employee/card-form";
    }

    @PostMapping("/edit/{id}")
    public String updateCard(@PathVariable Long id, @ModelAttribute("card") CardDTO cardDTO) {
        cardService.updateCard(id, cardDTO);
        return "redirect:/employee/cards/list/" + cardDTO.getBoardListId();
    }

    @PostMapping("/delete/{id}")
    public String deleteCard(@PathVariable Long id, @RequestParam Long boardListId) {
        cardService.deleteCard(id);
        return "redirect:/employee/cards/list/" + boardListId;
    }
}
