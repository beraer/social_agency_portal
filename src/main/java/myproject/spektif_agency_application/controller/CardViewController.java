package myproject.spektif_agency_application.controller;

import lombok.RequiredArgsConstructor;
import myproject.spektif_agency_application.dto.CardDTO;
import myproject.spektif_agency_application.dto.CommentDTO;
import myproject.spektif_agency_application.dto.AttachmentDTO;
import myproject.spektif_agency_application.service.CardService;
import myproject.spektif_agency_application.service.UserService;
import myproject.spektif_agency_application.service.CommentService;
import myproject.spektif_agency_application.service.AttachmentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Map;

@Controller
@RequestMapping("/cards")
@RequiredArgsConstructor
public class CardViewController {

    private final CardService cardService;
    private final UserService userService;
    private final CommentService commentService;
    private final AttachmentService attachmentService;

    @GetMapping("/{id}")
    public String viewCard(@PathVariable Long id, Model model) {
        CardDTO card = cardService.getCardById(id)
                .orElseThrow(() -> new RuntimeException("Card not found"));
        model.addAttribute("card", card);
        model.addAttribute("availableMembers", userService.getAllUsers());
        return "cards/view";
    }

    @PostMapping("/{id}/description")
    @ResponseBody
    public ResponseEntity<CardDTO> updateDescription(
            @PathVariable Long id,
            @RequestBody Map<String, String> payload) {
        String description = payload.get("description");
        return cardService.getCardById(id)
                .map(card -> {
                    card.setDescription(description);
                    return ResponseEntity.ok(cardService.updateCard(card).get());
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{id}/members")
    @ResponseBody
    public ResponseEntity<CardDTO> addMember(
            @PathVariable Long id,
            @RequestBody Map<String, Long> payload) {
        Long memberId = payload.get("memberId");
        return cardService.addMemberToCard(id, memberId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}/members/{memberId}")
    @ResponseBody
    public ResponseEntity<CardDTO> removeMember(
            @PathVariable Long id,
            @PathVariable Long memberId) {
        return cardService.removeMemberFromCard(id, memberId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{id}/comments")
    @ResponseBody
    public ResponseEntity<CommentDTO> addComment(
            @PathVariable Long id,
            @RequestBody Map<String, String> payload,
            @AuthenticationPrincipal UserDetails userDetails) {
        String content = payload.get("content");
        return ResponseEntity.ok(commentService.addComment(id, content, userDetails.getUsername()));
    }

    @PostMapping("/{id}/attachments")
    @ResponseBody
    public ResponseEntity<AttachmentDTO> addAttachment(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok(attachmentService.addAttachment(id, file));
    }
} 