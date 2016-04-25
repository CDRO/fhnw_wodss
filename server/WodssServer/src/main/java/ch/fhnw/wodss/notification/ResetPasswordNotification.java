package ch.fhnw.wodss.notification;

import java.util.LinkedList;
import java.util.List;

import ch.fhnw.wodss.domain.User;

public class ResetPasswordNotification extends AbstractNotification {

	private User user;

	public ResetPasswordNotification(User user) {
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
		return "Reset you password";
	}

	@Override
	String getMessageBody() {
		return "You requested a password reset link. Followi this link to reset your password: <a href=\"https://cs.technik.fhnw.ch/wodss5/validation/user?id="
				+ user.getId() + "&resetCode=" + user.getLoginData().getResetCode() + "\">LINK</a>";
	}

}
