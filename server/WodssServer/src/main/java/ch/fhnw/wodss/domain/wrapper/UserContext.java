package ch.fhnw.wodss.domain.wrapper;

import ch.fhnw.wodss.domain.User;
import ch.fhnw.wodss.security.Token;

public class UserContext {

	private Token token;
	private User user;
	
	public UserContext(){
		super();
	}

	/**
	 * @return the token
	 */
	public Token getToken() {
		return token;
	}

	/**
	 * @param token the token to set
	 */
	public void setToken(Token token) {
		this.token = token;
	}

	/**
	 * @return the user
	 */
	public User getUser() {
		return user;
	}

	/**
	 * @param user the user to set
	 */
	public void setUser(User user) {
		this.user = user;
	}
	
	
	
}
