package myproject.spektif_agency_application.mapper;

import myproject.spektif_agency_application.dto.CardDTO;
import myproject.spektif_agency_application.model.Card;
import myproject.spektif_agency_application.model.User;

import java.util.stream.Collectors;

public class CardMapper {
    
    public static CardDTO toDTO(Card card) {
        if (card == null) return null;
        
        return CardDTO.builder()
                .id(card.getId())
                .title(card.getTitle())
                .description(card.getDescription())
                .deadline(card.getDeadline())
                .listId(card.getList() != null ? card.getList().getId() : null)
                .listTitle(card.getList() != null ? card.getList().getTitle() : null)
                .assignedMemberIds(card.getAssignedMembers() != null ? 
                    card.getAssignedMembers().stream()
                        .map(User::getId)
                        .collect(Collectors.toList()) :
                    null)
                .activities(card.getComments() != null ?
                    card.getComments().stream()
                        .map(CommentMapper::toActivityDTO)
                        .collect(Collectors.toList()) :
                    null)
                .attachments(card.getAttachments() != null ?
                    card.getAttachments().stream()
                        .map(AttachmentMapper::toDTO)
                        .collect(Collectors.toList()) :
                    null)
                .createdAt(card.getCreatedAt())
                .updatedAt(card.getUpdatedAt())
                .build();
    }
    
    public static Card toEntity(CardDTO dto) {
        if (dto == null) return null;
        
        Card card = new Card();
        card.setId(dto.getId());
        card.setTitle(dto.getTitle());
        card.setDescription(dto.getDescription());
        card.setDeadline(dto.getDeadline());
        card.setCreatedAt(dto.getCreatedAt());
        card.setUpdatedAt(dto.getUpdatedAt());
        
        return card;
    }
}


