package ch.fhnw.wodss.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

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
		return attachmentRepository.findOne(id);
	}
	
	@Transactional
	public void deleteAttachment(Attachment attachment){
		boolean success = deleteAttachmentFromFileSystem(attachment);
		if(success){
			attachmentRepository.delete(attachment);
		}
	}
	
	@Transactional
	public Attachment saveAttachment(Attachment attachment){
		return attachmentRepository.save(attachment);
	}
	
	@Transactional(readOnly = true)
	public List<Attachment> getAllByTaskId(Integer taskId){
		return attachmentRepository.findByTaskId(taskId);
	}
	
	/**
	 * Saved the uploaded file to file system
	 * @param attachment the attachment object to retrieve path from uuid
	 * @param file the file to save.
	 * @return success
	 */
	public boolean saveAttachmentToFileSystem(Attachment attachment, MultipartFile file) {
		try {
			return saveAttachmentToFileSystem(attachment, file.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * Saved the uploaded file to file system
	 * @param attachment the attachment object to retrieve path from uuid
	 * @param file the file to save.
	 * @return success
	 */
	public boolean saveAttachmentToFileSystem(Attachment attachment, File file) {
		try {
			Path path = Paths.get(file.toURI());
			byte[] bytes = Files.readAllBytes(path);
			return saveAttachmentToFileSystem(attachment, bytes);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * Saved the uploaded file to file system
	 * @param attachment the attachment object to retrieve path from uuid
	 * @param bytes the file in bytes to save.
	 * @return success
	 */
	public boolean saveAttachmentToFileSystem(Attachment attachment, byte[] bytes) {
		FileOutputStream fos = null;
		try {
			FileUtils.forceMkdir(attachment.getFile().getParentFile());
			fos = new FileOutputStream(attachment.getFile());
			fos.write(bytes);
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
