package myproject.spektif_agency_application.repository;

import myproject.spektif_agency_application.model.Card;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CardRepository extends JpaRepository<Card, Long> {
    List<Card> findByBoardListId(Long boardListId);
}
