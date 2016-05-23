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
		return "Neuer Task für Dich!";
	}

	@Override
	String getMessageBody() {
		return "<p>Ein neuer Task wurde für Dich erstellt. "
				+ "Geh und sehe nach! </p><p><a href=\"https://www.cs.technik.fhnw.ch/wodss5/#/tasks/board/" + task.getBoard().getId() + "\">LINK</a></p>";
	}

}
