package ch.fhnw.wodss.domain;

public interface User {

	/**
	 * Gets the name of the user.
	 * 
	 * @return the user's name.
	 */
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
	public String getEmail();

	/**
	 * Sets the user's email address.
	 * 
	 * @param email
	 *            the user's email address to set.
	 */
	public void setEmail(String email);
	
	

}
