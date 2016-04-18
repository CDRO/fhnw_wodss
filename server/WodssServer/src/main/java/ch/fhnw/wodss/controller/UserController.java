package ch.fhnw.wodss.controller;

import java.util.Set;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ch.fhnw.wodss.domain.Board;
import ch.fhnw.wodss.domain.LoginData;
import ch.fhnw.wodss.domain.LoginDataFactory;
import ch.fhnw.wodss.domain.User;
import ch.fhnw.wodss.domain.UserFactory;
import ch.fhnw.wodss.notification.RegistrationNotification;
import ch.fhnw.wodss.security.Token;
import ch.fhnw.wodss.security.TokenHandler;
import ch.fhnw.wodss.service.BoardService;
import ch.fhnw.wodss.service.UserService;

@RestController
@CrossOrigin(origins = "http://localhost:9000")
public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	private BoardService boardService;

	/**
	 * Returns all users of that are subscribed for a specific board. This
	 * method can only be called from users that are also in this board.
	 * 
	 * @param tokenId
	 *            The security token to verify that the user is logged in.
	 * @param board
	 *            The board for which all subscribed users should be returned.
	 * @return The subscribed users.
	 */
	@RequestMapping(path = "/users", method = RequestMethod.GET)
	public ResponseEntity<Set<User>> getAllUsers(@RequestHeader(value = "x-session-token") Token token,
			@RequestBody Board board) {
		User user = TokenHandler.getUser(token.getId());
		// reload user form db.
		user = userService.getById(user.getId());
		if (user.getBoards().contains(board)) {
			// reload board from db.
			board = boardService.getById(board.getId());
			return new ResponseEntity<>(board.getUsers(), HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
	}

	/**
	 * Returns the user profile. This can only be done by the same user.
	 * 
	 * @param token
	 *            The security token to verify that the user is logged in.
	 * @param id
	 *            The user id.
	 * @return The user profile.
	 */
	@RequestMapping(path = "/user/{id}", method = RequestMethod.GET)
	public ResponseEntity<User> getUser(@RequestHeader(value = "x-session-token") Token token,
			@PathVariable Integer id) {
		User user = userService.getById(id);
		if (!TokenHandler.validate(token.getId(), user)) {
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}
		return new ResponseEntity<>(user, HttpStatus.OK);
	}

	/**
	 * Validates the user's email address.
	 * 
	 * @param email
	 *            the user's email address.
	 * @param validationCode
	 *            the validation code.
	 * @return true or false
	 */
	@RequestMapping(path = "/user", method = RequestMethod.GET)
	public ResponseEntity<Boolean> validate(@RequestParam("email") String email,
			@RequestParam("validationCode") String validationCode) {
		User user = userService.getByEmail(email);
		if (user != null) {
			if (validationCode.equals(user.getLoginData().getValidationCode())) {
				user.getLoginData().setValidated(true);
				userService.saveUser(user);
				return new ResponseEntity<Boolean>(true, HttpStatus.OK);
			}
		}
		return new ResponseEntity<Boolean>(false, HttpStatus.UNAUTHORIZED);
	}

	/**
	 * Registers a new user.
	 * 
	 * @param json
	 *            the json that contains a name, email and password.
	 * @return The registered user.
	 */
	@RequestMapping(path = "/user", method = RequestMethod.POST)
	public ResponseEntity<User> createUser(@RequestBody JSONObject json) {
		String name = (String) json.get("name");
		String email = (String) json.get("email");
		String password = (String) json.get("password");
		// check if user already exists
		User user = userService.getByEmail(email);
		if (user != null) {
			return new ResponseEntity<>(HttpStatus.CONFLICT);
		}
		User newUser = UserFactory.getInstance().createUser(name, email);
		LoginData loginData = LoginDataFactory.getInstance().createLoginData(password);
		newUser.setLoginData(loginData);
		User savedUser = userService.saveUser(newUser);
		RegistrationNotification notification = new RegistrationNotification(savedUser);
		notification.send();
		return new ResponseEntity<>(savedUser, HttpStatus.OK);
	}

	/**
	 * Deletes a user. This should only be possible to be done by the same user.
	 * 
	 * @param token
	 *            The security token to verify that the user is logged in.
	 * @param id
	 *            The user id to delete.
	 * @return <code>true</code> if successful, <code>false</code> otherwise
	 */
	@RequestMapping(path = "/user/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Boolean> deleteUser(@RequestHeader(value = "x-session-token") Token token,
			@PathVariable Integer id) {
		User user = userService.getById(id);
		if (TokenHandler.validate(token.getId(), user)) {
			userService.deleteUser(id);
			return new ResponseEntity<>(true, HttpStatus.OK);
		}
		return new ResponseEntity<>(false, HttpStatus.UNAUTHORIZED);
	}

	/**
	 * Updates a user profile. This should only be possible to be done by the
	 * same user.
	 * 
	 * @param token
	 *            The security token to verify the user is logged in.
	 * @param user
	 *            The new user data to save.
	 * @param id
	 *            The user id to be modified
	 * @return The modified user.
	 */
	@RequestMapping(path = "/user/{id}", method = RequestMethod.PUT)
	public ResponseEntity<User> updateUser(@RequestHeader(value = "x-session-token") Token token,
			@RequestBody User user, @PathVariable Integer id) {
		User currUser = userService.getById(id);
		if (TokenHandler.validate(token.getId(), currUser)) {
			User updatedUser = userService.saveUser(user);
			return new ResponseEntity<>(updatedUser, HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
	}

}
