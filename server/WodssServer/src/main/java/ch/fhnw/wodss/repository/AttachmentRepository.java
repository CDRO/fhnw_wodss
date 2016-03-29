package ch.fhnw.wodss.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ch.fhnw.wodss.domain.Attachment;

public interface AttachmentRepository extends JpaRepository<Attachment, String> {

}
