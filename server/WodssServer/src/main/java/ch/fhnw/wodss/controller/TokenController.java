package ch.fhnw.wodss.controller;

import java.util.Arrays;

import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import ch.fhnw.wodss.domain.User;
import ch.fhnw.wodss.security.Password;
import ch.fhnw.wodss.security.Token;
import ch.fhnw.wodss.security.TokenHandler;
import ch.fhnw.wodss.service.UserService;

@RestController
@CrossOrigin
public class TokenController {

	private static final Logger LOG = LoggerFactory.getLogger(TokenController.class);

	@Autowired
	private UserService userService;

	@RequestMapping(path = "/token", method = RequestMethod.POST)
	public ResponseEntity<Token> login(@RequestBody JSONObject object) {
		String email = (String) object.get("email");
		String password = (String) object.get("password");

		User aDbUser = userService.getByEmail(email);
		if (aDbUser != null) {
			if (aDbUser.getLoginData().isValidated()) {
				Password pass = new Password(password.toCharArray(), aDbUser.getLoginData().getSalt());
				if (Arrays.equals(pass.getHash(), aDbUser.getLoginData().getPassword())) {
					Token token = TokenHandler.register(aDbUser);
					LOG.info("User <{}> has logged in.", aDbUser.getEmail());
					return new ResponseEntity<>(token, HttpStatus.OK);
				}
			}
		}
		return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
	}

	@RequestMapping(path = "/token/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Boolean> logout(@RequestHeader(value = "x-session-token") Token token,
			@PathVariable("id") Integer id) {
		User user = TokenHandler.getUser(token.getId());
		TokenHandler.unregister(token.getId());
		LOG.info("User <{}> has logged out", user.getEmail());
		return new ResponseEntity<>(true, HttpStatus.OK);
	}

}
