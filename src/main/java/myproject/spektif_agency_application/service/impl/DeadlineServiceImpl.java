package myproject.spektif_agency_application.service.impl;

import lombok.RequiredArgsConstructor;
import myproject.spektif_agency_application.dto.DeadlineDTO;
import myproject.spektif_agency_application.mapper.DeadlineMapper;
import myproject.spektif_agency_application.model.Deadline;
import myproject.spektif_agency_application.model.User;
import myproject.spektif_agency_application.repository.*;
import myproject.spektif_agency_application.service.DeadlineService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DeadlineServiceImpl implements DeadlineService {

    private final DeadlineRepository deadlineRepo;
    private final UserRepository userRepo;

    @Override
    public DeadlineDTO createDeadline(DeadlineDTO dto) {
        User user = userRepo.findById(dto.getAssignedToUserId()).orElseThrow();
        Deadline deadline = DeadlineMapper.toEntity(dto, user);
        return DeadlineMapper.toDTO(deadlineRepo.save(deadline));
    }

    @Override
    public List<DeadlineDTO> getAllDeadlines() {
        return deadlineRepo.findAll()
                .stream()
                .map(DeadlineMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<DeadlineDTO> getDeadlinesByUserId(Long userId) {
        return deadlineRepo.findByAssignedToId(userId)
                .stream()
                .map(DeadlineMapper::toDTO)
                .collect(Collectors.toList());
    }
}
