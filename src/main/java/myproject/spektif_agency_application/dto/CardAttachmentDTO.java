package myproject.spektif_agency_application.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CardAttachmentDTO {
    private Long id;
    private String filename;
    private String fileType;
    private String url;
}
