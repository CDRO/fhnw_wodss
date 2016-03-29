package ch.fhnw.wodss.controller;

import java.util.List;

import org.json.simple.JSONObject;
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

import ch.fhnw.wodss.domain.LoginData;
import ch.fhnw.wodss.domain.LoginDataFactory;
import ch.fhnw.wodss.domain.User;
import ch.fhnw.wodss.domain.UserFactory;
import ch.fhnw.wodss.notification.RegistrationNotification;
import ch.fhnw.wodss.security.Token;
import ch.fhnw.wodss.service.UserService;

@RestController
@CrossOrigin(origins = "http://localhost:9000")
public class UserController {

	@Autowired
	private UserService userService;

	@RequestMapping(path = "/users", method = RequestMethod.GET)
	public ResponseEntity<List<User>> getAllUsers(@RequestHeader(value = "x-session-token") String tokenId) {
		List<User> users = userService.getAll();
		return new ResponseEntity<>(users, HttpStatus.OK);
	}

	@RequestMapping(path = "/user/{id}", method = RequestMethod.GET)
	public ResponseEntity<User> getUser(@RequestHeader(value = "x-session-token") Token token,
			@PathVariable Integer id) {
		User user = userService.getById(id);
		return new ResponseEntity<>(user, HttpStatus.OK);
	}

	@RequestMapping(path = "/user", method = RequestMethod.POST)
	public ResponseEntity<User> createUser(@RequestBody JSONObject json) {
		String name = (String) json.get("name");
		String email = (String) json.get("email");
		String password = (String) json.get("password");
		User newUser = UserFactory.getInstance().createUser(name, email);
		LoginData loginData = LoginDataFactory.getInstance().createLoginData(password);
		newUser.setLoginData(loginData);
		User savedUser = userService.saveUser(newUser);
		RegistrationNotification notification = new RegistrationNotification(savedUser);
		notification.send();
		return new ResponseEntity<>(savedUser, HttpStatus.OK);
	}

	@RequestMapping(path = "/user/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Boolean> deleteUser(@RequestHeader(value = "x-session-token") Token token,
			@PathVariable Integer id) {
		userService.deleteUser(id);
		return new ResponseEntity<>(true, HttpStatus.OK);
	}

	@RequestMapping(path = "/user/{id}", method = RequestMethod.PUT)
	public ResponseEntity<User> updateUser(@RequestHeader(value = "x-session-token") Token token,
			@RequestBody User user, @PathVariable Integer id) {
		User updatedUser = userService.saveUser(user);
		return new ResponseEntity<>(updatedUser, HttpStatus.OK);
	}

}
