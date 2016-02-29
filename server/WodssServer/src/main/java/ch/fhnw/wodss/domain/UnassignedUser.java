package ch.fhnw.wodss.domain;

import java.util.List;

/**
 * The user "Unassigned" for unassigned tasks.
 * 
 * @author tobias
 *
 */
public class UnassignedUser implements User {

	private static UnassignedUser instance;

	private UnassignedUser() {
		super();
	}

	/**
	 * Gets the referenced of this user "Unassigned".
	 * 
	 * @return the referenced of this user "Unassigned"
	 */
	public static synchronized User getInstance() {
		if (instance == null) {
			instance = new UnassignedUser();
		}
		return instance;
	}
	
	@Override
	public Integer getId(){
		return 0;
	}
	
	@Override
	public void setId(Integer id){
		return;
	}

	@Override
	public String getName() {
		return "Unassigned";
	}

	@Override
	public void setName(String name) {
		return;
	}

	@Override
	public String getEmail() {
		return "";
	}

	@Override
	public void setEmail(String email) {
		return;
	}

	@Override
	public boolean isValidated() {
		return true;
	}

	@Override
	public void setValidated(boolean validated) {
		return;
	}

	@Override
	public String getHashedPassword() {
		return null;
	}

	@Override
	public void setHashedPassword(String hashedPassword) {
		return;
	}

	@Override
	public List<Task> getTasks() {
		return null;
	}

	@Override
	public void setTasks(List<Task> tasks) {
		return;
	}

}
