package ch.fhnw.wodss.domain;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

/**
 * The board that holds tasks.
 * 
 * @author tobias
 *
 */
@Entity
public interface Board {

	/**
	 * Gets the board id.
	 * 
	 * @return the board id.
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer getId();

	/**
	 * Sets the board's id.
	 * 
	 * @param id
	 *            the id to set.
	 */
	public void setId(Integer id);

	/**
	 * Sets the title of the board.
	 * 
	 * @param title
	 *            the title to set.
	 */
	public void setTitle(String title);

	/**
	 * Gets the title of the board.
	 * 
	 * @return the title of the board.
	 */
	@Column
	public String getTitle();

	/**
	 * Gets all tasks of this board.
	 * 
	 * @return the list of all tasks.
	 */
	@OneToMany
	public List<Task> getTasks();
	
	/**
	 * Sets the list of tasks.
	 * @param tasks the list of tasks to set.
	 */
	public void setTasks(List<Task> tasks);

	/**
	 * Gets all users that have permission to see this board.
	 * 
	 * @return the list of users that have permission to see this board.
	 */
	@ManyToMany
	public List<User> getUsers();
	
	/**
	 * Sets the list of users.
	 * @param users the list of users to set.
	 */
	public void setUsers(List<User> users);
	
	/**
	 * Permits a user for this board.
	 * 
	 * @param user
	 *            the user to give permission for this board.
	 */
	public void addUser(User user);

	/**
	 * Removes a user from this board.
	 * 
	 * @param user
	 *            the user to delete.
	 */
	public void removeUser(User user);

	/**
	 * Adds a task to this board.
	 * 
	 * @param task
	 *            the task to add.
	 */
	public void addTask( Task task);
	

	/**
	 * Removes the task from this board.
	 * 
	 * @param task
	 *            the task to remove.
	 */
	public void removeTask(Task task);

}
