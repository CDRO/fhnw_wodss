package ch.fhnw.wodss.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import ch.fhnw.wodss.domain.Task;
import ch.fhnw.wodss.repository.TaskRepository;

@Component
@Transactional
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
	
	public void deleteTask(Integer id){
		taskRepository.delete(id);
	}
	
	public List<Task> getAll(){
		return taskRepository.findAll();
	}
	
	public Task getById(Integer id){
		return taskRepository.findOne(id);
	}
}
