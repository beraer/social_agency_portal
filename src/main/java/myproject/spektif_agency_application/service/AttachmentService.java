package myproject.spektif_agency_application.service;

import myproject.spektif_agency_application.dto.AttachmentDTO;
import org.springframework.web.multipart.MultipartFile;

public interface AttachmentService {
    AttachmentDTO addAttachment(Long cardId, MultipartFile file);
    void deleteAttachment(Long attachmentId);
} 