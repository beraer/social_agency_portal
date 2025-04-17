package myproject.spektif_agency_application.mapper;

import myproject.spektif_agency_application.dto.CardAttachmentDTO;
import myproject.spektif_agency_application.model.CardAttachment;

public class CardAttachmentMapper {

    public static CardAttachmentDTO toDTO(CardAttachment entity) {
        return CardAttachmentDTO.builder()
                .id(entity.getId())
                .filename(entity.getFilename())
                .fileType(entity.getFileType())
                .url(entity.getUrl())
                .build();
    }

    public static CardAttachment toEntity(CardAttachmentDTO dto) {
        return CardAttachment.builder()
                .id(dto.getId())
                .filename(dto.getFilename())
                .fileType(dto.getFileType())
                .url(dto.getUrl())
                .build();
    }
}
