package ch.fhnw.wodss.security;
import java.util.HashMap;
import java.util.Map;

/**
 * Handles all user tokens.
 * @author tobias
 *
 */
public class TokenHandler {
	
	/**
	 * Cache.
	 */
	private static Map<Long, Token> cache = new HashMap<>();
	
	
	private TokenHandler(){
		super();
	}
		
	public static Token register(){
		Token token = TokenFactory.getInstance().createToken();
		cache.put(token.getId(), token);
		return token;
	}
	
	public static void unregister(String tokenId){
		cache.remove(tokenId);
	}
	
	public static boolean validate(String tokenId){
		Token token = cache.get(tokenId);
		if(token == null){
			return false;
		}
		if(token.isExpired()){
			unregister(tokenId);
			return false;
		}
		return true;
	}
	
	public static void clear(){
		cache.clear();
	}
	
}
