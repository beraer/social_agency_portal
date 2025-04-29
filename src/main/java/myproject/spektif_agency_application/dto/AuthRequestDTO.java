package myproject.spektif_agency_application.dto;


import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthRequestDTO { 
    private String username;
    private String password;
}