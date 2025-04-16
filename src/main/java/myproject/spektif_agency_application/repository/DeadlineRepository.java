package myproject.spektif_agency_application.repository;

import myproject.spektif_agency_application.model.Deadline;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeadlineRepository extends JpaRepository<Deadline, Long> {
    List<Deadline> findByAssignedToId(Long userId);
}
