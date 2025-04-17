package myproject.spektif_agency_application.dto;

import lombok.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoardListDTO {
    private Long id;
    private String title;
    private String description;
    private Long ownerId;
    private List<Long> memberIds;
    private List<CardDTO> cards;
}
