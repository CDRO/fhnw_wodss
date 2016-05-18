package ch.fhnw.wodss.notification;

import java.util.LinkedList;
import java.util.List;

import ch.fhnw.wodss.domain.User;

public class ReassignedNotification extends AbstractNotification {

	private User user;

	public ReassignedNotification(User user) {
		this.user = user;
	}

	@Override
	List<User> getRecipients() {
		List<User> recipients = new LinkedList<>();
		recipients.add(user);
		return recipients;
	}

	@Override
	String getSubject() {
		return "You have been assigned for the following Task";
	}

	@Override
	String getMessageBody() {
		return "You have been assigned for a new Task. Go to <a href=\"https://www.cs.technik.fhnw.ch/wodss5/\">ToDooooo</a> now.";
	}

}
