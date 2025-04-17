package myproject.spektif_agency_application.repository;

import myproject.spektif_agency_application.model.BoardList;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardListRepository extends JpaRepository<BoardList, Long> {
}
