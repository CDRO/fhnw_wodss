package ch.fhnw.wodss.scheduler;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import ch.fhnw.wodss.domain.Attachment;
import ch.fhnw.wodss.service.AttachmentService;

@Component
public class AttachmentCleaner {

	public final static Logger LOG = LoggerFactory.getLogger(AttachmentCleaner.class);

	@Autowired
	private AttachmentService attachmentService;

	/**
	 * Removes attachments from the file system and database which have no
	 * reference to tasks anymore, every 20 Minutes
	 */
	@Scheduled(fixedDelay = 1200000L)
	public void cleanAttachments() {
		LOG.info("Removing old attachments...");
		List<Attachment> attachments = attachmentService.getAllByTaskId(null);
		for (Attachment attachment : attachments) {
			attachmentService.deleteAttachmentFromFileSystem(attachment);
			attachmentService.deleteAttachment(attachment);
		}
	}

}
