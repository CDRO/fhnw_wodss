package ch.fhnw.wodss.security;

import java.util.HashMap;
import java.util.Map;

import ch.fhnw.wodss.domain.User;

/**
 * Handles all user tokens.
 * 
 * @author tobias
 *
 */
public class TokenHandler {
	
	/**
	 * Token Cache.
	 */
	private static Map<String, Token> tokenCache = new HashMap<>();

	private TokenHandler() {
		super();
	}

	/**
	 * Registers a token for a user
	 * 
	 * @param user
	 *            the user for which a token should be registered
	 * @return the registered token.
	 */
	public static Token register(User user) {
		Token token = TokenFactory.getInstance().createToken(user);
		if (tokenCache.containsKey(token.getId())) {
			return register(user);
		}
		tokenCache.put(token.getId(), token);
		return token;
	}

	/**
	 * Unregisters a token by deleting it from the cache.
	 * 
	 * @param tokenId
	 *            the token id to unregister.
	 */
	public static void unregister(String tokenId) {
		tokenCache.remove(tokenId);
	}
	
	/**
	 * Gets the user that has been registered with this token.
	 * @param tokenId the token id for which the user should be returned.
	 * @return the user.
	 */
	public static User getUser(String tokenId){
		return tokenCache.get(tokenId).getUser();
	}

	/**
	 * Validates whether the token is existing and not expired.
	 * 
	 * @param tokenId
	 *            the token id to validate.
	 * @return true if the token is valid.
	 */
	public static boolean validate(String tokenId) {
		return validate(tokenId, null);
	}
	
	/**
	 * Validates whether the token is existing and not expired and if the token
	 * is correctly mapped to the specified user.
	 * 
	 * @param tokenId
	 *            the token id to validate.
	 * @param user
	 *            the user that should be mapped with the tokenid
	 * @return true if the token is okay and mapped with the specified user.
	 */
	public static boolean validate(String tokenId, User user) {
		Token token = tokenCache.get(tokenId);
		if (token == null) {
			return false;
		}
		if (token.isExpired()) {
			unregister(tokenId);
			return false;
		}
		if (user == null) {
			return true;
		}
		// verify token is mapped with specified user.
		return token.getUser().equals(user);
	}

	public static void clear() {
		tokenCache.clear();
	}

}
