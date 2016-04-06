package ch.fhnw.wodss.security;

import java.util.UUID;

import ch.fhnw.wodss.domain.User;

/**
 * Responsible for creating tokens.
 * 
 * @author tobias
 *
 */
class TokenFactory {

	/**
	 * The time to live.
	 */
	private static final long TTL = 3600000;

	private static TokenFactory instance;

	private TokenFactory() {
		super();
	}

	public static synchronized TokenFactory getInstance() {
		if (instance == null) {
			instance = new TokenFactory();
		}
		return instance;
	}

	/**
	 * Creates a new token.
	 * 
	 * @param user
	 *            the user bound to the token.
	 * @return a new token.
	 */
	public Token createToken(User user) {
		Token token = new Token();
		token.setId(UUID.randomUUID().toString());
		token.setCreatedAt(System.currentTimeMillis());
		token.setTimeToLive(TTL);
		token.setUser(user);
		return token;
	}
}
