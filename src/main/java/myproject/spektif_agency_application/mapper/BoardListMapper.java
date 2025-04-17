package myproject.spektif_agency_application.mapper;

import myproject.spektif_agency_application.dto.BoardListDTO;
import myproject.spektif_agency_application.model.BoardList;
import myproject.spektif_agency_application.model.User;

import java.util.List;
import java.util.stream.Collectors;

public class BoardListMapper {

    public static BoardListDTO toDTO(BoardList boardList) {
        return BoardListDTO.builder()
                .id(boardList.getId())
                .title(boardList.getTitle())
                .description(boardList.getDescription())
                .ownerId(boardList.getOwner() != null ? boardList.getOwner().getId() : null)
                .memberIds(boardList.getMembers() != null
                        ? boardList.getMembers().stream().map(User::getId).collect(Collectors.toList())
                        : null)
                .cards(boardList.getCards() != null
                        ? boardList.getCards().stream().map(CardMapper::toDTO).collect(Collectors.toList())
                        : null)
                .build();
    }

    public static BoardList toEntity(BoardListDTO dto, List<User> users) {
        BoardList list = new BoardList();
        list.setId(dto.getId());
        list.setTitle(dto.getTitle());
        list.setDescription(dto.getDescription());

        if (dto.getMemberIds() != null) {
            list.setMembers(users); // Caller must pass resolved User list
        }

        return list;
    }
}
