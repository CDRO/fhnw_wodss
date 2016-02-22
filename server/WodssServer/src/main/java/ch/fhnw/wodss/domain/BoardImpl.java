package ch.fhnw.wodss.domain;

import java.util.List;

class BoardImpl implements Board {
	
	private String title;
	private List<Task> tasks;
	private List<User> users;
	
	BoardImpl(){
		super();
	}

	@Override
	public void setTitle(String title) {
		this.title = title;
	}

	@Override
	public String getTitle() {
		return title;
	}

	@Override
	public List<Task> getTasks() {
		return tasks;
	}

	@Override
	public void saveTask(Task task) {
		// TODO Auto-generated method stub
	}

	@Override
	public void deleteTask(Task task) {
		// TODO Auto-generated method stub
	}

	@Override
	public List<User> getUsers() {
		return users;
	}

	@Override
	public void addUser(User user) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteUser(User user) {
		// TODO Auto-generated method stub
		
	}

}
