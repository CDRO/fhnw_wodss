package ch.fhnw.wodss.integration;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import ch.fhnw.wodss.domain.Board;
import ch.fhnw.wodss.domain.BoardFactory;
import ch.fhnw.wodss.domain.Task;
import ch.fhnw.wodss.domain.TaskFactory;
import ch.fhnw.wodss.domain.User;
import ch.fhnw.wodss.domain.UserFactory;
import ch.fhnw.wodss.security.Token;
import ch.fhnw.wodss.security.TokenHandler;
import ch.fhnw.wodss.service.BoardService;
import ch.fhnw.wodss.service.TaskService;
import ch.fhnw.wodss.service.UserService;

public class TaskIntTest extends AbstractIntegrationTest {

	@Autowired
	private TaskService taskService;

	@Autowired
	private BoardService boardService;

	@Autowired
	private UserService userService;

	@SuppressWarnings("unchecked")
	@Test
	public void testCreatTaskWithBoard() throws Exception {

		User user = UserFactory.getInstance().createUser("Hans", "hans.muster@fhnw.ch");

		Board board = BoardFactory.getInstance().createBoard("BoardTitle", user);
		board = boardService.saveBoard(board);

		JSONObject json = new JSONObject();
		json.put("email", "hans.muster@fhnw.ch");
		json.put("password", "password");

		// REQUEST TOKEN
		Token token = doPost("http://localhost:8080/login", null, json, Token.class);

		// CREATE
		json.clear();
		json.put("description", "TestTask");

		// Board
		JSONObject jsonBoard = new JSONObject();
		jsonBoard.put("id", board.getId());
		jsonBoard.put("title", board.getTitle());
		json.put("board", jsonBoard);

		System.out.println(json.toJSONString());

		Task task = doMulitPartPostTask("http://localhost:8080/task", token, json, null);
		Assert.assertNotNull(task.getId());
		Assert.assertEquals(board.getTitle(), task.getBoard().getTitle());
		Task taskFromDb = taskService.getById(task.getId());
		Assert.assertEquals("TestTask", taskFromDb.getDescription());
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testTaskAuthorizedCRUD() throws Exception {

		JSONObject json = new JSONObject();
		json.put("email", "hans.muster@fhnw.ch");
		json.put("password", "password");

		// REQUEST TOKEN
		Token token = doPost("http://localhost:8080/login", null, json, Token.class);

		// CREATE
		json.clear();
		json.put("description", "TestTask");

		Task task = doMulitPartPostTask("http://localhost:8080/task", token, json, null);
		Assert.assertNotNull(task.getId());
		Task taskFromDb = taskService.getById(task.getId());
		Assert.assertEquals("TestTask", taskFromDb.getDescription());

		// READ
		task = doGet("http://localhost:8080/task/{0}", token, Task.class, taskFromDb.getId());
		Assert.assertNotNull(task);
		Assert.assertEquals("TestTask", task.getDescription());

		// UPDATE
		json = new JSONObject();
		json.put("id", task.getId());
		json.put("description", "TestTask2");
		task = doMulitPartPutTask("http://localhost:8080/task/{0}", token, json, null, task.getId());
		taskFromDb = taskService.getById(task.getId());
		Assert.assertEquals("TestTask2", taskFromDb.getDescription());
		Assert.assertEquals("TestTask2", task.getDescription());

		// DELETE
		doDelete("http://localhost:8080/task/{0}", token, Boolean.class, task.getId());
		taskFromDb = taskService.getById(task.getId());
		Assert.assertNull(taskFromDb);

	}

	@SuppressWarnings("unchecked")
	@Test
	public void testTaskUnauthorizedCRUD() {

		User user = UserFactory.getInstance().createUser("test", "test");

		// INVALID TOKEN
		Token token = TokenHandler.register(user);
		token.setId("asdf-asdf-asdf-asdf");

		JSONObject json = new JSONObject();
		json.put("description", "TestTask");

		try {
			// CREATE
			doMulitPartPostTask("http://localhost:8080/task", token, json, null);
			Assert.fail();
		} catch (IOException e) {
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}

		try {
			// READ
			doGet("http://localhost:8080/task/{0}", token, Task.class, 1);
			Assert.fail();
		} catch (IOException e) {
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}

		try {
			// UPDATE
			doMulitPartPutTask("http://localhost:8080/task/{0}", token, json, null, 1);
			Assert.fail();
		} catch (IOException e) {
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}

		try {
			// DELETE
			doDelete("http://localhost:8080/task/{0}", token, Boolean.class, 1);
			Assert.fail();
		} catch (IOException e) {
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testCreateDeleteTaskWithAttachment() throws Exception {

		// load file
		URL resource1 = getClass().getClassLoader().getResource("ch/fhnw/wodss/integration/Trello_1.1.pdf");
		URL resource2 = getClass().getClassLoader().getResource("ch/fhnw/wodss/integration/Trello_1.1.pdf");
		File file1 = new File(resource1.toURI());
		File file2 = new File(resource2.toURI());
		List<File> files = new LinkedList<File>();
		files.add(file1);
		files.add(file2);

		JSONObject json = new JSONObject();
		json.put("email", "hans.muster@fhnw.ch");
		json.put("password", "password");

		// REQUEST TOKEN
		Token token = doPost("http://localhost:8080/login", null, json, Token.class);

		// CREATE
		json.clear();
		json.put("description", "TestTask");

		Task task = doMulitPartPostTask("http://localhost:8080/task", token, json, files);
		Assert.assertNotNull(task.getId().intValue());
		Task taskFromDb = taskService.getById(task.getId());
		Assert.assertEquals("TestTask", taskFromDb.getDescription());

		// TODO test delete task with attachment!!
	}

	/**
	 * Tests getting the all tasks of all boards the user is owner or has been
	 * invited.
	 * 
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void testGetTasks() throws Exception {
		JSONObject json = new JSONObject();

		// CREATE / REGISTER
		json.put("name", "TestUser10");
		json.put("email", "email10@fhnw.ch");
		json.put("password", "password");

		User user = doPost("http://localhost:8080/user", null, json, User.class);
		User userFromDb = userService.getById(user.getId());
		Assert.assertEquals("TestUser10", userFromDb.getName());
		Assert.assertEquals("email10@fhnw.ch", userFromDb.getEmail());
		Assert.assertNotNull(user.getId());

		json = new JSONObject();

		// CREATE / REGISTER
		json.put("name", "TestUser11");
		json.put("email", "email11@fhnw.ch");
		json.put("password", "password2");

		User user2 = doPost("http://localhost:8080/user", null, json, User.class);
		User userFromDb2 = userService.getById(user2.getId());
		Assert.assertEquals("TestUser11", userFromDb2.getName());
		Assert.assertEquals("email11@fhnw.ch", userFromDb2.getEmail());
		Assert.assertNotNull(user.getId());

		// VALIDATE EMAIL ADDRESS
		Boolean success = doGet("http://localhost:8080/validate?email={0}&validationCode={1}", null, Boolean.class,
				userFromDb2.getEmail(), userFromDb2.getLoginData().getValidationCode());
		Assert.assertTrue(success);
		userFromDb2 = userService.getById(user2.getId());
		Assert.assertTrue(userFromDb2.getLoginData().isValidated());

		// REQUEST TOKEN
		Token token = doPost("http://localhost:8080/login", null, json, Token.class);

		Board board1 = BoardFactory.getInstance().createBoard("Board1", user);
		board1 = boardService.saveBoard(board1);
		Assert.assertNotNull(board1.getId());

		Board board2 = BoardFactory.getInstance().createBoard("Board2", user2);
		board2 = boardService.saveBoard(board2);
		Assert.assertNotNull(board2.getId());

		Board board3 = BoardFactory.getInstance().createBoard("Board3", user2);
		board3 = boardService.saveBoard(board3);
		Assert.assertNotNull(board3.getId());

		Task task1 = TaskFactory.getInstance().createTask(board1, "Task1");
		task1 = taskService.saveTask(task1);
		Assert.assertNotNull(task1.getId());

		Task task2 = TaskFactory.getInstance().createTask(board2, "Task2");
		task2 = taskService.saveTask(task2);
		Assert.assertNotNull(task2.getId());

		Task task3 = TaskFactory.getInstance().createTask(board3, "Task3");
		task3 = taskService.saveTask(task3);
		Assert.assertNotNull(task3.getId());

		Task[] alltasks = doGet("http://localhost:8080/tasks", token, Task[].class);
		List<Task> alltasksList = Arrays.asList(alltasks);
		Assert.assertEquals(2, alltasksList.size());
		Assert.assertTrue(alltasksList.contains(task2));
		Assert.assertTrue(alltasksList.contains(task3));

	}

	/**
	 * Tests getting a single task by id. The taks should only be returned, if
	 * the task belongs to the board the user is subscribed to.
	 * 
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void testGetTask() throws Exception {
		JSONObject json = new JSONObject();

		// CREATE / REGISTER
		json.put("name", "TestUser12");
		json.put("email", "email12@fhnw.ch");
		json.put("password", "password");

		User user = doPost("http://localhost:8080/user", null, json, User.class);
		User userFromDb = userService.getById(user.getId());
		Assert.assertEquals("TestUser12", userFromDb.getName());
		Assert.assertEquals("email12@fhnw.ch", userFromDb.getEmail());
		Assert.assertNotNull(user.getId());

		json = new JSONObject();

		// CREATE / REGISTER
		json.put("name", "TestUser13");
		json.put("email", "email13@fhnw.ch");
		json.put("password", "password2");

		User user2 = doPost("http://localhost:8080/user", null, json, User.class);
		User userFromDb2 = userService.getById(user2.getId());
		Assert.assertEquals("TestUser13", userFromDb2.getName());
		Assert.assertEquals("email13@fhnw.ch", userFromDb2.getEmail());
		Assert.assertNotNull(user.getId());

		// VALIDATE EMAIL ADDRESS
		Boolean success = doGet("http://localhost:8080/validate?email={0}&validationCode={1}", null, Boolean.class,
				userFromDb2.getEmail(), userFromDb2.getLoginData().getValidationCode());
		Assert.assertTrue(success);
		userFromDb2 = userService.getById(user2.getId());
		Assert.assertTrue(userFromDb2.getLoginData().isValidated());

		// REQUEST TOKEN
		Token token = doPost("http://localhost:8080/login", null, json, Token.class);

		Board board1 = BoardFactory.getInstance().createBoard("Board1", user);
		board1 = boardService.saveBoard(board1);
		Assert.assertNotNull(board1.getId());

		Board board2 = BoardFactory.getInstance().createBoard("Board2", user2);
		board2 = boardService.saveBoard(board2);
		Assert.assertNotNull(board2.getId());

		Board board3 = BoardFactory.getInstance().createBoard("Board3", user2);
		board3 = boardService.saveBoard(board3);
		Assert.assertNotNull(board3.getId());

		Task task1 = TaskFactory.getInstance().createTask(board1, "Task1");
		task1 = taskService.saveTask(task1);
		Assert.assertNotNull(task1.getId());

		Task task2 = TaskFactory.getInstance().createTask(board2, "Task2");
		task2 = taskService.saveTask(task2);
		Assert.assertNotNull(task2.getId());

		Task task3 = TaskFactory.getInstance().createTask(board3, "Task3");
		task3 = taskService.saveTask(task3);
		Assert.assertNotNull(task3.getId());

		try{
			task2 = doGet("http://localhost:8080/task/{0}", token, Task.class, task2.getId());
		} catch (IOException e){
			Assert.fail();
		} catch (Exception e){
			Assert.fail();
		}
		
		try{
			task1 =  doGet("http://localhost:8080/task/{0}", token, Task.class, task1.getId());
			Assert.fail();
		} catch (IOException e){
		} catch (Exception e){
			Assert.fail();
		}

	}
	
	/**
	 * Tests creating a single task. The task should only be created, if
	 * the task belongs to the board the user is subscribed to.
	 * 
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void testCreateTask() throws Exception {
		JSONObject json = new JSONObject();

		// CREATE / REGISTER
		json.put("name", "TestUser14");
		json.put("email", "email14@fhnw.ch");
		json.put("password", "password");

		User user = doPost("http://localhost:8080/user", null, json, User.class);
		User userFromDb = userService.getById(user.getId());
		Assert.assertEquals("TestUser14", userFromDb.getName());
		Assert.assertEquals("email14@fhnw.ch", userFromDb.getEmail());
		Assert.assertNotNull(user.getId());

		json = new JSONObject();

		// CREATE / REGISTER
		json.put("name", "TestUser15");
		json.put("email", "email15@fhnw.ch");
		json.put("password", "password2");

		User user2 = doPost("http://localhost:8080/user", null, json, User.class);
		User userFromDb2 = userService.getById(user2.getId());
		Assert.assertEquals("TestUser15", userFromDb2.getName());
		Assert.assertEquals("email15@fhnw.ch", userFromDb2.getEmail());
		Assert.assertNotNull(user.getId());

		// VALIDATE EMAIL ADDRESS
		Boolean success = doGet("http://localhost:8080/validate?email={0}&validationCode={1}", null, Boolean.class,
				userFromDb2.getEmail(), userFromDb2.getLoginData().getValidationCode());
		Assert.assertTrue(success);
		userFromDb2 = userService.getById(user2.getId());
		Assert.assertTrue(userFromDb2.getLoginData().isValidated());

		// REQUEST TOKEN
		Token token = doPost("http://localhost:8080/login", null, json, Token.class);

		Board board1 = BoardFactory.getInstance().createBoard("Board1", user);
		board1 = boardService.saveBoard(board1);
		Assert.assertNotNull(board1.getId());

		Board board2 = BoardFactory.getInstance().createBoard("Board2", user2);
		board2 = boardService.saveBoard(board2);
		Assert.assertNotNull(board2.getId());

		Board board3 = BoardFactory.getInstance().createBoard("Board3", user2);
		board3 = boardService.saveBoard(board3);
		Assert.assertNotNull(board3.getId());

		try {
			Task task1 = TaskFactory.getInstance().createTask(board1, "Task1");
			JSONParser parser = new JSONParser();
			JSONObject task1json = (JSONObject) parser.parse(objectMapper.writeValueAsString(task1));
			task1 = doMulitPartPostTask("http://localhost:8080/task", token, task1json, null);
			Assert.assertNotNull(task1.getId());
			Assert.fail();
		} catch (IOException e) {
		} catch (Exception e) {
			Assert.fail();
		}

		try {
			Task task2 = TaskFactory.getInstance().createTask(board2, "Task2");
			JSONParser parser = new JSONParser();
			JSONObject task1json = (JSONObject) parser.parse(objectMapper.writeValueAsString(task2));
			task2 = doMulitPartPostTask("http://localhost:8080/task", token, task1json, null);
			Assert.assertNotNull(task2.getId());
		} catch (IOException e) {
			Assert.fail();
		} catch (Exception e) {
			Assert.fail();
		}

	}
	
	/**
	 * Tests deleting a single task by id, if
	 * the task belongs to the board the user is subscribed to.
	 * 
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void testDeleteTask() throws Exception {
		JSONObject json = new JSONObject();

		// CREATE / REGISTER
		json.put("name", "TestUser16");
		json.put("email", "email16@fhnw.ch");
		json.put("password", "password");

		User user = doPost("http://localhost:8080/user", null, json, User.class);
		User userFromDb = userService.getById(user.getId());
		Assert.assertEquals("TestUser16", userFromDb.getName());
		Assert.assertEquals("email16@fhnw.ch", userFromDb.getEmail());
		Assert.assertNotNull(user.getId());

		json = new JSONObject();

		// CREATE / REGISTER
		json.put("name", "TestUser17");
		json.put("email", "email17@fhnw.ch");
		json.put("password", "password2");

		User user2 = doPost("http://localhost:8080/user", null, json, User.class);
		User userFromDb2 = userService.getById(user2.getId());
		Assert.assertEquals("TestUser17", userFromDb2.getName());
		Assert.assertEquals("email17@fhnw.ch", userFromDb2.getEmail());
		Assert.assertNotNull(user.getId());

		// VALIDATE EMAIL ADDRESS
		Boolean success = doGet("http://localhost:8080/validate?email={0}&validationCode={1}", null, Boolean.class,
				userFromDb2.getEmail(), userFromDb2.getLoginData().getValidationCode());
		Assert.assertTrue(success);
		userFromDb2 = userService.getById(user2.getId());
		Assert.assertTrue(userFromDb2.getLoginData().isValidated());

		// REQUEST TOKEN
		Token token = doPost("http://localhost:8080/login", null, json, Token.class);

		Board board1 = BoardFactory.getInstance().createBoard("Board1", user);
		board1 = boardService.saveBoard(board1);
		Assert.assertNotNull(board1.getId());

		Board board2 = BoardFactory.getInstance().createBoard("Board2", user2);
		board2 = boardService.saveBoard(board2);
		Assert.assertNotNull(board2.getId());

		Board board3 = BoardFactory.getInstance().createBoard("Board3", user2);
		board3 = boardService.saveBoard(board3);
		Assert.assertNotNull(board3.getId());

		Task task1 = TaskFactory.getInstance().createTask(board1, "Task1");
		task1 = taskService.saveTask(task1);
		Assert.assertNotNull(task1.getId());

		Task task2 = TaskFactory.getInstance().createTask(board2, "Task2");
		task2 = taskService.saveTask(task2);
		Assert.assertNotNull(task2.getId());

		Task task3 = TaskFactory.getInstance().createTask(board3, "Task3");
		task3 = taskService.saveTask(task3);
		Assert.assertNotNull(task3.getId());

		boolean result = false;
		try{
			result = doDelete("http://localhost:8080/task/{0}", token, Boolean.class, task1.getId());
			Assert.fail();
		} catch (IOException e) {
		} catch (Exception e){
			Assert.fail();
		}
		Assert.assertFalse(result);
		
		try{
			result = doDelete("http://localhost:8080/task/{0}", token, Boolean.class, task2.getId());
		} catch (IOException e) {
			Assert.fail();
		} catch (Exception e){
			Assert.fail();
		}
		Assert.assertTrue(result);

	}

	/**
	 * Tests modifying a single task by id, if
	 * the task belongs to the board the user is subscribed to.
	 * 
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void testModifyTask() throws Exception {
		JSONObject json = new JSONObject();
	
		// CREATE / REGISTER
		json.put("name", "TestUser18");
		json.put("email", "email18@fhnw.ch");
		json.put("password", "password");
	
		User user = doPost("http://localhost:8080/user", null, json, User.class);
		User userFromDb = userService.getById(user.getId());
		Assert.assertEquals("TestUser18", userFromDb.getName());
		Assert.assertEquals("email18@fhnw.ch", userFromDb.getEmail());
		Assert.assertNotNull(user.getId());
	
		json = new JSONObject();
	
		// CREATE / REGISTER
		json.put("name", "TestUser19");
		json.put("email", "email19@fhnw.ch");
		json.put("password", "password2");
	
		User user2 = doPost("http://localhost:8080/user", null, json, User.class);
		User userFromDb2 = userService.getById(user2.getId());
		Assert.assertEquals("TestUser19", userFromDb2.getName());
		Assert.assertEquals("email19@fhnw.ch", userFromDb2.getEmail());
		Assert.assertNotNull(user.getId());
	
		// VALIDATE EMAIL ADDRESS
		Boolean success = doGet("http://localhost:8080/validate?email={0}&validationCode={1}", null, Boolean.class,
				userFromDb2.getEmail(), userFromDb2.getLoginData().getValidationCode());
		Assert.assertTrue(success);
		userFromDb2 = userService.getById(user2.getId());
		Assert.assertTrue(userFromDb2.getLoginData().isValidated());
	
		// REQUEST TOKEN
		Token token = doPost("http://localhost:8080/login", null, json, Token.class);
	
		Board board1 = BoardFactory.getInstance().createBoard("Board1", user);
		board1 = boardService.saveBoard(board1);
		Assert.assertNotNull(board1.getId());
	
		Board board2 = BoardFactory.getInstance().createBoard("Board2", user2);
		board2 = boardService.saveBoard(board2);
		Assert.assertNotNull(board2.getId());
	
		Board board3 = BoardFactory.getInstance().createBoard("Board3", user2);
		board3 = boardService.saveBoard(board3);
		Assert.assertNotNull(board3.getId());
	
		Task task1 = TaskFactory.getInstance().createTask(board1, "Task1");
		task1 = taskService.saveTask(task1);
		Assert.assertNotNull(task1.getId());
	
		Task task2 = TaskFactory.getInstance().createTask(board2, "Task2");
		task2 = taskService.saveTask(task2);
		Assert.assertNotNull(task2.getId());
	
		Task task3 = TaskFactory.getInstance().createTask(board3, "Task3");
		task3 = taskService.saveTask(task3);
		Assert.assertNotNull(task3.getId());
	
		JSONParser parser = new JSONParser();
		
		try{
			task1.setDescription("OtherDescription1");
			JSONObject task1json = (JSONObject) parser.parse(objectMapper.writeValueAsString(task1));
			task1 = doMulitPartPutTask("http://localhost:8080/task/{0}", token, task1json, null, task1.getId());
			Assert.fail();
		} catch (IOException e) {
		} catch (Exception e){
			Assert.fail();
		}
		
		try{
			task2.setDescription("OtherDescription2");
			JSONObject task2json = (JSONObject) parser.parse(objectMapper.writeValueAsString(task2));
			task2 = doMulitPartPutTask("http://localhost:8080/task/{0}", token, task2json, null, task2.getId());
			Assert.assertEquals("OtherDescription2", task2.getDescription());
		} catch (IOException e) {
			Assert.fail();
		} catch (Exception e){
			Assert.fail();
		}
	
	}

}