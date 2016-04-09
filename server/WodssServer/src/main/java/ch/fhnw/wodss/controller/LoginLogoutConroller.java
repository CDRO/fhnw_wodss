package ch.fhnw.wodss.controller;

import java.util.Arrays;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ch.fhnw.wodss.domain.User;
import ch.fhnw.wodss.security.Password;
import ch.fhnw.wodss.security.Token;
import ch.fhnw.wodss.security.TokenHandler;
import ch.fhnw.wodss.service.UserService;

@RestController
@CrossOrigin(origins = "http://localhost:9000")
public class LoginLogoutConroller {

	@Autowired
	private UserService userService;

	@RequestMapping(path = "/login", method = RequestMethod.POST)
	public ResponseEntity<Token> login(@RequestBody JSONObject object) {
		String email = (String) object.get("email");
		String password = (String) object.get("password");

		User aDbUser = userService.getByEmail(email);
		if (aDbUser != null) {
			if (aDbUser.getLoginData().isValidated()) {
				Password pass = new Password(password.toCharArray(), aDbUser.getLoginData().getSalt());
				if (Arrays.equals(pass.getHash(), aDbUser.getLoginData().getPassword())) {
					Token token = TokenHandler.register(aDbUser);
					return new ResponseEntity<>(token, HttpStatus.OK);
				}
			}
		}
		return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
	}

	@RequestMapping(path = "/logout", method = RequestMethod.DELETE)
	public ResponseEntity<Boolean> logout(@RequestHeader(value = "x-session-token") Token token) {
		TokenHandler.unregister(token.getId());
		return new ResponseEntity<>(true, HttpStatus.OK);
	}

	@RequestMapping(path = "/validate", method = RequestMethod.GET)
	public ResponseEntity<Boolean> validate(@RequestParam("email") String email,
			@RequestParam("validationCode") String validationCode) {
		User user = userService.getByEmail(email);
		if(user != null){			
			if (validationCode.equals(user.getLoginData().getValidationCode())) {
				user.getLoginData().setValidated(true);
				userService.saveUser(user);
				return new ResponseEntity<Boolean>(true, HttpStatus.OK);
			}
		}
		return new ResponseEntity<Boolean>(false, HttpStatus.UNAUTHORIZED);
	}

}
