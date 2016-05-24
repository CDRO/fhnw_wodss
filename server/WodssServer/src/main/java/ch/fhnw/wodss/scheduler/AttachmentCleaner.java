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
	 * Removes unreferenced attachments every 20 minutes.
	 */
	//@Scheduled(fixedDelay = 1200000L)
	@Scheduled(fixedDelay = 60000L)
	public void cleanUnreferencedAttachments(){
		LOG.info("Removing unreferenced attachments...");
		List<Attachment> attachments = attachmentService.getAllByTaskId(null);
		attachments.forEach(attachment -> attachmentService.deleteAttachment(attachment));		
	}
	
}
