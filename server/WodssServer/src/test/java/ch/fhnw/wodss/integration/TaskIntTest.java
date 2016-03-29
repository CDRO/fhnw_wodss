package ch.fhnw.wodss.integration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.MessageFormat;
import java.util.LinkedList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.HttpClientBuilder;
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

		JSONObject json = new JSONObject();
		json.put("email", "email@fhnw.ch");
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

		// IVALID TOKEN
		Token token = TokenHandler.register();
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
		json.put("email", "email@fhnw.ch");
		json.put("password", "password");

		// REQUEST TOKEN
		Token token = doPost("http://localhost:8080/login", null, json, Token.class);

		// CREATE
		json.clear();
		json.put("description", "TestTask");

		Task task = doMulitPartPostTask("http://localhost:8080/task", token, json, files);
		Assert.assertEquals(1, task.getId().intValue());
		Task taskFromDb = taskService.getById(task.getId());
		Assert.assertEquals("TestTask", taskFromDb.getDescription());
		
		// TODO test delete task with attachment!!
	}

	private Task doMulitPartPostTask(String url, Token token, JSONObject json, List<File> files, Object... urlParameters)
			throws Exception {

		HttpClient httpClient = HttpClientBuilder.create().build();

		MultipartEntityBuilder mpBuilder = MultipartEntityBuilder.create();
		mpBuilder.addTextBody("task", json.toJSONString(), ContentType.APPLICATION_JSON);
		if(files != null){
			for(File file : files){
				mpBuilder.addBinaryBody("file", file);
			}
		}
		HttpEntity entity = mpBuilder.build();

		String formattedUrl = MessageFormat.format(url, urlParameters);

		HttpPost httpPost = new HttpPost(formattedUrl);
		httpPost.setHeader("x-session-token", token.getId());
		httpPost.setEntity(entity);
		HttpResponse response = httpClient.execute(httpPost);
		HttpEntity result = response.getEntity();

		InputStream is = result.getContent();
		Task readValue = objectMapper.readValue(is, Task.class);
		return readValue;

	}

	private Task doMulitPartPutTask(String url, Token token, JSONObject json, File file, Object... urlParameters)
			throws Exception {

		HttpClient httpClient = HttpClientBuilder.create().build();

		MultipartEntityBuilder mpBuilder = MultipartEntityBuilder.create();
		mpBuilder.addTextBody("task", json.toJSONString(), ContentType.APPLICATION_JSON);
		if(file != null){
			mpBuilder.addBinaryBody("file", file);
		}
		HttpEntity entity = mpBuilder.build();

		String formattedUrl = MessageFormat.format(url, urlParameters);

		HttpPut httpPut = new HttpPut(formattedUrl);
		httpPut.setHeader("x-session-token", token.getId());
		httpPut.setEntity(entity);
		HttpResponse response = httpClient.execute(httpPut);
		HttpEntity result = response.getEntity();

		InputStream is = result.getContent();
		Task readValue = objectMapper.readValue(is, Task.class);
		return readValue;

	}
}