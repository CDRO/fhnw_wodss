package ch.fhnw.wodss.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ch.fhnw.wodss.domain.Task;
import ch.fhnw.wodss.repository.TaskRepository;

@Component
public class TaskService {

	@Autowired
	private TaskRepository taskRepository;
	
	public TaskService(){
		super();
	}
	
	public Task saveTask(Task task){
		return taskRepository.save(task);
	}
	
	public void deleteTask(Task task){
		taskRepository.delete(task);
	}
}
