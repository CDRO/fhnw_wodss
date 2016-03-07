package ch.fhnw.wodss.domain.wrapper;

import ch.fhnw.wodss.domain.Task;
import ch.fhnw.wodss.security.Token;

public class TaskContext {

	private Token token;
	private Task task;
	
	public TaskContext(){
		super();
	}

	/**
	 * @return the token
	 */
	public Token getToken() {
		return token;
	}

	/**
	 * @param token the token to set
	 */
	public void setToken(Token token) {
		this.token = token;
	}

	/**
	 * @return the task
	 */
	public Task getTask() {
		return task;
	}

	/**
	 * @param task the task to set
	 */
	public void setTask(Task task) {
		this.task = task;
	}
	
	
	
}
