package myproject.spektif_agency_application.service.impl;

import myproject.spektif_agency_application.dto.ActivityDTO;
import myproject.spektif_agency_application.model.Activity;
import myproject.spektif_agency_application.repository.ActivityRepository;
import myproject.spektif_agency_application.service.ActivityService;
import myproject.spektif_agency_application.mapper.ActivityMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ActivityServiceImpl implements ActivityService {
    private final ActivityRepository activityRepository;

    @Override
    @Transactional(readOnly = true)
    public List<ActivityDTO> getActivitiesByCardId(Long cardId) {
        return activityRepository.findByCardIdOrderByCreatedAtDesc(cardId)
            .stream()
            .map(ActivityMapper::toDTO)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ActivityDTO addComment(Long cardId, String content) {
        return addActivity(cardId, content, "COMMENT");
    }

    @Override
    @Transactional
    public ActivityDTO addActivity(Long cardId, String content, String type) {
        Activity activity = new Activity();
        activity.setCardId(cardId);
        activity.setContent(content);
        activity.setType(type);
        // TODO: Set current user once security is implemented
        
        Activity savedActivity = activityRepository.save(activity);
        return ActivityMapper.toDTO(savedActivity);
    }
} 