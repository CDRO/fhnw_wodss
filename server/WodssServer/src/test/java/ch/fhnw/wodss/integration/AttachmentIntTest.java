package ch.fhnw.wodss.integration;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import org.json.simple.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import ch.fhnw.wodss.domain.Attachment;
import ch.fhnw.wodss.domain.AttachmentFactory;
import ch.fhnw.wodss.domain.Board;
import ch.fhnw.wodss.domain.BoardFactory;
import ch.fhnw.wodss.domain.Task;
import ch.fhnw.wodss.domain.TaskFactory;
import ch.fhnw.wodss.domain.User;
import ch.fhnw.wodss.security.Token;
import ch.fhnw.wodss.service.AttachmentService;
import ch.fhnw.wodss.service.BoardService;
import ch.fhnw.wodss.service.TaskService;
import ch.fhnw.wodss.service.UserService;

public class AttachmentIntTest extends AbstractIntegrationTest {

	@Autowired
	private BoardService boardService;

	@Autowired
	private UserService userService;

	@Autowired
	private TaskService taskService;

	@Autowired
	private AttachmentService attachmentService;

	private File attachmentFile;

	@Before
	public void setupAttachmentFile() throws URISyntaxException{
		attachmentFile = new File(AttachmentIntTest.class.getClassLoader()
				.getResource("ch/fhnw/wodss/integration/Trello_1.1.pdf").toURI());
	}
	
