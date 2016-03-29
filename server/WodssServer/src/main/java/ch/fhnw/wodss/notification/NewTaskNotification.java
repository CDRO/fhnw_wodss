package ch.fhnw.wodss.notification;

import java.util.LinkedList;
import java.util.List;

import ch.fhnw.wodss.domain.Task;
import ch.fhnw.wodss.domain.User;

public class NewTaskNotification extends AbstractNotification {
	
	private Task task;

	public NewTaskNotification(Task task) {
		super();
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
		return "New Taks for you!";
	}

	@Override
	String getMessageBody() {
		return "<p>New task has been created for you. "
				+ "Look it up with the following link:</p><p><a href=\"#\">LINK</a></p>";
	}

}
