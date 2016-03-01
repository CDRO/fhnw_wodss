package ch.fhnw.wodss.domain;

public class UserFactory {

	private static UserFactory instance;

	private UserFactory() {
		super();
	}

	/**
	 * Gets the reference of this object.
	 * 
	 * @return the reference of this object
	 */
	public static synchronized UserFactory getInstance() {
		if (instance == null) {
			instance = new UserFactory();
		}
		return instance;
	}

	/**
	 * Creates a user with the given name and email address.
	 * 
	 * @param name
	 *            the name to set.
	 * @param email
	 *            the email address to set.
	 * @return the created user reference.
	 */
	public User createUser(String name, String email) {
		User user = new User();
		user.setName(name);
		user.setEmail(email);
		return user;
	}

}
