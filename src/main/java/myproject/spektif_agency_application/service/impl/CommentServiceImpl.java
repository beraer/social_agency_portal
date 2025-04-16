package myproject.spektif_agency_application.service.impl;

import lombok.RequiredArgsConstructor;
import myproject.spektif_agency_application.dto.CommentDTO;
import myproject.spektif_agency_application.mapper.CommentMapper;
import myproject.spektif_agency_application.model.Comment;
import myproject.spektif_agency_application.model.Project;
import myproject.spektif_agency_application.model.User;
import myproject.spektif_agency_application.repository.*;
import myproject.spektif_agency_application.service.CommentService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepo;
    private final UserRepository userRepo;
    private final ProjectRepository projectRepo;

    @Override
    public CommentDTO postComment(CommentDTO dto) {
        User author = userRepo.findById(dto.getAuthorId()).orElseThrow();
        Project project = projectRepo.findById(dto.getProjectId()).orElseThrow();

        Comment comment = CommentMapper.toEntity(dto, author, project);
        comment.setCreatedAt(LocalDateTime.now());

        return CommentMapper.toDTO(commentRepo.save(comment));
    }

    @Override
    public List<CommentDTO> getCommentsByProject(Long projectId) {
        return commentRepo.findByProjectId(projectId)
                .stream()
                .map(CommentMapper::toDTO)
                .collect(Collectors.toList());
    }
}
