package myproject.spektif_agency_application.mapper;

import myproject.spektif_agency_application.dto.UserDTO;
import myproject.spektif_agency_application.entity.Role;
import myproject.spektif_agency_application.entity.User;

public class UserMapper {

    public static UserDTO toDTO(User user) {
        return UserDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .role(user.getRole().name())
                .build();
    }

    public static User toEntity(UserDTO dto) {
        return User.builder()
                .id(dto.getId())
                .username(dto.getUsername())
                .role(Role.valueOf(dto.getRole()))
                .build();
    }
}
