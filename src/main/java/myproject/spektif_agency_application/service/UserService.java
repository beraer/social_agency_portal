package myproject.spektif_agency_application.service;

import myproject.spektif_agency_application.dto.UserDTO;

import java.util.List;
import java.util.Optional;

public interface UserService {
    Optional<UserDTO> findById(Long id);
    Optional<UserDTO> findByUsername(String username);
    List<UserDTO> getAllUsers();
    UserDTO saveUser(UserDTO userDTO);
}
