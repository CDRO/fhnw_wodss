package ch.fhnw.wodss.domain;

import java.util.List;

import org.apache.commons.lang3.builder.HashCodeBuilder;

class UserImpl implements User {
	
	private Integer id;
	private String name;
	private String email;
	private String hashedPassword;
	private boolean isValidated;
	private List<Task> tasks;
	
	UserImpl(){
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
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String getEmail() {
		return email;
	}

	@Override
	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public boolean isValidated() {
		return isValidated;
	}

	@Override
	public void setValidated(boolean isValidated) {
		this.isValidated = isValidated;
	}

	@Override
	public String getHashedPassword() {
		return hashedPassword;
	}

	@Override
	public void setHashedPassword(String hashedPassword) {
		this.hashedPassword = hashedPassword;
	}

	@Override
	public boolean equals(Object object){
		if(!(object instanceof User)){
			return false;
		}
		User user = (User) object;
		return user.getId() == this.id;
	}
	
	@Override
	public int hashCode(){
		return new HashCodeBuilder(17,31). 
				append(id). 
				append(name). 
				append(email). 
				append(hashedPassword). 
				append(isValidated). 
				toHashCode();
	}

	@Override
	public List<Task> getTasks() {
		return tasks;
	}

	@Override
	public void setTasks(List<Task> tasks) {
		this.tasks = tasks;
	}
}
