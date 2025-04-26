package myproject.spektif_agency_application.service;

import myproject.spektif_agency_application.dto.CardDTO;

import java.util.List;
import java.util.Optional;

public interface CardService {

    CardDTO createCard(CardDTO cardDTO);

    List<CardDTO> getAllCards();

    void deleteCard(Long id);

    Optional<CardDTO> getCardById(Long id);

    Optional<CardDTO> updateCard(CardDTO cardDTO);

    Optional<CardDTO> addMemberToCard(Long cardId, Long memberId);

    Optional<CardDTO> removeMemberFromCard(Long cardId, Long memberId);

}
