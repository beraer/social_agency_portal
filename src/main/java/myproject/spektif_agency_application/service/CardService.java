package myproject.spektif_agency_application.service;

import myproject.spektif_agency_application.dto.CardDTO;

import java.util.List;
import java.util.Optional;

public interface CardService {

    CardDTO createCard(CardDTO cardDTO);

    Optional<CardDTO> getCardById(Long id);

    List<CardDTO> getCardsByBoardListId(Long boardListId);

    CardDTO updateCard(Long id, CardDTO cardDTO);

    void deleteCard(Long id);
}
