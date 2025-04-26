package myproject.spektif_agency_application.service.impl;

import lombok.RequiredArgsConstructor;
import myproject.spektif_agency_application.dto.CardDTO;
import myproject.spektif_agency_application.mapper.CardMapper;
import myproject.spektif_agency_application.model.BoardList;
import myproject.spektif_agency_application.model.Card;
import myproject.spektif_agency_application.model.User;
import myproject.spektif_agency_application.repository.BoardListRepository;
import myproject.spektif_agency_application.repository.CardRepository;
import myproject.spektif_agency_application.repository.UserRepository;
import myproject.spektif_agency_application.service.CardService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CardServiceImpl implements CardService {

    private final CardRepository cardRepository;
    private final BoardListRepository boardListRepository;
    private final UserRepository userRepository;

    @Override
    public CardDTO createCard(CardDTO cardDTO) {
        Card card = CardMapper.toEntity(cardDTO);
        
        // Set the BoardList
        if (cardDTO.getListId() != null) {
            BoardList boardList = boardListRepository.findById(cardDTO.getListId())
                    .orElseThrow(() -> new RuntimeException("Board list not found"));
            card.setList(boardList);
        }

        // Set assigned members
        if (cardDTO.getAssignedMemberIds() != null && !cardDTO.getAssignedMemberIds().isEmpty()) {
            card.setAssignedMembers(
                userRepository.findAllById(cardDTO.getAssignedMemberIds())
                    .stream()
                    .collect(Collectors.toList())
            );
        }

        card = cardRepository.save(card);
        return CardMapper.toDTO(card);
    }

    @Override
    public Optional<CardDTO> getCardById(Long id) {
        return cardRepository.findById(id)
                .map(CardMapper::toDTO);
    }

    @Override
    public List<CardDTO> getAllCards() {
        return cardRepository.findAll().stream()
                .map(CardMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<CardDTO> updateCard(CardDTO cardDTO) {
        Card existingCard = cardRepository.findById(cardDTO.getId())
                .orElseThrow(() -> new RuntimeException("Card not found"));
        
        Card updatedCard = CardMapper.toEntity(cardDTO);
        updatedCard.setId(existingCard.getId());

        // Set the BoardList
        if (cardDTO.getListId() != null) {
            BoardList boardList = boardListRepository.findById(cardDTO.getListId())
                    .orElseThrow(() -> new RuntimeException("Board list not found"));
            updatedCard.setList(boardList);
        }

        // Set assigned members
        if (cardDTO.getAssignedMemberIds() != null && !cardDTO.getAssignedMemberIds().isEmpty()) {
            updatedCard.setAssignedMembers(
                userRepository.findAllById(cardDTO.getAssignedMemberIds())
                    .stream()
                    .collect(Collectors.toList())
            );
        }

        return Optional.of(CardMapper.toDTO(cardRepository.save(updatedCard)));
    }

    @Override
    public void deleteCard(Long id) {
        cardRepository.deleteById(id);
    }

    @Override
    public Optional<CardDTO> addMemberToCard(Long cardId, Long memberId) {
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new RuntimeException("Card not found"));
        
        User member = userRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        if (!card.getAssignedMembers().contains(member)) {
            card.getAssignedMembers().add(member);
            return Optional.of(CardMapper.toDTO(cardRepository.save(card)));
        }
        
        return Optional.of(CardMapper.toDTO(card));
    }

    @Override
    public Optional<CardDTO> removeMemberFromCard(Long cardId, Long memberId) {
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new RuntimeException("Card not found"));
        
        User member = userRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        card.getAssignedMembers().remove(member);
        return Optional.of(CardMapper.toDTO(cardRepository.save(card)));
    }

    @Override
    public void moveCardToList(Long cardId, Long newListId) {
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new RuntimeException("Card not found"));
        BoardList newList = boardListRepository.findById(newListId)
                .orElseThrow(() -> new RuntimeException("List not found"));
        card.setList(newList);
        cardRepository.save(card);
    }
}
