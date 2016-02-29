package ch.fhnw.wodss.domain;

import java.util.Date;

import org.apache.commons.lang3.builder.HashCodeBuilder;

class TaskImpl implements Task {
	
	private Integer id;
	private Board board;
	private TaskState state;
	private User assignee;
	private Date creationDate;
	private Date dueDate;
	private Date doneDate;
	private String description;
	
	TaskImpl(){
		super();
	}
	
	@Override
	public Integer getId(){
		return id;
	}
	
	@Override
	public void setId(Integer id){
		this.id = id;
	}

	@Override
	public Board getBoard() {
		return board;
	}

	@Override
	public void setBoard(Board board) {
		this.board = board;
	}

	@Override
	public TaskState getState() {
		return state;
	}

	@Override
	public void setState(TaskState state) {
		this.state = state;
	}

	@Override
	public User getAssignee() {
		return assignee;
	}

	@Override
	public void setAssignee(User assignee) {
		this.assignee = assignee;
	}

	@Override
	public Date getCreationDate() {
		return creationDate;
	}

	@Override
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate; 
	}

	@Override
	public Date getDueDate() {
		return dueDate;
	}

	@Override
	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}

	@Override
	public Date getDoneDate() {
		return doneDate;
	}

	@Override
	public void setDoneDate(Date doneDate) {
		this.doneDate = doneDate;
	}

	@Override
	public String getDescription() {
		return description;
	}

	@Override
	public void setDescription(String description) {
		this.description = description;
	}
	
	@Override
	public boolean equals(Object object){
		if(!(object instanceof Task)){
			return false;
		}
		Task task = (Task) object;
		return task.getId() == this.id;
	}
	
	@Override
	public int hashCode(){
		return new HashCodeBuilder(17,31). 
				append(id). 
				append(board). 
				append(state). 
				append(assignee). 
				append(creationDate). 
				append(dueDate). 
				append(doneDate). 
				append(description). 
				toHashCode();
	}

}
