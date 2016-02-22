package ch.fhnw.wodss.domain;

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

}
