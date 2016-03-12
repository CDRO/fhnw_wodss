package ch.fhnw.wodss.integration;

import java.io.IOException;

import org.json.simple.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import ch.fhnw.wodss.domain.Task;
import ch.fhnw.wodss.security.Token;
import ch.fhnw.wodss.security.TokenHandler;
import ch.fhnw.wodss.service.TaskService;

public class TaskIntTest extends AbstractIntegrationTest {

	@Autowired
	private TaskService taskService;

	@SuppressWarnings("unchecked")
	@Test
	public void testTaskAuthorizedCRUD() throws Exception {

		// REQUEST TOKEN
		Token token = doGet("http://localhost:8080/token", null, Token.class);

		// CREATE
		JSONObject json = new JSONObject();
		json.put("description", "TestTask");

		Task task = doPost("http://localhost:8080/task", token, json, Task.class);
		Assert.assertEquals(1, task.getId().intValue());
		Task taskFromDb = taskService.getById(task.getId());
		Assert.assertEquals("TestTask", taskFromDb.getDescription());

		// READ
		task = doGet("http://localhost:8080/task/{0}", token, Task.class, taskFromDb.getId());
		Assert.assertNotNull(task);
		Assert.assertEquals("TestTask", task.getDescription());
		Assert.assertEquals(1, task.getId().intValue());

		// UPDATE
		json = new JSONObject();
		json.put("id", task.getId());
		json.put("description", "TestTask2");
		task = doPut("http://localhost:8080/task/{0}", token, json, Task.class, task.getId());
		taskFromDb = taskService.getById(task.getId());
		Assert.assertEquals("TestTask2", taskFromDb.getDescription());
		Assert.assertEquals("TestTask2", task.getDescription());
		Assert.assertEquals(1, task.getId().intValue());

		// DELETE
		doDelete("http://localhost:8080/task/{0}", token, Boolean.class, task.getId());
		taskFromDb = taskService.getById(task.getId());
		Assert.assertNull(taskFromDb);

	}

	@SuppressWarnings("unchecked")
	@Test
	public void testTaskUnauthorizedCRUD() {

		// IVALID TOKEN
		Token token = TokenHandler.register();
		token.setId("asdf-asdf-asdf-asdf");

		JSONObject json = new JSONObject();
		json.put("description", "TestTask");


		try {
			// CREATE
			doPost("http://localhost:8080/task", token, json, Task.class);
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
			doPut("http://localhost:8080/task/{0}", token, json, Task.class, 1);
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
}