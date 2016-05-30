package ch.fhnw.wodss.service;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import ch.fhnw.wodss.domain.Attachment;
import ch.fhnw.wodss.domain.AttachmentFactory;
import ch.fhnw.wodss.domain.Board;
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
			task.setCreationDate(new Date());
			task.setState(TaskState.TODO);
		}
		if (files != null) {
			for (MultipartFile file : files) {
				// get extension
				String extension = null;
				String[] split = file.getOriginalFilename().split("\\.");
				if (split.length > 0) {
					extension = split[split.length - 1];
				}

				Attachment anAttachment = AttachmentFactory.getInstance().createAttachment(task, extension);
				anAttachment.setDocumentName(file.getOriginalFilename());

				// add attachment to task when saving to file system was
				// successful.
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
		for (Attachment attachment : task.getAttachments()) {
			attachmentService.deleteAttachmentFromFileSystem(attachment);
		}
	}

	@Transactional
	public void deleteTask(Integer id) {
		Task task = taskRepository.getOne(id);
		deleteTask(task);
	}

	@Transactional(readOnly = true)
	public List<Task> getAll() {
		return taskRepository.findAll();
	}

	@Transactional(readOnly = true)
	public Task getById(Integer id) {
		return taskRepository.findOne(id);
	}

	/**
	 * Gets all the tasks of all the boards specified.
	 * 
	 * @param boards
	 *            the boards for which all the tasks should be returned.
	 * @return the tasks of all the boards specified.
	 */
	@Transactional(readOnly = true)
	public List<Task> getByBoards(Set<Board> boards) {
		List<Task> taskList = new LinkedList<>();
		for (Board board : boards) {
			List<Task> tasks = taskRepository.findByBoard(board);
			taskList.addAll(tasks);
		}
		return taskList;
	}

	/**
	 * Gets all the tasks of one specified board.
	 * 
	 * @param board
	 *            the board for which the tasks should be returned.
	 * @return the list of tasks for the specified board.
	 */
	@Transactional(readOnly = true)
	public List<Task> getByBoard(Board board) {
		return taskRepository.findByBoard(board);
	}
}
