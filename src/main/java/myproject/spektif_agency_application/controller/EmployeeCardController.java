package myproject.spektif_agency_application.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import myproject.spektif_agency_application.dto.CardDTO;
import myproject.spektif_agency_application.service.CardService;
import myproject.spektif_agency_application.service.UserService;
import myproject.spektif_agency_application.service.ActivityService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/employee/cards")
@RequiredArgsConstructor
public class EmployeeCardController {

    private final CardService cardService;
    private final UserService userService;
    private final ActivityService activityService;

    @GetMapping("/{id}")
    public String getCardDetails(@PathVariable("id") Long id, Model model) {
        CardDTO card = cardService.getCardById(id)
                .orElseThrow(() -> new IllegalArgumentException("Card not found with id: " + id));
        
        // Populate card activities
        card.setActivities(activityService.getActivitiesByCardId(id));
        
        model.addAttribute("card", card);
        model.addAttribute("availableMembers", userService.getAllEmployees());
        model.addAttribute("labels", new ArrayList<>()); // Add empty labels list for now
        
        return "fragments/card-modal :: cardDetailModal";
    }

    @GetMapping("/new")
    public String getNewCardForm(@RequestParam("listId") Long listId, Model model) {
        model.addAttribute("listId", listId);
        model.addAttribute("availableMembers", userService.getAllEmployees());
        model.addAttribute("labels", new ArrayList<>()); // Add empty labels list for now
        
        return "fragments/card-modal :: cardDetailModal";
    }

    @PostMapping("/create")
    @ResponseBody
    public ResponseEntity<CardDTO> createCard(@Valid @RequestBody CardDTO cardDTO) {
        CardDTO createdCard = cardService.createCard(cardDTO);
        return ResponseEntity.ok(createdCard);
    }

    @PostMapping("/{id}/update")
    @ResponseBody
    public ResponseEntity<CardDTO> updateCard(@PathVariable("id") Long id, @Valid @RequestBody CardDTO cardDTO) {
        cardDTO.setId(id);
        return cardService.updateCard(cardDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{id}/delete")
    @ResponseBody
    public ResponseEntity<Void> deleteCard(@PathVariable("id") Long id) {
        cardService.deleteCard(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/comments/add")
    @ResponseBody
    public ResponseEntity<String> addComment(@PathVariable("id") Long id, @RequestBody Map<String, String> payload) {
        String content = payload.get("content");
        activityService.addComment(id, content);
        return ResponseEntity.ok("Comment added successfully");
    }

    @PostMapping("/{cardId}/members/add")
    @ResponseBody
    public ResponseEntity<String> addMember(@PathVariable Long cardId, @RequestBody MemberRequest request) {
        cardService.addMemberToCard(cardId, request.getMemberId());
        return ResponseEntity.ok("success");
    }

    @PostMapping("/{cardId}/members/remove")
    @ResponseBody
    public ResponseEntity<String> removeMember(@PathVariable Long cardId, @RequestBody MemberRequest request) {
        cardService.removeMemberFromCard(cardId, request.getMemberId());
        return ResponseEntity.ok("success");
    }

    @PostMapping("/reorder")
    @ResponseBody
    public ResponseEntity<String> reorderCards(@RequestBody Map<String, Object> payload) {
        Long listId = Long.valueOf(payload.get("listId").toString());
        @SuppressWarnings("unchecked")
        List<Long> cardIds = ((List<Integer>) payload.get("cardIds"))
                .stream()
                .map(Long::valueOf)
                .collect(Collectors.toList());
        
        cardService.reorderCards(listId, cardIds);
        return ResponseEntity.ok("Cards reordered successfully");
    }

    private static class MemberRequest {
        private Long memberId;
        
        public Long getMemberId() {
            return memberId;
        }
        
        public void setMemberId(Long memberId) {
            this.memberId = memberId;
        }
    }
} 