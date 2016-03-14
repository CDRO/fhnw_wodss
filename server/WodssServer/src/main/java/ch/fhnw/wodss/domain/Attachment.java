package ch.fhnw.wodss.domain;

import java.io.File;

import javax.persistence.Entity;
import javax.persistence.Id;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.springframework.beans.factory.annotation.Value;

@Entity
public class Attachment {
	
	@Value("${wodss.attachment.root.path}")
	private String attachmentRootPath;

	/**
	 * The id of the attachment. A UUID.
	 */
	@Id
	private String id;
	
	/**
	 * The name of the document for display layer.
	 */
	private String documentName;
	
	Attachment(){
		super();
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the file that can be determined via id.
	 */
	public File getFile(){
		
		StringBuilder sb = new StringBuilder();
		
		// append attachments root path
		sb.append(attachmentRootPath);
		
		// get first part of the UUID
		String firstPart = id.split("-")[0];
		
		// split the first part again into two byte parts.
		// these will be our sub directories.
		byte[] bytes = firstPart.getBytes();
		for(int i = 0; i < bytes.length; i = i + 2){
			byte[] twoBytes = new byte[2];
			twoBytes[0] = bytes[i];
			twoBytes[1] = bytes[i + 1];
			sb.append(new String(twoBytes));
			sb.append(File.separator);
		}
		
		sb.append(id);
		
		File file = new File(sb.toString());
		
		return file;
	}

	/**
	 * @return the documentName
	 */
	public String getDocumentName() {
		return documentName;
	}

	/**
	 * @param documentName the documentName to set
	 */
	public void setDocumentName(String documentName) {
		this.documentName = documentName;
	}
	
	@Override
	public boolean equals(Object object){
		if(!(object instanceof Attachment)){
			return false;
		}
		Attachment attachment = (Attachment) object;
		return attachment.getId() == this.id;
	}
	
	@Override
	public int hashCode(){
		return new HashCodeBuilder(17,31). 
				append(id). 
				append(documentName). 
				toHashCode();
	}
	
}
