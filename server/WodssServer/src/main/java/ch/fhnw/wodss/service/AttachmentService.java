package ch.fhnw.wodss.service;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import ch.fhnw.wodss.domain.Attachment;
import ch.fhnw.wodss.repository.AttachmentRepository;

@Component
public class AttachmentService {

	@Autowired
	private AttachmentRepository attachmentRepository;

	public AttachmentService() {
		super();
	}
	
	public Attachment getAttachment(String id){
		return attachmentRepository.getOne(id);
	}
	
	@Transactional
	public void deleteAttachment(Attachment attachment){
		attachmentRepository.delete(attachment);
	}
	
	/**
	 * Saved the uploaded file to file system
	 * @param attachment the attachment object to retrieve path from uuid
	 * @param file the file to save.
	 * @return success
	 */
	public boolean saveAttachmentToFileSystem(Attachment attachment, MultipartFile file) {
		FileOutputStream fos = null;
		try {
			FileUtils.forceMkdir(attachment.getFile().getParentFile());
			fos = new FileOutputStream(attachment.getFile());
			fos.write(file.getBytes());
			return true;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(fos != null){
				try {
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
					return false;
				}
			}
		}
		return false;
	}

	
	public boolean deleteAttachmentFromFileSystem(Attachment attachment) {
		try {
			FileUtils.forceDelete(attachment.getFile());
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
}
