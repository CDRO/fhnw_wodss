package ch.fhnw.wodss.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import ch.fhnw.wodss.domain.Board;
import ch.fhnw.wodss.domain.Task;
import ch.fhnw.wodss.security.Token;
import ch.fhnw.wodss.service.TaskService;

@RestController
@CrossOrigin(origins = "http://localhost:9000")
public class TaskController {

	@Autowired
	private TaskService taskService;

	@RequestMapping(path = "/tasks", method = RequestMethod.GET)
	public ResponseEntity<List<Task>> getAllTasks(@RequestHeader(value = "x-session-token") Token token,
			@RequestBody(required = false) Board board) {
		List<Task> tasks = taskService.getAll();
		return new ResponseEntity<>(tasks, HttpStatus.OK);
	}

	@RequestMapping(path = "/task/{id}", method = RequestMethod.GET)
	public ResponseEntity<Task> getTask(@RequestHeader(value = "x-session-token") Token token,
			@PathVariable Integer id) {
		Task task = taskService.getById(id);
		return new ResponseEntity<>(task, HttpStatus.OK);
	}

	// TODO: restrict attachment mime types.
	// TODO: upload multiple attachment.
	@RequestMapping(path = "/task", method = RequestMethod.POST)
	public ResponseEntity<Task> createTask(@RequestHeader(value = "x-session-token") Token token,
			@RequestPart("task") Task task, @RequestPart(name = "attachment", required = false) MultipartFile file) {
		Task savedTask = taskService.saveTask(task, file);
		return new ResponseEntity<>(savedTask, HttpStatus.OK);
	}

	// TODO Test delete attachment
	@RequestMapping(path = "/task/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Boolean> deleteTask(@RequestHeader(value = "x-session-token") Token token,
			@PathVariable Integer id) {
		taskService.deleteTask(id);
		return new ResponseEntity<>(true, HttpStatus.OK);
	}

	// TODO: put with multiple attachments.
	// TODO: how to delete a single attachment --> attachment resource with delete
	@RequestMapping(path = "/task/{id}", method = RequestMethod.PUT)
	public ResponseEntity<Task> updateTask(@RequestHeader(value = "x-session-token") Token token,
			@RequestPart("task") Task task, @PathVariable Integer id,
			@RequestPart(name = "attachment", required = false) MultipartFile file) {
		Task updatedTask = taskService.saveTask(task, file);
		return new ResponseEntity<>(updatedTask, HttpStatus.OK);
	}

}
