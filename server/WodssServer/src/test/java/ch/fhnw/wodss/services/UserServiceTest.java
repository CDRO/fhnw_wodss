package ch.fhnw.wodss.services;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import ch.fhnw.wodss.WodssServer;
import ch.fhnw.wodss.domain.Board;
import ch.fhnw.wodss.domain.BoardFactory;
import ch.fhnw.wodss.domain.Task;
import ch.fhnw.wodss.domain.TaskFactory;
import ch.fhnw.wodss.domain.User;
import ch.fhnw.wodss.domain.UserFactory;
import ch.fhnw.wodss.service.BoardService;
import ch.fhnw.wodss.service.TaskService;
import ch.fhnw.wodss.service.UserService;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(WodssServer.class)
@WebIntegrationTest
public class UserServiceTest {

	@Autowired
	private UserService userService;
	
	@Autowired
	private BoardService boardService;
	
	@Autowired
	private TaskService taskService;
	
	@Test
	public void testGetById(){
		User user = UserFactory.getInstance().createUser("Username", "email");
		userService.saveUser(user);
		
		Board board = BoardFactory.getInstance().createBoard("BoardTitle", user);
		boardService.saveBoard(board);
		
		Task task = TaskFactory.getInstance().createTask(board, "TaskDescription");
		taskService.saveTask(task);
		
		User loadedUser = userService.getById(user.getId());
		Assert.assertEquals(1, loadedUser.getBoards().size());
		
	}
}
