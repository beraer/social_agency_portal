package myproject.spektif_agency_application.controller.api;

import lombok.RequiredArgsConstructor;
import myproject.spektif_agency_application.dto.CardDTO;
import myproject.spektif_agency_application.service.CardService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/cards")
@RequiredArgsConstructor
public class CardApiController {

    private final CardService cardService;

    @GetMapping("/{id}")
    public ResponseEntity<CardDTO> getCardDetails(@PathVariable Long id) {
        try {
            return cardService.getCardById(id)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/list/{listId}")
    public ResponseEntity<List<CardDTO>> getCardsByList(@PathVariable Long listId) {
        try {
            List<CardDTO> cards = cardService.getAllCards().stream()
                    .filter(card -> card.getListId() != null && card.getListId().equals(listId))
                    .collect(Collectors.toList());
            return ResponseEntity.ok(cards);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping
    public ResponseEntity<CardDTO> createCard(@RequestBody CardDTO cardDTO) {
        try {
            CardDTO createdCard = cardService.createCard(cardDTO);
            return ResponseEntity.ok(createdCard);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<CardDTO> updateCard(@PathVariable Long id, @RequestBody CardDTO cardDTO) {
        try {
            cardDTO.setId(id);
            return cardService.updateCard(cardDTO)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCard(@PathVariable Long id) {
        try {
            cardService.deleteCard(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/{cardId}/move")
    public ResponseEntity<?> moveCard(@PathVariable Long cardId, @RequestBody Map<String, Long> payload) {
        Long newListId = payload.get("listId");
        try {
            cardService.moveCardToList(cardId, newListId);
            return ResponseEntity.ok().body(Map.of("message", "Card moved successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }
} 