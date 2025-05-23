package myproject.spektif_agency_application.repository;

import myproject.spektif_agency_application.model.Activity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActivityRepository extends JpaRepository<Activity, Long> {
    List<Activity> findByCardId(Long cardId);
    List<Activity> findByCardIdOrderByCreatedAtDesc(Long cardId);
} 