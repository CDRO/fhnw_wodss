package ch.fhnw.wodss.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import ch.fhnw.wodss.domain.Task;
import ch.fhnw.wodss.service.TaskService;

@RestController
public class TaskController {

	@Autowired
	private TaskService taskService;
	
	@RequestMapping(path = "/tasks", method = RequestMethod.GET)
    public ResponseEntity<List<Task>> getAllTasks() {
         List<Task> tasks = taskService.getAll();
         return new ResponseEntity<>(tasks, HttpStatus.OK);
    }
	
	@RequestMapping(path = "/task/{id}", method = RequestMethod.GET)
	public ResponseEntity<Task> getTask(@PathVariable Integer id) {
		 Task task = taskService.getById(id);
		 return new ResponseEntity<>(task, HttpStatus.OK);
	}
	
	@RequestMapping(path = "/task", method = RequestMethod.POST)
	public ResponseEntity<Task> createTask(@RequestBody Task task) {
		 Task savedTask = taskService.saveTask(task);
		 return new ResponseEntity<>(savedTask, HttpStatus.OK);
	}
	
	@RequestMapping(path = "/task/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<String> deleteTask(@PathVariable Integer id) {
		taskService.deleteTask(id);
		return new ResponseEntity<>("Deleted board.", HttpStatus.OK);
	}
	
	@RequestMapping(path = "/task/{id}", method = RequestMethod.PUT)
	public ResponseEntity<Task> updateTask(@PathVariable Integer id, @RequestBody Task task) {
		Task updatedTask = taskService.saveTask(task);
		return new ResponseEntity<>(updatedTask, HttpStatus.OK);
	}
	
	
}
