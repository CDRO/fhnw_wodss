package ch.fhnw.wodss.security;

import java.util.UUID;

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
	 * @return a new token.
	 */
	public Token createToken() {
		Token token = new Token();
		token.setId(UUID.randomUUID().toString());
		token.setCreatedAt(System.currentTimeMillis());
		token.setTimeToLive(TTL);
		return token;
	}
}
