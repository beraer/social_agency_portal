package myproject.spektif_agency_application.repository;

import myproject.spektif_agency_application.model.Attachment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttachmentRepository extends JpaRepository<Attachment, Long> {
} 