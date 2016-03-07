package ch.fhnw.wodss.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import ch.fhnw.wodss.security.Token;
import ch.fhnw.wodss.security.TokenHandler;

@RestController
public class TokenConroller {
	
	@RequestMapping(path = "/token", method = RequestMethod.POST)
	public ResponseEntity<Token> createToken() {
		 Token token = TokenHandler.register();
		 return new ResponseEntity<>(token, HttpStatus.OK);
	}
	
	@RequestMapping(path = "/token/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<String> deleteToken(@PathVariable String id) {
		TokenHandler.unregister(id);
		return new ResponseEntity<>("Unregistered token.", HttpStatus.OK);
	}
	
}
