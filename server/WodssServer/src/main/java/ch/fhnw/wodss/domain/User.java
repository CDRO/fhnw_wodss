package ch.fhnw.wodss.domain;

import java.util.LinkedList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

import org.apache.commons.lang3.builder.HashCodeBuilder;

@Entity
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String name;
	private String email;
	private String hashedPassword;
	private boolean isValidated;
	@OneToMany(fetch = FetchType.EAGER, mappedBy = "assignee")
	private List<Task> tasks;
	@ManyToMany
	private List<Board> boards;

	User() {
		super();
		tasks = new LinkedList<>();
		boards = new LinkedList<>();
	}

	/**
	 * Gets the user's id.
	 * 
	 * @return the user's id.
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * Sets the user's id.
	 * 
	 * @param id
	 *            the id to set.
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * Gets the name of the user.
	 * 
	 * @return the user's name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the user's name.
	 * 
	 * @param name
	 *            the name to set.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Gets the user's email address.
	 * 
	 * @return the user's email address.
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * Sets the user's email address.
	 * 
	 * @param email
	 *            the user's email address to set.
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * Whether the user has successfully validated his email address.
	 * 
	 * @return <code>true</code> if user's email address has been successfully
	 *         validated.<br>
	 *         <code>false</code> otherwise.
	 */
	public boolean isValidated() {
		return isValidated;
	}

	/**
	 * Sets whether the user has been validated or not.
	 * 
	 * @param isValidated
	 *            <code>true</code> for user has successfully validated his
	 *            email address.<br>
	 *            <code>false</code> otherwise.
	 */
	public void setValidated(boolean isValidated) {
		this.isValidated = isValidated;
	}

	/**
	 * Gets the user's hashed password.
	 * 
	 * @return the user's hashed password.
	 */
	public String getHashedPassword() {
		return hashedPassword;
	}

	/**
	 * Sets the user's hashed password.
	 * 
	 * @param hashedPassword
	 *            The hashed password to set.
	 */
	public void setHashedPassword(String hashedPassword) {
		this.hashedPassword = hashedPassword;
	}

	/**
	 * Get's the tasks the user is assigned to.
	 * 
	 * @return the tasks the user is assigned to.
	 */
	public List<Task> getTasks() {
		return tasks;
	}

	/**
	 * Gets all boards of this user.
	 * 
	 * @return the boards of this user.
	 */
	public List<Board> getBoards() {
		return boards;
	}

	/**
	 * Sets the boards of this user.
	 * 
	 * @param boards
	 *            the boards to set
	 */
	public void setBoards(List<Board> boards) {
		this.boards = boards;
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

	@Override
	public boolean equals(Object object) {
		if (!(object instanceof User)) {
			return false;
		}
		User user = (User) object;
		return user.getId() == this.id;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 31).append(id).append(name).append(email).append(hashedPassword)
				.append(isValidated).toHashCode();
	}
}
