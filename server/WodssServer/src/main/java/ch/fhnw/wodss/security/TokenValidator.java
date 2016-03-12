package ch.fhnw.wodss.security;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * Aspect for token validation.
 * 
 * @author tobias
 *
 */
@Aspect
@Component
public class TokenValidator {

	@Around("execution(* ch.fhnw.wodss.controller.*.*(..)) && args(tokenId,..)")
	public Object validate(ProceedingJoinPoint pjp, String tokenId) throws Throwable {
		boolean valid = TokenHandler.validate(tokenId);
		if (!valid) {
			return new ResponseEntity<Object>(HttpStatus.UNAUTHORIZED);
		}
		return pjp.proceed();
	}

}
