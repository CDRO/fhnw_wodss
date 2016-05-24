package ch.fhnw.wodss.controller;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import ch.fhnw.wodss.domain.Attachment;
import ch.fhnw.wodss.domain.Board;
import ch.fhnw.wodss.domain.Task;
import ch.fhnw.wodss.domain.TaskState;
import ch.fhnw.wodss.domain.User;
import ch.fhnw.wodss.notification.AbstractNotification;
import ch.fhnw.wodss.notification.NewTaskNotification;
import ch.fhnw.wodss.notification.ReassignedNotification;
import ch.fhnw.wodss.security.Token;
import ch.fhnw.wodss.security.TokenHandler;
import ch.fhnw.wodss.service.AttachmentService;
import ch.fhnw.wodss.service.TaskService;
import ch.fhnw.wodss.service.UserService;

@RestController
@CrossOrigin
public class TaskController {

	private static final Logger LOG = LoggerFactory.getLogger(TaskController.class);

	@Autowired
	private TaskService taskService;

	@Autowired
	private UserService userService;

	@Autowired
	private AttachmentService attachmentService;

	/**
	 * Gets the list of tasks of one or all board the user is subscribed to.
	 * 
	 * @param token
	 *            the token to verify that the user is logged in.
	 * @param board
	 *            the board for which the tasks should be retrieved. If
	 *            <code>null</code> all tasks of all user boards will be
	 *            retrieved.
	 * @return the list of tasks.
	 */
	@RequestMapping(path = "/tasks", method = RequestMethod.GET)
	public ResponseEntity<List<Task>> getAllTasks(@RequestHeader(value = "x-session-token") Token token,
			@RequestParam(name = "boardId", required = false) Board board) {
		User user = TokenHandler.getUser(token.getId());
		// reload user from db
		user = userService.getById(user.getId());
		if (board == null) {
			// give me all the task of all the boards a I am subscribed.
			List<Task> tasks = taskService.getByBoards(user.getBoards());
			LOG.debug("User <{}> requested all tasks from all boards", user.getEmail());
			return new ResponseEntity<>(tasks, HttpStatus.OK);
		}
		if (user.getBoards().contains(board)) {
			List<Task> tasks = taskService.getByBoard(board);
			LOG.debug("User <{}> requested all tasks of board <{}>", user.getEmail(), board.getId());
			return new ResponseEntity<>(tasks, HttpStatus.OK);
		}
		LOG.debug("User <{}> requested all tasks of board <{}> but is not authorized", user.getEmail(), board.getId());
		return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
	}

	/**
	 * Gets the task specified by id.
	 * 
	 * @param token
	 *            the security token to verify user is logged in.
	 * @param id
	 *            the id of the task.
	 * @return the task.
	 */
	@RequestMapping(path = "/task/{id}", method = RequestMethod.GET)
	public ResponseEntity<Task> getTask(@RequestHeader(value = "x-session-token") Token token,
			@PathVariable Integer id) {
		User user = TokenHandler.getUser(token.getId());
		// reload user from db
		user = userService.getById(user.getId());
		Task task = taskService.getById(id);
		if (user.getBoards().contains(task.getBoard())) {
			LOG.debug("User <{}> requested task <{}>", user.getEmail(), task.getId());
			return new ResponseEntity<>(task, HttpStatus.OK);
		}
		LOG.debug("User <{}> requested task <{}> but is not authorized", user.getEmail(), task.getId());
		return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
	}

