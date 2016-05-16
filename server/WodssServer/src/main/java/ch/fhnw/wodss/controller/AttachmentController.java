package ch.fhnw.wodss.controller;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import ch.fhnw.wodss.domain.Attachment;
import ch.fhnw.wodss.domain.User;
import ch.fhnw.wodss.security.Token;
import ch.fhnw.wodss.security.TokenHandler;
import ch.fhnw.wodss.service.AttachmentService;
import ch.fhnw.wodss.service.UserService;

@RestController
@CrossOrigin(origins = "*")
public class AttachmentController {

	private static final Logger LOG = LoggerFactory.getLogger(AttachmentController.class);

	@Autowired
	private AttachmentService attachmentService;

	@Autowired
	private UserService userService;

	/**
	 * Gets the attachment of a task, only if the user is subscribed to the
	 * board which contains this task.
	 * 
	 * @param token
	 *            The security token to verify that the user is logged in.
	 * @param id
	 *            The attachment id (UUID)
	 * @return The attachament if any.
	 */
	@RequestMapping(path = "/attachment/{id}", method = RequestMethod.GET)
	public ResponseEntity<File> getAttachment(@RequestHeader(value = "x-session-token") Token token,
			@PathVariable String id) {
		LOG.debug("Getting attachment with id <{}>", id);
		User user = TokenHandler.getUser(token.getId());
		// Reload user from database
		user = userService.getById(user.getId());
		Attachment attachment = attachmentService.getAttachment(id);
		if (attachment == null) {
			LOG.debug("Attachment not found, returning <{}>", HttpStatus.NOT_FOUND);
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		if (user.getBoards().contains(attachment.getTask().getBoard())) {
			LOG.debug("Found attachment with id <{}> and user <{}> is permitted to open it", id,
					user.getEmail());
			return new ResponseEntity<>(attachment.getFile(), HttpStatus.OK);
		}
		LOG.debug(
				"The user <{}> wanted to open the attachment with id <{}> but is not authorized.", user.getEmail(),
				id);
		return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
	}

	/**
	 * Deletes the attachment of a task, only if the user is subscribed to the
	 * board which contains this task.
	 * 
	 * @param token
	 *            The security token to verify the user is logged in.
	 * @param id
	 *            The attachment id to delete.
	 * @return <code>true</code> if successful, <code>false</code> otherwise.
	 */
	@RequestMapping(path = "/attachment/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Boolean> deleteAttachment(@RequestHeader(value = "x-session-token") Token token,
			@PathVariable String id) {
		User user = TokenHandler.getUser(token.getId());
		// Reload user from database
		user = userService.getById(user.getId());
		Attachment attachment = attachmentService.getAttachment(id);
		if (attachment == null) {
			LOG.debug("Attachment with id <{}> not found", id);
			return new ResponseEntity<>(false, HttpStatus.NOT_FOUND);
		}
		if (user.getBoards().contains(attachment.getTask().getBoard())) {
			attachmentService.deleteAttachment(attachment);
			LOG.info("User <{}> deleted attachment with id <{}>", user.getEmail(), id);
			return new ResponseEntity<>(true, HttpStatus.OK);
		}
		LOG.debug("User <{}> is not authorized to delete attachment with id <{}>",
				user.getEmail(), id);
		return new ResponseEntity<>(false, HttpStatus.UNAUTHORIZED);
	}

}
