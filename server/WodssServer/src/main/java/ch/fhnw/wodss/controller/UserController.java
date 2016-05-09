package ch.fhnw.wodss.controller;

import java.util.Set;
import java.util.UUID;

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

import ch.fhnw.wodss.domain.Board;
import ch.fhnw.wodss.domain.LoginData;
import ch.fhnw.wodss.domain.LoginDataFactory;
import ch.fhnw.wodss.domain.User;
import ch.fhnw.wodss.domain.UserFactory;
import ch.fhnw.wodss.notification.RegistrationNotification;
import ch.fhnw.wodss.notification.ResetPasswordNotification;
import ch.fhnw.wodss.security.Token;
import ch.fhnw.wodss.security.TokenHandler;
import ch.fhnw.wodss.service.BoardService;
import ch.fhnw.wodss.service.UserService;

@RestController
@CrossOrigin(origins = "http://localhost:9000")
public class UserController {

	private static final Logger LOG = LoggerFactory.getLogger(UserController.class);

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
			LOG.debug("User <{}> requested all users for board <{}>", user.getEmail(), board.getId());
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
		LOG.debug("User <{}> requested the user profile information", user.getEmail());
		return new ResponseEntity<>(user, HttpStatus.OK);
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
			if(user.getLoginData() != null){
				return new ResponseEntity<>(HttpStatus.CONFLICT);
			}
			user.setName(name);
		} else {
			user = UserFactory.getInstance().createUser(name, email);
		}
		LoginData loginData = LoginDataFactory.getInstance().createLoginData(password);
		user.setLoginData(loginData);
		User savedUser = userService.saveUser(user);
		RegistrationNotification notification = new RegistrationNotification(savedUser);
		notification.send();
		LOG.info("User <{}> has been registered", savedUser.getEmail());
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
			if(user.getBoards().size() > 0){
				return new ResponseEntity<>(false, HttpStatus.CONFLICT);
			}
			userService.deleteUser(id);
			LOG.info("User <{}> has been deleted", user.getEmail());
			TokenHandler.unregister(token.getId());
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
			LOG.info("User <{}> has updated his user profile", updatedUser.getEmail());
			return new ResponseEntity<>(updatedUser, HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
	}

	/**
	 * Validates a users email address or resets a users password.
	 */
	@RequestMapping(path = "/user/{id}/logindata", method = RequestMethod.PUT)
	public ResponseEntity<Boolean> validateOrReset(@PathVariable("id") Integer id,
			@RequestBody(required = false) JSONObject json) {
		String validationCode = (String) json.get("validationCode");
		Boolean doReset = (Boolean) json.get("doReset");
		String resetCode = (String) json.get("resetCode");
		String password = (String) json.get("password");
		if (validationCode != null && !"".equals(validationCode)) {
			return validate(id, validationCode);
		}
		if (doReset != null && doReset) {
			return generateResetCode(id);
		}
		if (resetCode != null && !"".equals(resetCode) && password != null && !"".equals(password)) {
			return reset(id, resetCode, password);
		}
		return new ResponseEntity<Boolean>(false, HttpStatus.BAD_REQUEST);
	}

	/**
	 * Validates the user's email address.
	 * 
	 * @param id
	 *            the user's id.
	 * @param validationCode
	 *            the validation code.
	 * @return true or false
	 */
	private ResponseEntity<Boolean> validate(Integer id, String validationCode) {
		User aCurrUser = userService.getById(id);
		if (aCurrUser != null) {
			if (validationCode.equals(aCurrUser.getLoginData().getValidationCode())) {
				aCurrUser.getLoginData().setValidated(true);
				userService.saveUser(aCurrUser);
				LOG.info("User <{}> has validated his email address", aCurrUser.getEmail());
				return new ResponseEntity<>(true, HttpStatus.OK);
			}
		}
		return new ResponseEntity<>(false, HttpStatus.UNAUTHORIZED);
	}

	/**
	 * Resets the user's password.
	 * 
	 * @param id
	 *            the user's id.
	 * @param resetCode
	 *            the reset code.
	 * @param password
	 *            the password to set.
	 * @return true or false
	 */
	private ResponseEntity<Boolean> reset(Integer id, String resetCode, String password) {
		User aCurrUser = userService.getById(id);
		if (aCurrUser != null) {
			if (resetCode.equals(aCurrUser.getLoginData().getResetCode())) {
				LoginData newLoginData = LoginDataFactory.getInstance().createLoginData(password);
				aCurrUser.getLoginData().setSalt(newLoginData.getSalt());
				aCurrUser.getLoginData().setPassword(newLoginData.getPassword());
				userService.saveUser(aCurrUser);
				LOG.info("User <{}> has resetted his password.", aCurrUser.getEmail());
				return new ResponseEntity<>(true, HttpStatus.OK);
			}
		}
		return new ResponseEntity<>(false, HttpStatus.UNAUTHORIZED);
	}

	/**
	 * Generates the user's password reset code.
	 * 
	 * @param id
	 *            the user's id.
	 * @return true or false
	 */
	private ResponseEntity<Boolean> generateResetCode(Integer id) {
		User aCurrUser = userService.getById(id);
		if (aCurrUser != null) {
			String resetCode = UUID.randomUUID().toString();
			aCurrUser.getLoginData().setResetCode(resetCode);
			userService.saveUser(aCurrUser);
			ResetPasswordNotification notification = new ResetPasswordNotification(aCurrUser);
			notification.send();
			LOG.info("User <{}> has requested a password reset code.", aCurrUser.getEmail());
			return new ResponseEntity<>(true, HttpStatus.OK);
		}
		return new ResponseEntity<>(false, HttpStatus.UNAUTHORIZED);
	}

}
