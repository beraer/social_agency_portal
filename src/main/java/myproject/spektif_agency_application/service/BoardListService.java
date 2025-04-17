package myproject.spektif_agency_application.service;

import myproject.spektif_agency_application.dto.BoardListDTO;

import java.util.List;
import java.util.Optional;

public interface BoardListService {

    BoardListDTO createBoardList(BoardListDTO dto);


    Optional<BoardListDTO> getById(Long id);

    BoardListDTO updateBoardList(Long id, BoardListDTO dto);

    void deleteBoardList(Long id);

    List<BoardListDTO> getAllLists();
}
