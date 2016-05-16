package ch.fhnw.wodss.notification;

import org.junit.Test;

import ch.fhnw.wodss.domain.Board;
import ch.fhnw.wodss.domain.BoardFactory;
import ch.fhnw.wodss.domain.Task;
import ch.fhnw.wodss.domain.TaskFactory;
import ch.fhnw.wodss.domain.User;
import ch.fhnw.wodss.domain.UserFactory;

public class TestNewTaskNotification {

	@Test
	public void testNotification(){
		User user = UserFactory.getInstance().createUser("TestUser", "test@email.com");
		Board board = BoardFactory.getInstance().createBoard("TestBaoard", user);
		Task task = TaskFactory.getInstance().createTask(board, "TestTask", user);
		NewTaskNotification notification = new NewTaskNotification(task);
		notification.send();
	}
	
}
