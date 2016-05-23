package ch.fhnw.wodss.notification;

import java.util.LinkedList;
import java.util.List;

import ch.fhnw.wodss.domain.User;

public class RegistrationNotification extends AbstractNotification {

	private User user;

	public RegistrationNotification(User user) {
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
		return "Danke für Deine Registration";
	}

	@Override
	String getMessageBody() {
		return "Bevor du ToDooooo nutzen kannst, musst Du noch deine E-Mail-Adresse validieren. Dies kannst du unter folgendem Link tun: <a href=\"https://www.cs.technik.fhnw.ch/wodss5/#/validate/"
				+ user.getId() + "?validationCode=" + user.getLoginData().getValidationCode() + "\">LINK</a>";
	}

}
