package ch.fhnw.wodss.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import ch.fhnw.wodss.security.Token;
import ch.fhnw.wodss.security.TokenHandler;

@RestController
@CrossOrigin(origins = "http://localhost:9000")
public class TokenConroller {
	
	// TODO Get token should not be implemented. The token shoulb be generated and returned after successful login.
	@RequestMapping(path = "/token", method = RequestMethod.GET)
	public ResponseEntity<Token> createToken() {
		 Token token = TokenHandler.register();
		 return new ResponseEntity<>(token, HttpStatus.OK);
	}
	
	@RequestMapping(path = "/token", method = RequestMethod.DELETE)
	public ResponseEntity<Boolean> deleteToken(@RequestHeader(value = "x-session-token") String tokenId) {
		TokenHandler.unregister(tokenId);
		return new ResponseEntity<>(true, HttpStatus.OK);
	}
	
}
