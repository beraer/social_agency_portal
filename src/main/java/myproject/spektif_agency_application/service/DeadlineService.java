package myproject.spektif_agency_application.service;

import myproject.spektif_agency_application.dto.DeadlineDTO;

import java.util.List;

public interface DeadlineService {
    DeadlineDTO createDeadline(DeadlineDTO dto);
    List<DeadlineDTO> getAllDeadlines();
    List<DeadlineDTO> getDeadlinesByUserId(Long userId);
}
