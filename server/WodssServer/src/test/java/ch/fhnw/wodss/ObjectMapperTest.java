package ch.fhnw.wodss;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Assert;
import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

import ch.fhnw.wodss.domain.Attachment;
import ch.fhnw.wodss.domain.AttachmentFactory;
import ch.fhnw.wodss.domain.Board;
import ch.fhnw.wodss.domain.BoardFactory;
import ch.fhnw.wodss.domain.Task;
import ch.fhnw.wodss.domain.TaskFactory;
import ch.fhnw.wodss.domain.User;
import ch.fhnw.wodss.domain.UserFactory;

public class ObjectMapperTest {

	@Test
	public void testMappingTask() throws Exception {
		User user = UserFactory.getInstance().createUser("TG", "tg@fhnw.ch");
		Board board = BoardFactory.getInstance().createBoard("Board", user);
		Task task = TaskFactory.getInstance().createTask(board, "Task");
		task.setId(1);
		Attachment attachment = AttachmentFactory.getInstance().createAttachment(task);
		attachment.setId("asdffasd-sadf-asdf-asdf");
		task.setAttachments(new ArrayList<>(Arrays.asList(attachment)));
		
		ObjectMapper mapper = new ObjectMapper();
		String taskjson = mapper.writeValueAsString(task);
		System.out.println(taskjson);
		
		String json = "{\"id\":1,\"board\":{\"id\":null,\"title\":\"Board\",\"owner\":{\"id\":null,\"name\":\"TG\",\"email\":\"tg@fhnw.ch\"},\"users\":[{\"id\":null,\"name\":\"TG\",\"email\":\"tg@fhnw.ch\"}]},\"state\":\"TODO\",\"assignee\":null,\"creationDate\":1464023015201,\"dueDate\":null,\"doneDate\":null,\"description\":\"Task\",\"attachments\":[{\"id\":\"asdffasd-sadf-asdf-asdf\",\"documentName\":null,\"extension\":null, \"task\":{\"id\":1}}]}";
		Task readValue = mapper.readValue(json, Task.class);
		Assert.assertNotNull(readValue.getAttachments().get(0).getTask());
	}
}
