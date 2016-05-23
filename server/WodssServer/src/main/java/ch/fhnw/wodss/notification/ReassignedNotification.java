package ch.fhnw.wodss.notification;

import java.util.LinkedList;
import java.util.List;

import ch.fhnw.wodss.domain.Task;
import ch.fhnw.wodss.domain.User;

public class ReassignedNotification extends AbstractNotification {

	private Task task;

	public ReassignedNotification(Task task) {
		this.task = task;
	}

	@Override
	List<User> getRecipients() {
		List<User> recipients = new LinkedList<>();
		recipients.add(task.getAssignee());
		return recipients;
	}

	@Override
	String getSubject() {
		return "Du wurdest zu einem Task zugewiesen.";
	}

	@Override
	String getMessageBody() {
		return "Du wurdest zu einem Task zugewiesen. Siehe <a href=\"https://www.cs.technik.fhnw.ch/wodss5/#/tasks/board/"
				+ task.getBoard() + "\">ToDooooo</a>.";
	}

}
