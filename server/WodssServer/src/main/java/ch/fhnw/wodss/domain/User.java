package ch.fhnw.wodss.domain;

import java.util.LinkedList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String name;
	@Column(unique = true)
	private String email;
	@JsonIgnore
	@OneToOne(cascade = CascadeType.ALL)
	private LoginData loginData;
	@OneToMany(fetch = FetchType.EAGER, mappedBy = "assignee")
	private List<Task> tasks;
	@ManyToMany(fetch = FetchType.EAGER)
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

	/**
	 * @return the loginData
	 */
	public LoginData getLoginData() {
		return loginData;
	}

	/**
	 * @param loginData the loginData to set
	 */
	public void setLoginData(LoginData loginData) {
		this.loginData = loginData;
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
		return new HashCodeBuilder(17, 31).append(id).append(name).append(email).append(loginData).toHashCode();
	}
}
