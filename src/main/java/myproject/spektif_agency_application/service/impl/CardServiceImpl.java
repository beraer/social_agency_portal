package myproject.spektif_agency_application.service.impl;

import lombok.RequiredArgsConstructor;
import myproject.spektif_agency_application.dto.CardDTO;
import myproject.spektif_agency_application.mapper.CardMapper;
import myproject.spektif_agency_application.mapper.ProjectMapper;
import myproject.spektif_agency_application.model.BoardList;
import myproject.spektif_agency_application.model.Card;
import myproject.spektif_agency_application.model.Project;
import myproject.spektif_agency_application.model.User;
import myproject.spektif_agency_application.repository.BoardListRepository;
import myproject.spektif_agency_application.repository.CardRepository;
import myproject.spektif_agency_application.repository.ProjectRepository;
import myproject.spektif_agency_application.repository.UserRepository;
import myproject.spektif_agency_application.service.CardService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CardServiceImpl implements CardService {

    private final CardRepository cardRepository;
    private final BoardListRepository boardListRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    @Override
    public CardDTO createCard(CardDTO dto) {
        BoardList boardList = boardListRepository.findById(dto.getBoardListId()).orElseThrow();

        List<User> assignedMembers = dto.getAssignedMemberIds() != null
                ? dto.getAssignedMemberIds().stream()
                .map(id -> userRepository.findById(id).orElseThrow())
                .collect(Collectors.toList())
                : List.of();

        Card card = CardMapper.toEntity(dto, assignedMembers);
        card.setBoardList(boardList);

        if (dto.isProject()) {
            Project project = ProjectMapper.toEntity(dto.getProjectDetails());
            project.setCard(card); // bidirectional
            card.setProjectDetails(project);
        }

        return CardMapper.toDTO(cardRepository.save(card));
    }

    @Override
    public Optional<CardDTO> getCardById(Long id) {
        return cardRepository.findById(id).map(CardMapper::toDTO);
    }

    @Override
    public List<CardDTO> getCardsByBoardListId(Long boardListId) {
        return cardRepository.findByBoardListId(boardListId)
                .stream()
                .map(CardMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public CardDTO updateCard(Long id, CardDTO dto) {
        Card existing = cardRepository.findById(id).orElseThrow();

        existing.setTitle(dto.getTitle());
        existing.setDescription(dto.getDescription());
        existing.setDueDate(dto.getDueDate());
        existing.setProject(dto.isProject());

        if (dto.getAssignedMemberIds() != null) {
            List<User> updatedUsers = dto.getAssignedMemberIds().stream()
                    .map(uid -> userRepository.findById(uid).orElseThrow())
                    .collect(Collectors.toList());
            existing.setAssignedMembers(updatedUsers);
        }

        if (dto.isProject() && dto.getProjectDetails() != null) {
            Project updatedProject = ProjectMapper.toEntity(dto.getProjectDetails());
            updatedProject.setCard(existing);
            existing.setProjectDetails(updatedProject);
        }

        return CardMapper.toDTO(cardRepository.save(existing));
    }

    @Override
    public void deleteCard(Long id) {
        cardRepository.deleteById(id);
    }
}
