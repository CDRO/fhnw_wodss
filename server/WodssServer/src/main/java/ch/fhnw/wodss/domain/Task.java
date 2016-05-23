package ch.fhnw.wodss.domain;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
public class Task {
	// TODO check what should be not nullable.
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	@ManyToOne
	private Board board;
	private TaskState state;
	@ManyToOne
	private User assignee;
	private Date creationDate;
	private Date dueDate;
	private Date doneDate;
	private String description;
	@OneToMany(mappedBy = "task", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JsonManagedReference
	private List<Attachment> attachments;

	Task() {
		super();
		setAttachments(new LinkedList<>());
	}

	/**
	 * Gets the task id.
	 * 
	 * @return the task id.
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * Sets the id of the task.
	 * 
	 * @param id
	 *            the id to set.
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * Gets the board of this task.
	 * 
	 * @return the board of this task.
	 */
	public Board getBoard() {
		return board;
	}

	/**
	 * Sets the board of this task.
	 * 
	 * @param board
	 *            the board to set.
	 */
	public void setBoard(Board board) {
		this.board = board;
	}

	/**
	 * Gets the state of the current task.
	 * 
	 * @return the state of the current task.
	 */
	public TaskState getState() {
		return state;
	}

	/**
	 * Sets the state of the current task.
	 * 
	 * @param the
	 *            state to set.
	 */
	public void setState(TaskState state) {
		this.state = state;
	}

	/**
	 * Gets the assignee of this task.
	 * 
	 * @return the assignee of this task.
	 */
	public User getAssignee() {
		return assignee;
	}

	/**
	 * Sets the assignee of this task.
	 * 
	 * @param user
	 *            the assignee to set.
	 */
	public void setAssignee(User assignee) {
		this.assignee = assignee;
	}

	/**
	 * Gets the creation date of this task.
	 * 
	 * @return the creation date of this task.
	 */
	public Date getCreationDate() {
		return creationDate;
	}

	/**
	 * Sets the creation date of this task.
	 * 
	 * @param creationDate
	 *            the creation date to set.
	 */
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	/**
	 * Gets the due date of this task.
	 * 
	 * @return the due date of this task.
	 */
	public Date getDueDate() {
		return dueDate;
	}

	/**
	 * Sets the due date of this task.
	 * 
	 * @param dueDate
	 *            the due date to set.
	 */
	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}

	/**
	 * Gets the date when this task has been completed.
	 * 
	 * @return the date when this task has been completed.
	 */
	public Date getDoneDate() {
		return doneDate;
	}

	/**
	 * Sets the date when this task has been completed.
	 * 
	 * @param doneDate
	 *            date to set when this task has been completed.
	 */
	public void setDoneDate(Date doneDate) {
		this.doneDate = doneDate;
	}

	/**
	 * Gets the description of this task.
	 * 
	 * @return the description of this task.
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Sets the description of this task.
	 * 
	 * @param description
	 *            the description to set.
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public boolean equals(Object object) {
		if (!(object instanceof Task)) {
			return false;
		}
		Task task = (Task) object;
		return task.getId() == this.id;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 31).append(id).append(board).append(state).append(assignee).append(creationDate)
				.append(dueDate).append(doneDate).append(description).append(attachments).toHashCode();
	}

	/**
	 * @return the attachments
	 */
	public List<Attachment> getAttachments() {
		return attachments;
	}

	/**
	 * @param attachments
	 *            the attachments to set
	 */
	public void setAttachments(List<Attachment> attachments) {
		this.attachments = attachments;
	}

}
