package myproject.spektif_agency_application.controller;

import lombok.RequiredArgsConstructor;
import myproject.spektif_agency_application.dto.BoardListDTO;
import myproject.spektif_agency_application.dto.CardDTO;
import myproject.spektif_agency_application.dto.ErrorResponse;
import myproject.spektif_agency_application.dto.UserDTO;
import myproject.spektif_agency_application.service.BoardListService;
import myproject.spektif_agency_application.service.CardService;
import myproject.spektif_agency_application.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/employee")
@RequiredArgsConstructor
public class EmployeeController {

    private final BoardListService boardListService;
    private final UserService userService;
    private final CardService cardService;

    @GetMapping("/dashboard")
    public String showDashboard(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        String username = userDetails.getUsername();
        List<BoardListDTO> boardLists = boardListService.getAllLists();
        List<UserDTO> members = userService.getAllEmployees();

        model.addAttribute("boardLists", boardLists);
        model.addAttribute("username", username);
        model.addAttribute("role", "EMPLOYEE");
        model.addAttribute("newList", new BoardListDTO());
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

    @PostMapping(value = "/cards/create", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String createCardFromForm(@ModelAttribute CardDTO cardDTO,
                                   @AuthenticationPrincipal UserDetails userDetails) {
        try {
            Long creatorId = userService.findByUsername(userDetails.getUsername())
                    .orElseThrow()
                    .getId();
            
            cardDTO.setCreatedAt(LocalDateTime.now());
            cardDTO.setUpdatedAt(LocalDateTime.now());
            
            cardService.createCard(cardDTO);
            return "redirect:/employee/dashboard";
        } catch (Exception e) {
            return "redirect:/employee/dashboard?error=" + e.getMessage();
        }
    }

    @PostMapping(value = "/cards/create", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> createCardFromJson(@RequestBody CardDTO cardDTO,
                                              @AuthenticationPrincipal UserDetails userDetails) {
        try {
            Long creatorId = userService.findByUsername(userDetails.getUsername())
                    .orElseThrow()
                    .getId();
            
            cardDTO.setCreatedAt(LocalDateTime.now());
            cardDTO.setUpdatedAt(LocalDateTime.now());
            
            CardDTO created = cardService.createCard(cardDTO);
            return ResponseEntity.ok(created);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(new ErrorResponse("Error creating card: " + e.getMessage()));
        }
    }

    @GetMapping("/cards/{id}")
    public String getCardDetails(@PathVariable Long id, Model model) {
        try {
            System.out.println("Fetching card details for ID: " + id);
            
            CardDTO card = cardService.getCardById(id)
                    .orElseThrow(() -> new RuntimeException("Card not found"));
            
            System.out.println("Found card: " + card);
            System.out.println("Card ID: " + card.getId());
            System.out.println("Card Title: " + card.getTitle());
            System.out.println("Card Description: " + card.getDescription());
            System.out.println("Card Deadline: " + card.getDeadline());
            System.out.println("Card Members: " + card.getAssignedMemberIds());
            
            List<UserDTO> members = userService.getAllEmployees();
            System.out.println("Found " + members.size() + " members");
            
            model.addAttribute("card", card);
            model.addAttribute("members", members);
            
            System.out.println("Model attributes added:");
            System.out.println("- card: " + model.getAttribute("card"));
            System.out.println("- members: " + model.getAttribute("members"));
            
            return "fragments/card-detail-modal :: cardDetailModal";
        } catch (Exception e) {
            System.out.println("Error in getCardDetails: " + e.getMessage());
            e.printStackTrace();
            model.addAttribute("error", "Error loading card details: " + e.getMessage());
            return "error :: content";
        }
    }

    @PutMapping("/cards/{id}")
    @ResponseBody
    public ResponseEntity<CardDTO> updateCard(@PathVariable Long id, @RequestBody CardDTO cardDTO) {
        try {
            if (!id.equals(cardDTO.getId())) {
                return ResponseEntity.badRequest().build();
            }
            
            cardDTO.setUpdatedAt(LocalDateTime.now());
            Optional<CardDTO> updatedCard = cardService.updateCard(cardDTO);
            
            return updatedCard.map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            System.out.println("Error updating card: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/members")
    @ResponseBody
    public ResponseEntity<List<UserDTO>> getMembers(@RequestParam List<Long> ids) {
        try {
            List<UserDTO> members = userService.getAllUsers().stream()
                    .filter(user -> ids.contains(user.getId()))
                    .collect(Collectors.toList());
            return ResponseEntity.ok(members);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/cards/{cardId}/members")
    @ResponseBody
    public ResponseEntity<CardDTO> addMemberToCard(@PathVariable Long cardId, @RequestBody Map<String, Long> request) {
        try {
            Long memberId = request.get("memberId");
            if (memberId == null) {
                return ResponseEntity.badRequest().build();
            }
            Optional<CardDTO> updatedCard = cardService.addMemberToCard(cardId, memberId);
            return updatedCard.map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/cards/{cardId}/members/{memberId}")
    @ResponseBody
    public ResponseEntity<CardDTO> removeMemberFromCard(@PathVariable Long cardId, @PathVariable Long memberId) {
        try {
            Optional<CardDTO> updatedCard = cardService.removeMemberFromCard(cardId, memberId);
            return updatedCard.map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/cards/{id}")
    @ResponseBody
    public ResponseEntity<Void> deleteCard(@PathVariable Long id) {
        try {
            System.out.println("Deleting card with ID: " + id);
            cardService.deleteCard(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            System.out.println("Error deleting card: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
