package myproject.spektif_agency_application.service.impl;

import lombok.RequiredArgsConstructor;
import myproject.spektif_agency_application.dto.ProjectDTO;
import myproject.spektif_agency_application.dto.UserDTO;
import myproject.spektif_agency_application.model.User;
import myproject.spektif_agency_application.mapper.UserMapper;
import myproject.spektif_agency_application.repository.ProjectRepository;
import myproject.spektif_agency_application.repository.UserRepository;
import myproject.spektif_agency_application.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Optional<UserDTO> findById(Long id) {
        return userRepository.findById(id).map(UserMapper::toDTO);
    }

    @Override
    public Optional<UserDTO> findByUsername(String username) {
        return userRepository.findByUsername(username).map(UserMapper::toDTO);
    }

    @Override
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(UserMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public UserDTO saveUser(UserDTO userDTO) {
        User user = UserMapper.toEntity(userDTO);
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        return UserMapper.toDTO(userRepository.save(user));
    }

    @Override
    public UserDTO updateUser(UserDTO userDTO) {
        User existingUser = userRepository.findById(userDTO.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        User updatedUser = UserMapper.toEntity(userDTO);
        if (userDTO.getPassword() != null && !userDTO.getPassword().isEmpty()) {
            updatedUser.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        } else {
            updatedUser.setPassword(existingUser.getPassword());
        }
        
        return UserMapper.toDTO(userRepository.save(updatedUser));
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public List<ProjectDTO> getAllProjectHistory() {
        return projectRepository.findAll().stream()
                .map(project -> ProjectDTO.builder()
                        .id(project.getId())
                        .name(project.getName())
                        .startDate(project.getStartDate())
                        .endDate(project.getEndDate())
                        .status(project.getStatus().name())
                        .clientId(project.getClient() != null ? project.getClient().getId() : null)
                        .cardId(project.getCard() != null ? project.getCard().getId() : null)
                        .build())
                .collect(Collectors.toList());
    }

}
