package myproject.spektif_agency_application.mapper;

import myproject.spektif_agency_application.dto.ActivityDTO;
import myproject.spektif_agency_application.model.Activity;

public class ActivityMapper {
    
    public static ActivityDTO toDTO(Activity activity) {
        if (activity == null) return null;
        
        return ActivityDTO.builder()
                .id(activity.getId())
                .type(activity.getType())
                .cardId(activity.getCardId())
                .createdBy(activity.getCreatedBy())
                .content(activity.getContent())
                .createdAt(activity.getCreatedAt())
                .build();
    }
    
    public static Activity toEntity(ActivityDTO dto) {
        if (dto == null) return null;
        
        Activity activity = new Activity();
        activity.setId(dto.getId());
        activity.setType(dto.getType());
        activity.setCardId(dto.getCardId());
        activity.setCreatedBy(dto.getCreatedBy());
        activity.setContent(dto.getContent());
        activity.setCreatedAt(dto.getCreatedAt());
        
        return activity;
    }
} 