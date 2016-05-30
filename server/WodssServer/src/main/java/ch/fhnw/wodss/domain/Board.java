package ch.fhnw.wodss.domain;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Board {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	@NotNull
	private String title;
	@ManyToOne
	@NotNull
	private User owner;
	@OneToMany(mappedBy = "board", fetch = FetchType.EAGER)
	@JsonIgnore
	private List<Task> tasks;
	@ManyToMany(fetch = FetchType.EAGER)
	private Set<User> users;

	Board() {
		super();
		users = new HashSet<>();
	}

	/**
	 * Gets the board id.
	 * 
	 * @return the board id.
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * Sets the board's id.
	 * 
	 * @param id
	 *            the id to set.
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * Sets the title of the board.
	 * 
	 * @param title
	 *            the title to set.
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * Gets the title of the board.
	 * 
	 * @return the title of the board.
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Sets the owner of this board.
	 * 
	 * @param owner
	 *            the owner to set.
	 */
	public void setOwner(User owner) {
		this.owner = owner;
	}

	/**
	 * Returns the owner of this user.
	 * 
	 * @return the owner.
	 */
	public User getOwner() {
		return owner;
	}

	/**
	 * Sets the list of tasks.
	 * 
	 * @param tasks
	 *            the list of tasks to set.
	 */
	public void setTasks(List<Task> tasks) {
		this.tasks = tasks;
	}

	/**
	 * Gets all users that have permission to see this board.
	 * 
	 * @return the list of users that have permission to see this board.
	 */
	public Set<User> getUsers() {
		return users;
	}

	/**
	 * Sets the list of users.
	 * 
	 * @param users
	 *            the list of users to set.
	 */
	public void setUsers(Set<User> users) {
		this.users = users;
	}

	/**
	 * Permits a user for this board.
	 * 
	 * @param user
	 *            the user to give permission for this board.
	 */
	public void addUser(User user) {
		users.add(user);
	}

	/**
	 * Removes a user from this board.
	 * 
	 * @param user
	 *            the user to delete.
	 */
	public void removeUser(User user) {
		users.remove(user);
	}

	/**
	 * Adds a task to this board.
	 * 
	 * @param task
	 *            the task to add.
	 */
	public void addTask(Task task) {
		if (!tasks.contains(task)) {
			tasks.add(task);
		}
	}

	/**
	 * Removes the task from this board.
	 * 
	 * @param task
	 *            the task to remove.
	 */
	public void removeTask(Task task) {
		tasks.remove(task);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Board other = (Board) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}



}
