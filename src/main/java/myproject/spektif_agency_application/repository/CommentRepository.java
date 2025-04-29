package myproject.spektif_agency_application.repository;

import myproject.spektif_agency_application.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
} 