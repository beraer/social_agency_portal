package myproject.spektif_agency_application.service;

import myproject.spektif_agency_application.dto.ActivityDTO;
import java.util.List;

public interface ActivityService {
    List<ActivityDTO> getActivitiesByCardId(Long cardId);
    ActivityDTO addComment(Long cardId, String content);
    ActivityDTO addActivity(Long cardId, String content, String type);
} 