package myproject.spektif_agency_application.dto;

import lombok.Data;
import myproject.spektif_agency_application.entity.UserRole;

@Data
public class UserDTO {
    private Long id;
    private String username;
    private UserRole userRole;
}
