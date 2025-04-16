package myproject.spektif_agency_application.repository;

import myproject.spektif_agency_application.dto.CommentDTO;
import myproject.spektif_agency_application.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByProjectIdOrderByCreatedAtDesc(Long projectId);
    List<Comment> findByUserIdOrderByCreatedAtDesc(Long userId);
    Arrays findByProjectId(Long projectId);
}
