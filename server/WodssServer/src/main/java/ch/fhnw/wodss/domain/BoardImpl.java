package ch.fhnw.wodss.domain;

import java.util.List;

import org.apache.commons.lang3.builder.HashCodeBuilder;

class BoardImpl implements Board {

	private Integer id;
	private String title;
	private List<Task> tasks;
	private List<User> users;

	BoardImpl() {
		super();
	}

	@Override
	public Integer getId() {
		return id;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
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
	public void setTasks(List<Task> tasks) {
		this.tasks = tasks;
	}

	@Override
	public List<User> getUsers() {
		return users;
	}

	@Override
	public void setUsers(List<User> users) {
		this.users = users;
	}

	@Override
	public void addUser(User user) {
		if (!users.contains(user)) {
			users.add(user);
		}
	}

	@Override
	public void removeUser(User user) {
		users.remove(user);
	}

	@Override
	public void addTask(Task task) {
		if (!tasks.contains(task)) {
			tasks.add(task);
		}
	}

	@Override
	public void removeTask(Task task) {
		tasks.remove(task);
	}

	@Override
	public boolean equals(Object object) {
		if (!(object instanceof Board)) {
			return false;
		}
		Board board = (Board) object;
		return board.getId() == this.id;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 31).append(id).append(title).append(tasks).append(users).toHashCode();
	}

}
