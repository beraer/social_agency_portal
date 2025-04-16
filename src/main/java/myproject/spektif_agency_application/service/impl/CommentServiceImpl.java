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
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepo;
    private final UserRepository userRepo;
    private final ProjectRepository projectRepo;

    @Override
    @Transactional
    public CommentDTO addComment(CommentDTO dto) {
        User author = userRepo.findById(dto.getAuthorId())
            .orElseThrow(() -> new RuntimeException("User not found with id: " + dto.getAuthorId()));
        Project project = projectRepo.findById(dto.getProjectId())
            .orElseThrow(() -> new RuntimeException("Project not found with id: " + dto.getProjectId()));

        Comment comment = CommentMapper.toEntity(dto, author, project);
        Comment saved = commentRepo.save(comment);
        return CommentMapper.toDTO(saved);
    }

    @Override
    public List<CommentDTO> getCommentsByProjectId(Long projectId) {
        return commentRepo.findByProjectIdOrderByCreatedAtDesc(projectId)
                .stream()
                .map(CommentMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<CommentDTO> getCommentsByUserId(Long userId) {
        return commentRepo.findByUserIdOrderByCreatedAtDesc(userId)
                .stream()
                .map(CommentMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteComment(Long commentId, Long userId) {
        Comment comment = commentRepo.findById(commentId)
            .orElseThrow(() -> new RuntimeException("Comment not found with id: " + commentId));

        if (!comment.getUser().getId().equals(userId)) {
            throw new RuntimeException("User not authorized to delete this comment");
        }

        commentRepo.delete(comment);
    }
}
