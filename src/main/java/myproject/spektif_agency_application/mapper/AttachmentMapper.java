package myproject.spektif_agency_application.mapper;

import myproject.spektif_agency_application.dto.AttachmentDTO;
import myproject.spektif_agency_application.model.Attachment;

public class AttachmentMapper {
    
    public static AttachmentDTO toDTO(Attachment attachment) {
        if (attachment == null) return null;
        
        return AttachmentDTO.builder()
                .id(attachment.getId())
                .fileName(attachment.getFileName())
                .fileType(attachment.getFileType())
                .fileUrl(attachment.getFileUrl())
                .cardId(attachment.getCard() != null ? attachment.getCard().getId() : null)
                .uploadedAt(attachment.getUploadedAt())
                .build();
    }
    
    public static Attachment toEntity(AttachmentDTO dto) {
        if (dto == null) return null;
        
        Attachment attachment = new Attachment();
        attachment.setId(dto.getId());
        attachment.setFileName(dto.getFileName());
        attachment.setFileType(dto.getFileType());
        attachment.setFileUrl(dto.getFileUrl());
        attachment.setUploadedAt(dto.getUploadedAt());
        
        return attachment;
    }
} 