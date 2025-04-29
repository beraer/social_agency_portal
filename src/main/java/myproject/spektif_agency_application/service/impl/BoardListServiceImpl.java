package myproject.spektif_agency_application.service.impl;

import lombok.RequiredArgsConstructor;
import myproject.spektif_agency_application.dto.BoardListDTO;
import myproject.spektif_agency_application.dto.CardDTO;
import myproject.spektif_agency_application.mapper.BoardListMapper;
import myproject.spektif_agency_application.model.BoardList;
import myproject.spektif_agency_application.model.User;
import myproject.spektif_agency_application.repository.BoardListRepository;
import myproject.spektif_agency_application.repository.UserRepository;
import myproject.spektif_agency_application.service.BoardListService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class BoardListServiceImpl implements BoardListService {

    private final BoardListRepository boardListRepository;
    private final UserRepository userRepository;

    @Override
    public BoardListDTO createBoardList(BoardListDTO dto) {
        User owner = userRepository.findById(dto.getOwnerId()).orElseThrow();
        List<User> members = dto.getMemberIds() != null
                ? dto.getMemberIds().stream()
                .map(id -> userRepository.findById(id).orElseThrow())
                .collect(Collectors.toList())
                : List.of();

        BoardList list = BoardListMapper.toEntity(dto, owner, members);
        return BoardListMapper.toDTO(boardListRepository.save(list));
    }

    @Override
    public Optional<BoardListDTO> getById(Long id) {
        return boardListRepository.findById(id)
                .map(BoardListMapper::toDTO);
    }

    @Override
    public BoardListDTO updateBoardList(Long id, BoardListDTO dto) {
        BoardList list = boardListRepository.findById(id).orElseThrow();

        list.setTitle(dto.getTitle());
        list.setDescription(dto.getDescription());

        if (dto.getMemberIds() != null) {
            List<User> updatedMembers = dto.getMemberIds().stream()
                    .map(userId -> userRepository.findById(userId).orElseThrow())
                    .collect(Collectors.toList());
            list.setMembers(updatedMembers);
        }

        return BoardListMapper.toDTO(boardListRepository.save(list));
    }

    @Override
    public List<BoardListDTO> getAllLists() {
        return boardListRepository.findAll()
                .stream()
                .map(BoardListMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteBoardList(Long id) {
        boardListRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void reorderLists(List<Long> listIds) {
        IntStream.range(0, listIds.size()).forEach(index -> {
            Long listId = listIds.get(index);
            BoardList list = boardListRepository.findById(listId)
                    .orElseThrow(() -> new IllegalArgumentException("List not found with id: " + listId));
            list.setOrder(index);
            boardListRepository.save(list);
        });
    }
}