	/**
	 * Tests getting an attachment. This should be return an attachment only if
	 * an attachment exists and when the user is subscribed to the board that
	 * contains the task with this attachment.
	 * 
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void testGetAttachment() throws Exception {
		JSONObject jsonUser1 = new JSONObject();

		// CREATE / REGISTER
		jsonUser1.put("name", "TestUser20");
		jsonUser1.put("email", "email20@fhnw.ch");
		jsonUser1.put("password", "password");

		User user1 = doPost("http://localhost:8080/user", null, jsonUser1, User.class);
		User userFromDb1= userService.getById(user1.getId());
		Assert.assertEquals("TestUser20", userFromDb1.getName());
		Assert.assertEquals("email20@fhnw.ch", userFromDb1.getEmail());
		Assert.assertNotNull(user1.getId());

		JSONObject jsonUser2 = new JSONObject();

		// CREATE / REGISTER
		jsonUser2.put("name", "TestUser21");
		jsonUser2.put("email", "email21@fhnw.ch");
		jsonUser2.put("password", "password2");

		User user2 = doPost("http://localhost:8080/user", null, jsonUser2, User.class);
		User userFromDb2 = userService.getById(user2.getId());
		Assert.assertEquals("TestUser21", userFromDb2.getName());
		Assert.assertEquals("email21@fhnw.ch", userFromDb2.getEmail());
		Assert.assertNotNull(user2.getId());

		// VALIDATE EMAIL ADDRESS
		Boolean success = doGet("http://localhost:8080/validate?email={0}&validationCode={1}", null, Boolean.class,
				userFromDb1.getEmail(), userFromDb1.getLoginData().getValidationCode());
		Assert.assertTrue(success);
		userFromDb1 = userService.getById(user1.getId());
		Assert.assertTrue(userFromDb1.getLoginData().isValidated());
		success = doGet("http://localhost:8080/validate?email={0}&validationCode={1}", null, Boolean.class,
				userFromDb2.getEmail(), userFromDb2.getLoginData().getValidationCode());
		Assert.assertTrue(success);
		userFromDb2 = userService.getById(user2.getId());
		Assert.assertTrue(userFromDb2.getLoginData().isValidated());

		// REQUEST TOKEN
		Token token1 = doPost("http://localhost:8080/login", null, jsonUser1, Token.class);
		Token token2 = doPost("http://localhost:8080/login", null, jsonUser2, Token.class);

		Board board1 = BoardFactory.getInstance().createBoard("Board1", user1);
		board1 = boardService.saveBoard(board1);
		Assert.assertNotNull(board1.getId());

		Board board2 = BoardFactory.getInstance().createBoard("Board2", user2);
		board2 = boardService.saveBoard(board2);
		Assert.assertNotNull(board2.getId());

		Board board3 = BoardFactory.getInstance().createBoard("Board3", user2);
		board3 = boardService.saveBoard(board3);
		Assert.assertNotNull(board3.getId());
		
		Task task1 = TaskFactory.getInstance().createTask(board1, "Task1");
		
		Attachment attachment = AttachmentFactory.getInstance().createAttachment(task1, "pdf");
		success = attachmentService.saveAttachmentToFileSystem(attachment, attachmentFile);
		Assert.assertTrue(success);
		
		task1.getAttachments().add(attachment);
		task1 = taskService.saveTask(task1);
		Assert.assertNotNull(task1.getId());
		
		try{
			doGet("http://localhost:8080/attachment/{0}", token1, File.class, attachment.getId());
		} catch (IOException e){
			e.printStackTrace();
			Assert.fail();
		} catch (Exception e){
			Assert.fail();
		}
		
		try{
			doGet("http://localhost:8080/attachment/{0}", token2, File.class, attachment.getId());
			Assert.fail();
		} catch (IOException e){
		} catch (Exception e){
			Assert.fail();
		}
		
	}
	
	/**
	 * Tests deleting an attachment. This should be return an attachment only if
	 * an attachment exists and when the user is subscribed to the board that
	 * contains the task with this attachment.
	 * 
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void testDeleteAttachment() throws Exception {
		JSONObject jsonUser1 = new JSONObject();

		// CREATE / REGISTER
		jsonUser1.put("name", "TestUser22");
		jsonUser1.put("email", "email22@fhnw.ch");
		jsonUser1.put("password", "password");

		User user1 = doPost("http://localhost:8080/user", null, jsonUser1, User.class);
		User userFromDb1= userService.getById(user1.getId());
		Assert.assertEquals("TestUser22", userFromDb1.getName());
		Assert.assertEquals("email22@fhnw.ch", userFromDb1.getEmail());
		Assert.assertNotNull(user1.getId());

		JSONObject jsonUser2 = new JSONObject();

		// CREATE / REGISTER
		jsonUser2.put("name", "TestUser23");
		jsonUser2.put("email", "email23@fhnw.ch");
		jsonUser2.put("password", "password2");

		User user2 = doPost("http://localhost:8080/user", null, jsonUser2, User.class);
		User userFromDb2 = userService.getById(user2.getId());
		Assert.assertEquals("TestUser23", userFromDb2.getName());
		Assert.assertEquals("email23@fhnw.ch", userFromDb2.getEmail());
		Assert.assertNotNull(user2.getId());

		// VALIDATE EMAIL ADDRESS
		Boolean success = doGet("http://localhost:8080/validate?email={0}&validationCode={1}", null, Boolean.class,
				userFromDb1.getEmail(), userFromDb1.getLoginData().getValidationCode());
		Assert.assertTrue(success);
		userFromDb1 = userService.getById(user1.getId());
		Assert.assertTrue(userFromDb1.getLoginData().isValidated());
		success = doGet("http://localhost:8080/validate?email={0}&validationCode={1}", null, Boolean.class,
				userFromDb2.getEmail(), userFromDb2.getLoginData().getValidationCode());
		Assert.assertTrue(success);
		userFromDb2 = userService.getById(user2.getId());
		Assert.assertTrue(userFromDb2.getLoginData().isValidated());

		// REQUEST TOKEN
		Token token1 = doPost("http://localhost:8080/login", null, jsonUser1, Token.class);
		Token token2 = doPost("http://localhost:8080/login", null, jsonUser2, Token.class);

		Board board1 = BoardFactory.getInstance().createBoard("Board1", user1);
		board1 = boardService.saveBoard(board1);
		Assert.assertNotNull(board1.getId());

		Board board2 = BoardFactory.getInstance().createBoard("Board2", user2);
		board2 = boardService.saveBoard(board2);
		Assert.assertNotNull(board2.getId());

		Board board3 = BoardFactory.getInstance().createBoard("Board3", user2);
		board3 = boardService.saveBoard(board3);
		Assert.assertNotNull(board3.getId());
		
		Task task1 = TaskFactory.getInstance().createTask(board1, "Task1");
		
		Attachment attachment = AttachmentFactory.getInstance().createAttachment(task1, "pdf");
		success = attachmentService.saveAttachmentToFileSystem(attachment, attachmentFile);
		Assert.assertTrue(success);
		
		task1.getAttachments().add(attachment);
		task1 = taskService.saveTask(task1);
		Assert.assertNotNull(task1.getId());
		
		try{
			success = doDelete("http://localhost:8080/attachment/{0}", token2, Boolean.class, attachment.getId());
			Assert.fail();
		} catch (IOException e){
		} catch (Exception e){
			Assert.fail();
		}

		try{
			success = doDelete("http://localhost:8080/attachment/{0}", token1, Boolean.class, attachment.getId());
		} catch (IOException e){
			e.printStackTrace();
			Assert.fail();
		} catch (Exception e){
			Assert.fail();
		}
		
		Assert.assertFalse(attachment.getFile().exists());
	}

}
