package ch.fhnw.wodss.integration;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import ch.fhnw.wodss.domain.Board;
import ch.fhnw.wodss.domain.Task;
import ch.fhnw.wodss.service.TaskService;

public class TaskIntTest extends AbstractIntegrationTest {

	private RestTemplate restTemplate = new TestRestTemplate();

	private ObjectMapper objectMapper = new ObjectMapper();

	@Autowired
	private TaskService taskService;

	@Test
	public void testTaskCRUD() throws JsonProcessingException {
		// CREATE
		Map<String, Object> requestBody = new HashMap<String, Object>();
		requestBody.put("description", "TestTask");
		HttpHeaders requestHeaders = new HttpHeaders();
		requestHeaders.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> httpEntity = new HttpEntity<String>(objectMapper.writeValueAsString(requestBody),
				requestHeaders);
		Task task = restTemplate.postForObject("http://localhost:8080/task", httpEntity, Task.class);
		Assert.assertNotNull(task.getId());
		Task taskFromDb = taskService.getById(task.getId());
		Assert.assertEquals("TestTask", taskFromDb.getDescription());
		
		// READ
		task = restTemplate.getForObject("http://localhost:8080/task/{0}", Task.class, task.getId());
		// ----> here
		Assert.assertNotNull(task);
		Assert.assertEquals("TestTask", task.getDescription());
		Board[] boards = restTemplate.getForObject("http://localhost:8080/tasks", Board[].class, new Object[0]);
		Assert.assertEquals(1, boards.length);
		
		// UPDATE
		task.setDescription("TestTask2");
		restTemplate.put("http://localhost:8080/task/{0}", task, task.getId());
		taskFromDb = taskService.getById(task.getId());
		Assert.assertEquals("TestTask2", taskFromDb.getDescription());
		
		// DELETE
		restTemplate.delete("http://localhost:8080/task/{0}", task.getId());
		taskFromDb = taskService.getById(task.getId());
		Assert.assertNull(taskFromDb);

	}
}