	/**
	 * Creates a new task in a board. To create a new task, the user must be
	 * subscribed for the board, where the task should be associated with.
	 * 
	 * @param token
	 *            The security token to verify the user is logged in.
	 * @param task
	 *            The task to create.
	 * @param files
	 *            The attachments for the task.
	 * @return The saved task that contains an ID from database.
	 */
	// TODO: restrict attachment mime types.
	@RequestMapping(path = "/task", method = RequestMethod.POST, headers = "Content-Type=multipart/*")
	public ResponseEntity<Task> createTask(@RequestHeader(value = "x-session-token") Token token,
			@RequestPart("info") Task task, @RequestPart(name = "file[0]") MultipartFile file0,
			@RequestPart(name = "file[1]", required = false) MultipartFile file1,
			@RequestPart(name = "file[2]", required = false) MultipartFile file2,
			@RequestPart(name = "file[3]", required = false) MultipartFile file3,
			@RequestPart(name = "file[4]", required = false) MultipartFile file4,
			@RequestPart(name = "file[5]", required = false) MultipartFile file5,
			@RequestPart(name = "file[6]", required = false) MultipartFile file6,
			@RequestPart(name = "file[7]", required = false) MultipartFile file7,
			@RequestPart(name = "file[8]", required = false) MultipartFile file8,
			@RequestPart(name = "file[9]", required = false) MultipartFile file9) {
		User user = TokenHandler.getUser(token.getId());
		// reload user from db
		user = userService.getById(user.getId());
		if (user.getBoards().contains(task.getBoard())) {
			List<MultipartFile> files = new LinkedList<>();
			if (file0 != null) {
				files.add(file0);
			}
			if (file1 != null) {
				files.add(file1);
			}
			if (file2 != null) {
				files.add(file2);
			}
			if (file3 != null) {
				files.add(file3);
			}
			if (file4 != null) {
				files.add(file4);
			}
			if (file5 != null) {
				files.add(file5);
			}
			if (file6 != null) {
				files.add(file6);
			}
			if (file7 != null) {
				files.add(file7);
			}
			if (file8 != null) {
				files.add(file8);
			}
			if (file9 != null) {
				files.add(file9);
			}
			Task savedTask = taskService.saveTask(task, files);
			LOG.info("User <{}> saved task <{}> with attachments", user.getEmail(), task.getId());
			// sending notification when assignee is not the same
			if (task.getAssignee() != null && !task.getAssignee().equals(user)) {
				AbstractNotification notification = new NewTaskNotification(savedTask);
				notification.send();
			}
			return new ResponseEntity<>(savedTask, HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
	}

	/**
	 * Creates a new task in a board. To create a new task, the user must be
	 * subscribed for the board, where the task should be associated with.
	 * 
	 * @param token
	 *            The security token to verify the user is logged in.
	 * @param task
	 *            The task to create.
	 * @return The saved task that contains an ID from database.
	 */
	// TODO: restrict attachment mime types.
	@RequestMapping(path = "/task", method = RequestMethod.POST)
	public ResponseEntity<Task> createTaskWithoutMultiPart(@RequestHeader(value = "x-session-token") Token token,
			@RequestBody Task task) {
		User user = TokenHandler.getUser(token.getId());
		// reload user from db
		user = userService.getById(user.getId());
		if (user.getBoards().contains(task.getBoard())) {
			Task savedTask = taskService.saveTask(task);
			LOG.info("User <{}> saved task <{}>", user.getEmail(), task.getId());
			// sending notification when assignee is not the same
			if (task.getAssignee() != null && !task.getAssignee().equals(user)) {
				AbstractNotification notification = new NewTaskNotification(savedTask);
				notification.send();
			}
			return new ResponseEntity<>(savedTask, HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
	}

	/**
	 * Deletes a task from its board, but only if the user is subscribed to this
	 * board.
	 * 
	 * @param token
	 *            The security token to verify whether user is logged in.
	 * @param id
	 *            The Task ID
	 * @return <code>true</code> if the task could be deleted,
	 *         <code>false</code> otherwise.
	 */
	// TODO Test delete attachment
	@RequestMapping(path = "/task/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Boolean> deleteTask(@RequestHeader(value = "x-session-token") Token token,
			@PathVariable Integer id) {
		Task task = taskService.getById(id);
		User user = TokenHandler.getUser(token.getId());
		// reload user from db
		user = userService.getById(user.getId());
		if (user.getBoards().contains(task.getBoard())) {
			taskService.deleteTask(id);
			LOG.info("User <{}> deleted task <{}>", user.getEmail(), task.getId());
			return new ResponseEntity<>(true, HttpStatus.OK);
		}
		return new ResponseEntity<>(false, HttpStatus.UNAUTHORIZED);
	}

	/**
	 * Modifies a created task. This should only be possible if the user is
	 * subscribed to the board that contains the task to modify.
	 * 
	 * @param token
	 *            The security token to verify whether the user is logged in.
	 * @param task
	 *            The modified task to save.
	 * @param id
	 *            The id of the task to modify.
	 * @param files
	 *            The attachments of the task.
	 * @return The modified task.
	 */
	@RequestMapping(path = "/task/{id}", method = RequestMethod.PUT, headers = "Content-Type=multipart/*")
	public ResponseEntity<Task> updateTask(@RequestHeader(value = "x-session-token") Token token,
			@RequestPart(name = "info") Task task, @PathVariable Integer id,
			@RequestPart(name = "file[0]") MultipartFile file0,
			@RequestPart(name = "file[1]", required = false) MultipartFile file1,
			@RequestPart(name = "file[2]", required = false) MultipartFile file2,
			@RequestPart(name = "file[3]", required = false) MultipartFile file3,
			@RequestPart(name = "file[4]", required = false) MultipartFile file4,
			@RequestPart(name = "file[5]", required = false) MultipartFile file5,
			@RequestPart(name = "file[6]", required = false) MultipartFile file6,
			@RequestPart(name = "file[7]", required = false) MultipartFile file7,
			@RequestPart(name = "file[8]", required = false) MultipartFile file8,
			@RequestPart(name = "file[9]", required = false) MultipartFile file9) {
		User user = TokenHandler.getUser(token.getId());
		// reload user from db
		user = userService.getById(user.getId());
		if (user.getBoards().contains(task.getBoard())) {
			List<MultipartFile> files = new LinkedList<>();
			if (file0 != null) {
				files.add(file0);
			}
			if (file1 != null) {
				files.add(file1);
			}
			if (file2 != null) {
				files.add(file2);
			}
			if (file3 != null) {
				files.add(file3);
			}
			if (file4 != null) {
				files.add(file4);
			}
			if (file5 != null) {
				files.add(file5);
			}
			if (file6 != null) {
				files.add(file6);
			}
			if (file7 != null) {
				files.add(file7);
			}
			if (file8 != null) {
				files.add(file8);
			}
			if (file9 != null) {
				files.add(file9);
			}
			Task oldTask = taskService.getById(task.getId());
			// set done date if set to done
			// remove otherwise
			if (task.getState() == TaskState.DONE) {
				task.setDoneDate(new Date());
			} else {
				task.setDoneDate(null);
			}
			removeAttachments(oldTask, task);
			setTaskOnAttachments(task);
			Task updatedTask = taskService.saveTask(task, files);
			LOG.info("User <{}> updated task <{}> with attachments.", user.getEmail(), task.getId());
			if (oldTask.getAssignee() != null && task.getAssignee() != null
					&& !oldTask.getAssignee().equals(task.getAssignee())) {
				AbstractNotification notification = new ReassignedNotification(updatedTask);
				notification.send();
			}
			return new ResponseEntity<>(updatedTask, HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
	}

	/**
	 * Modifies a created task. This should only be possible if the user is
	 * subscribed to the board that contains the task to modify.
	 * 
	 * @param token
	 *            The security token to verify whether the user is logged in.
	 * @param task
	 *            The modified task to save.
	 * @param id
	 *            The id of the task to modify.
	 * @return The modified task.
	 */
	@RequestMapping(path = "/task/{id}", method = RequestMethod.PUT)
	public ResponseEntity<Task> updateTaskWithoutMultiPart(@RequestHeader(value = "x-session-token") Token token,
			@RequestBody Task task) {
		User user = TokenHandler.getUser(token.getId());
		// reload user from db
		user = userService.getById(user.getId());
		if (user.getBoards().contains(task.getBoard())) {
			Task oldTask = taskService.getById(task.getId());
			if (task.getState() == TaskState.DONE) {
				task.setDoneDate(new Date());
			} else {
				task.setDoneDate(null);
			}
			removeAttachments(oldTask, task);
			setTaskOnAttachments(task);
			Task updatedTask = taskService.saveTask(task);
			LOG.info("User <{}> updated task <{}>", user.getEmail(), task.getId());
			if (oldTask.getAssignee() != null && task.getAssignee() != null
					&& !oldTask.getAssignee().equals(task.getAssignee())) {
				AbstractNotification notification = new ReassignedNotification(updatedTask);
				notification.send();
			}
			return new ResponseEntity<>(updatedTask, HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
	}

	private void removeAttachments(Task oldTask, Task updatedTask) {
		List<Attachment> attachments = oldTask.getAttachments();
		attachments.removeAll(updatedTask.getAttachments());
		attachments.forEach((Attachment attachment) -> {
			attachment.setTask(null);
			attachmentService.saveAttachment(attachment);
		});
	}

	private void setTaskOnAttachments(Task task){
		if(task.getAttachments().size() > 0){
			task.getAttachments().forEach((Attachment attachment) -> {
				if(attachment != null){
					attachment.setTask(task);
				}
			});
		}
	}

}
