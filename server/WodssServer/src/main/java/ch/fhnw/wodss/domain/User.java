package ch.fhnw.wodss.domain;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public interface User {

	/**
	 * Gets the user's id.
	 * 
	 * @return the user's id.
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer getId();

	/**
	 * Sets the user's id.
	 * 
	 * @param id
	 *            the id to set.
	 */
	public void setId(Integer id);

	/**
	 * Gets the name of the user.
	 * 
	 * @return the user's name.
	 */
	@Column
	public String getName();

	/**
	 * Sets the user's name.
	 * 
	 * @param name
	 *            the name to set.
	 */
	public void setName(String name);

	/**
	 * Gets the user's email address.
	 * 
	 * @return the user's email address.
	 */
	@Column
	public String getEmail();

	/**
	 * Sets the user's email address.
	 * 
	 * @param email
	 *            the user's email address to set.
	 */
	public void setEmail(String email);

	/**
	 * Whether the user has successfully validated his email address.
	 * 
	 * @return <code>true</code> if user's email address has been successfully
	 *         validated.<br>
	 *         <code>false</code> otherwise.
	 */
	public boolean isValidated();

	/**
	 * Sets whether the user has been validated or not.
	 * 
	 * @param isValidated
	 *            <code>true</code> for user has successfully validated his
	 *            email address.<br>
	 *            <code>false</code> otherwise.
	 */
	public void setValidated(boolean isValidated);

	/**
	 * Gets the user's hashed password.
	 * 
	 * @return the user's hashed password.
	 */
	@Column
	public String getHashedPassword();

	/**
	 * Sets the user's hashed password.
	 * 
	 * @param hashedPassword
	 *            The hashed password to set.
	 */
	public void setHashedPassword(String hashedPassword);
	
	/**
	 * Get's the tasks the user is assigned to.
	 * 
	 * @return the tasks the user is assigned to.
	 */
	@OneToMany
	public List<Task> getTasks();
	
	/**
	 * Sets the list of tasks.
	 * @param tasks the list of tasks to set.
	 */
	public void setTasks(List<Task> tasks);

}
