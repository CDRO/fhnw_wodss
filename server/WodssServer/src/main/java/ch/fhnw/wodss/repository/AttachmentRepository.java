package ch.fhnw.wodss.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import ch.fhnw.wodss.domain.Attachment;

public interface AttachmentRepository extends JpaRepository<Attachment, String> {

	List<Attachment> findByTaskId(Integer taskId);
	
}
