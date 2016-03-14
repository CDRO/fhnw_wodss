package ch.fhnw.wodss.controller;

import java.util.List;

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
	public ResponseEntity<User> getUser(@RequestHeader(value = "x-session-token") String tokenId,
			@PathVariable Integer id) {
		User user = userService.getById(id);
		return new ResponseEntity<>(user, HttpStatus.OK);
	}

	@RequestMapping(path = "/user", method = RequestMethod.POST)
	public ResponseEntity<User> createUser(@RequestBody User user) {
		User savedUser = userService.saveUser(user);
		return new ResponseEntity<>(savedUser, HttpStatus.OK);
	}

	@RequestMapping(path = "/user/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Boolean> deleteUser(@RequestHeader(value = "x-session-token") String tokenId,
			@PathVariable Integer id) {
		userService.deleteUser(id);
		return new ResponseEntity<>(true, HttpStatus.OK);
	}

	@RequestMapping(path = "/user/{id}", method = RequestMethod.PUT)
	public ResponseEntity<User> updateUser(@RequestHeader(value = "x-session-token") String tokenId,
			@RequestBody User user, @PathVariable Integer id) {
		User updatedUser = userService.saveUser(user);
		return new ResponseEntity<>(updatedUser, HttpStatus.OK);
	}

}
