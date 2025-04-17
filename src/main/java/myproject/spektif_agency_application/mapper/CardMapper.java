package myproject.spektif_agency_application.mapper;

import myproject.spektif_agency_application.dto.CardDTO;
import myproject.spektif_agency_application.mapper.CardAttachmentMapper;
import myproject.spektif_agency_application.mapper.ProjectMapper;
import myproject.spektif_agency_application.model.Card;
import myproject.spektif_agency_application.model.User;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class CardMapper {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static CardDTO toDTO(Card card) {
        if (card == null) {
            return null;
        }

        CardDTO dto = new CardDTO();
        dto.setId(card.getId());
        dto.setTitle(card.getTitle());
        dto.setDescription(card.getDescription());
        dto.setDueDate(card.getDueDate());
        dto.setProject(card.isProject());
        dto.setBoardListId(card.getBoardList() != null ? card.getBoardList().getId() : null);
        dto.setProjectDetails(card.getProjectDetails() != null ? ProjectMapper.toDTO(card.getProjectDetails()) : null);
        dto.setAttachments(card.getAttachments() != null
                ? card.getAttachments().stream().map(CardAttachmentMapper::toDTO).collect(Collectors.toList())
                : null);
        dto.setAssignedMemberIds(card.getAssignedMembers() != null
                ? card.getAssignedMembers().stream().map(User::getId).collect(Collectors.toList())
                : null);

        return dto;
    }

    public static Card toEntity(CardDTO dto, List<User> assignedMembers) {
        Card card = toEntity(dto);
        card.setAssignedMembers(assignedMembers);
        return card;
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


