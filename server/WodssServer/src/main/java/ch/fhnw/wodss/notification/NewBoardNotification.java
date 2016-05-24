package ch.fhnw.wodss.notification;

import java.util.LinkedList;
import java.util.List;

import ch.fhnw.wodss.domain.User;

public class NewBoardNotification extends AbstractNotification {

	private User user;

	public NewBoardNotification(User user) {
		super();
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
		return "Du wurdest einem Board Hinzugefügt";
	}

	@Override
	String getMessageBody() {
		return "Du wurdest einem Board hinzugefügt. Logge dich jetzt ein unter <a href=\"https://www.cs.technik.fhnw.ch/wodss5/\">LINK</a>";
	}

}
