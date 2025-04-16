package myproject.spektif_agency_application.repository;

import myproject.spektif_agency_application.entity.Deadline;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DeadlineRepository extends JpaRepository<Deadline, Long> {
    List<Deadline> findByAssignedToId(Long userId);
}
