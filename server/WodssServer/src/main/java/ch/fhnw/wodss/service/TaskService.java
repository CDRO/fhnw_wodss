package ch.fhnw.wodss.service;

import java.util.List;

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
public class TaskService {

	@Autowired
	private TaskRepository taskRepository;
	
	@Autowired
	private AttachmentService attachmentService;

	public TaskService() {
		super();
	}
	
	public Task saveTask(Task task) {
		return saveTask(task, null);
	}

	@Transactional
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
				if (attachmentService.saveAttachmentToFileSystem(anAttachment, file)) {
					task.getAttachments().add(anAttachment);
				}
			}
		}
		return taskRepository.save(task);
	}

	@Transactional
	public void deleteTask(Task task) {
		taskRepository.delete(task);
		for(Attachment attachment : task.getAttachments()){
			attachmentService.deleteAttachmentFromFileSystem(attachment);
		}
	}

	@Transactional
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

	
}
