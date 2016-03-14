package ch.fhnw.wodss.domain;

import java.util.UUID;

public class AttachmentFactory {

	private static AttachmentFactory instance;

	private AttachmentFactory() {
		super();
	}

	/**
	 * Gets the reference of this object.
	 * 
	 * @return the reference of this object.
	 */
	public static synchronized AttachmentFactory getInstance() {
		if (instance == null) {
			instance = new AttachmentFactory();
		}
		return instance;
	}

	/**
	 * Creates an attachment;
	 * 
	 * @return the attachment to create.
	 */
	public Attachment createAttachment() {
		return createAttachment(null);
	}
	
	/**
	 * Creates an attachment;
	 * 
	 * @return the attachment to create.
	 */
	public Attachment createAttachment(String extension) {
		Attachment attachment = new Attachment();
		String id = UUID.randomUUID().toString();
		attachment.setId(id);
		if(attachment.getFile().exists()){
			// in the seldom case the
			// generated id is already reserved.
			return createAttachment(extension);
		}
		if(extension != null){
			attachment.setExtension(extension);
		}
		return attachment;
	}
	
	

}
