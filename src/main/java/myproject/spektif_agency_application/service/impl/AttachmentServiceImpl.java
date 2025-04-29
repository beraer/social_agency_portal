package myproject.spektif_agency_application.service.impl;

import lombok.RequiredArgsConstructor;
import myproject.spektif_agency_application.dto.AttachmentDTO;
import myproject.spektif_agency_application.mapper.AttachmentMapper;
import myproject.spektif_agency_application.model.Attachment;
import myproject.spektif_agency_application.model.Card;
import myproject.spektif_agency_application.repository.AttachmentRepository;
import myproject.spektif_agency_application.repository.CardRepository;
import myproject.spektif_agency_application.service.AttachmentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AttachmentServiceImpl implements AttachmentService {

    private final AttachmentRepository attachmentRepository;
    private final CardRepository cardRepository;
    private final String uploadDir = "./uploads";

    @Override
    @Transactional
    public AttachmentDTO addAttachment(Long cardId, MultipartFile file) {
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new RuntimeException("Card not found"));

        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        String uniqueFileName = UUID.randomUUID().toString() + "_" + fileName;
        
        try {
            // Create uploads directory if it doesn't exist
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            
            // Save the file
            Path filePath = uploadPath.resolve(uniqueFileName);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            
            // Create attachment record
            Attachment attachment = new Attachment();
            attachment.setFileName(fileName);
            attachment.setFileUrl("/uploads/" + uniqueFileName);
            attachment.setFileType(file.getContentType());
            attachment.setCard(card);
            
            attachment = attachmentRepository.save(attachment);
            return AttachmentMapper.toDTO(attachment);
            
        } catch (IOException ex) {
            throw new RuntimeException("Could not store file " + fileName, ex);
        }
    }

    @Override
    @Transactional
    public void deleteAttachment(Long attachmentId) {
        Attachment attachment = attachmentRepository.findById(attachmentId)
                .orElseThrow(() -> new RuntimeException("Attachment not found"));
        
        try {
            // Delete the file
            Path filePath = Paths.get(uploadDir).resolve(
                attachment.getFileUrl().substring(attachment.getFileUrl().lastIndexOf('/') + 1)
            );
            Files.deleteIfExists(filePath);
            
            // Delete the record
            attachmentRepository.delete(attachment);
            
        } catch (IOException ex) {
            throw new RuntimeException("Could not delete file", ex);
        }
    }
} 