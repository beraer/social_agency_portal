package myproject.spektif_agency_application.mapper;

import myproject.spektif_agency_application.dto.BoardListDTO;
import myproject.spektif_agency_application.model.BoardList;
import myproject.spektif_agency_application.model.User;

import java.util.List;
import java.util.stream.Collectors;

public class BoardListMapper {

    public static BoardListDTO toDTO(BoardList boardList) {
        if (boardList == null) return null;
        
        return BoardListDTO.builder()
                .id(boardList.getId())
                .title(boardList.getTitle())
                .description(boardList.getDescription())
                .ownerId(boardList.getOwner() != null ? boardList.getOwner().getId() : null)
                .memberIds(boardList.getMembers() != null ? 
                    boardList.getMembers().stream()
                        .map(User::getId)
                        .collect(Collectors.toList()) : 
                    null)
                .cards(boardList.getCards() != null ?
                    boardList.getCards().stream()
                        .map(CardMapper::toDTO)
                        .collect(Collectors.toList()) :
                    null)
                .order(boardList.getOrder())
                .build();
    }

    public static BoardList toEntity(BoardListDTO dto, User owner, List<User> members) {
        if (dto == null) return null;
        
        BoardList boardList = new BoardList();
        boardList.setId(dto.getId());
        boardList.setTitle(dto.getTitle());
        boardList.setDescription(dto.getDescription());
        boardList.setOwner(owner);
        boardList.setMembers(members);
        boardList.setOrder(dto.getOrder());
        
        return boardList;
    }
}
