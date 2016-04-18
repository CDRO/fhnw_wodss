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
		return "Thank you for your registration";
	}

	@Override
	String getMessageBody() {
		return "Please validate your email address by clicking on the following link: <a href=\"https://cs.technik.fhnw.ch/wodss5/validation/user?id="
				+ user.getId() + "&validationCode=" + user.getLoginData().getValidationCode() + "\">LINK</a>";
	}

}
