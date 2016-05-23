package ch.fhnw.wodss.scheduler;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import ch.fhnw.wodss.security.Token;
import ch.fhnw.wodss.security.TokenHandler;

@Component
public class TokenCacheCleaner {

	public final static Logger LOG = LoggerFactory.getLogger(TokenCacheCleaner.class);
	
	/**
	 * Removes expired tokens from cache every 20 minutes.
	 */
	@Scheduled(fixedDelay = 1200000L)
	public void cleanTokenCache(){
		LOG.info("Removing expired tokens from cache...");
		Map<String, Token> cache = TokenHandler.getCache();
		for(Token token : cache.values()){
			if(token.isExpired()){
				cache.remove(token);
			}
		}
	}
	
}
