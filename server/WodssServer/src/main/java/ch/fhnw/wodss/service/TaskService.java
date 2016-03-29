package ch.fhnw.wodss.service;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import ch.fhnw.wodss.domain.Attachment;
import ch.fhnw.wodss.domain.AttachmentFactory;
import ch.fhnw.wodss.domain.Task;
import ch.fhnw.wodss.domain.TaskState;
import ch.fhnw.wodss.repository.TaskRepository;

@Component
@Transactional
public class TaskService {

	@Autowired
	private TaskRepository taskRepository;

	public TaskService() {
		super();
	}
	
	public Task saveTask(Task task) {
		return saveTask(task, null);
	}

	public Task saveTask(Task task, List<MultipartFile> files) {
		if (task.getState() == null) {
			task.setState(TaskState.TODO);
		}
		if (files != null) {
			for(MultipartFile file : files){				
				// get extension
				String extension = null;
				String[] split = file.getOriginalFilename().split("\\.");
				if(split.length > 0){
					extension = split[split.length - 1];
				}
				
				Attachment anAttachment = AttachmentFactory.getInstance().createAttachment(extension);
				anAttachment.setDocumentName(file.getOriginalFilename());
				
				// add attachment to task when saving to file system was successful.
				if (saveAttachmentToFileSystem(anAttachment, file)) {
					task.getAttachments().add(anAttachment);
				}
			}
		}
		return taskRepository.save(task);
	}

	public void deleteTask(Task task) {
		taskRepository.delete(task);
		for(Attachment attachment : task.getAttachments()){
			deleteAttachmentFromFileSystem(attachment);
		}
	}

	public void deleteTask(Integer id) {
		Task task = taskRepository.getOne(id);
		deleteTask(task);
	}

	public List<Task> getAll() {
		return taskRepository.findAll();
	}

	public Task getById(Integer id) {
		return taskRepository.findOne(id);
	}

	/**
	 * Saved the uploaded file to file system
	 * @param attachment the attachment object to retrieve path from uuid
	 * @param file the file to save.
	 * @return success
	 */
	private boolean saveAttachmentToFileSystem(Attachment attachment, MultipartFile file) {
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

	
	private boolean deleteAttachmentFromFileSystem(Attachment attachment) {
		try {
			FileUtils.forceDelete(attachment.getFile());
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
}
