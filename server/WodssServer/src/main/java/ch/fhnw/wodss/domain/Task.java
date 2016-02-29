package ch.fhnw.wodss.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 * The task to do.
 * 
 * @author tobias
 *
 */
@Entity
public interface Task {

	/**
	 * Gets the task id.
	 * 
	 * @return the task id.
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer getId();

	/**
	 * Sets the id of the task.
	 * 
	 * @param id
	 *            the id to set.
	 */
	public void setId(Integer id);

	/**
	 * Gets the board of this task.
	 * 
	 * @return the board of this task.
	 */
	@ManyToOne
	public Board getBoard();

	/**
	 * Sets the board of this task.
	 * 
	 * @param board
	 *            the board to set.
	 */
	public void setBoard(Board board);

	/**
	 * Gets the state of the current task.
	 * 
	 * @return the state of the current task.
	 */
	@Column
	public TaskState getState();

	/**
	 * Sets the state of the current task.
	 * 
	 * @param the
	 *            state to set.
	 */
	public void setState(TaskState state);

	/**
	 * Gets the assignee of this task.
	 * 
	 * @return the assignee of this task.
	 */
	@ManyToOne
	public User getAssignee();

	/**
	 * Sets the assignee of this task.
	 * 
	 * @param user
	 *            the assignee to set.
	 */
	public void setAssignee(User assignee);

	/**
	 * Gets the creation date of this task.
	 * 
	 * @return the creation date of this task.
	 */
	@Column
	public Date getCreationDate();

	/**
	 * Sets the creation date of this task.
	 * 
	 * @param creationDate
	 *            the creation date to set.
	 */
	public void setCreationDate(Date creationDate);

	/**
	 * Gets the due date of this task.
	 * 
	 * @return the due date of this task.
	 */
	@Column
	public Date getDueDate();

	/**
	 * Sets the due date of this task.
	 * 
	 * @param dueDate
	 *            the due date to set.
	 */
	public void setDueDate(Date dueDate);

	/**
	 * Gets the date when this task has been completed.
	 * 
	 * @return the date when this task has been completed.
	 */
	@Column
	public Date getDoneDate();

	/**
	 * Sets the date when this task has been completed.
	 * 
	 * @param doneDate
	 *            date to set when this task has been completed.
	 */
	public void setDoneDate(Date doneDate);

	/**
	 * Gets the description of this task.
	 * 
	 * @return the description of this task.
	 */
	@Column
	public String getDescription();

	/**
	 * Sets the description of this task.
	 * 
	 * @param description
	 *            the description to set.
	 */
	public void setDescription(String description);

}
