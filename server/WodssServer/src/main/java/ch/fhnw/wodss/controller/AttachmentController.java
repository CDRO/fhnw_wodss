package ch.fhnw.wodss.controller;

import java.io.File;

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
import ch.fhnw.wodss.security.Token;
import ch.fhnw.wodss.service.AttachmentService;

@RestController
@CrossOrigin(origins = "http://localhost:9000")
public class AttachmentController {
	
	@Autowired
	private AttachmentService attachmentService;
	
	@RequestMapping(path = "/attachment/{id}", method = RequestMethod.GET)
	public ResponseEntity<File> getAttachment(@RequestHeader(value = "x-session-token") Token token, @PathVariable String id) {
		Attachment attachment = attachmentService.getAttachment(id);
		if(attachment == null){
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(attachment.getFile(), HttpStatus.OK);
	}
	
	@RequestMapping(path = "/attachment/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Boolean> deleteAttachment(@RequestHeader(value = "x-session-token") Token token, @PathVariable String id) {
		Attachment attachment = attachmentService.getAttachment(id);
		if(attachment == null){
			return new ResponseEntity<>(false, HttpStatus.NOT_FOUND);
		}
		attachmentService.deleteAttachment(attachment);
		return new ResponseEntity<>(true, HttpStatus.OK);
	}

}
