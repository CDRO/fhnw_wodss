package ch.fhnw.wodss.security;

import java.security.SecureRandom;

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
	private static final long TTL = 360000;

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
	 * @return a new token.
	 */
	public Token createToken() {
		Token token = new Token();
		token.setId(new SecureRandom().nextLong());
		token.setCreatedAt(System.currentTimeMillis());
		token.setTimeToLive(TTL);
		return token;
	}
}
