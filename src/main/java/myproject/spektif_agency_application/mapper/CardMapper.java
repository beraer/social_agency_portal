package myproject.spektif_agency_application.mapper;

import myproject.spektif_agency_application.dto.CardDTO;
import myproject.spektif_agency_application.model.Card;

import java.util.stream.Collectors;

public class CardMapper {

    public static CardDTO toDTO(Card card) {
        return CardDTO.builder()
                .id(card.getId())
                .title(card.getTitle())
                .description(card.getDescription())
                .dueDate(card.getDueDate())
                .isProject(card.isProject())
                .boardListId(card.getBoardList() != null ? card.getBoardList().getId() : null)
                .projectDetails(card.getProjectDetails() != null ? ProjectMapper.toDTO(card.getProjectDetails()) : null)
                .attachments(card.getAttachments() != null
                        ? card.getAttachments().stream().map(CardAttachmentMapper::toDTO).collect(Collectors.toList())
                        : null)
                .build();
    }

    public static Card toEntity(CardDTO dto) {
        Card card = new Card();
        card.setId(dto.getId());
        card.setTitle(dto.getTitle());
        card.setDescription(dto.getDescription());
        card.setDueDate(dto.getDueDate());
        card.setProject(dto.isProject());

        if (dto.getProjectDetails() != null) {
            card.setProjectDetails(ProjectMapper.toEntity(dto.getProjectDetails()));
        }

        if (dto.getAttachments() != null) {
            card.setAttachments(dto.getAttachments().stream()
                    .map(CardAttachmentMapper::toEntity)
                    .collect(Collectors.toList()));
        }

        return card;
    }
}
