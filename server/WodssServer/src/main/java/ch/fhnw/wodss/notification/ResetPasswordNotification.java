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
		return "Passwort zurücksetzen";
	}

	@Override
	String getMessageBody() {
		return "Du möchtest dein Passwort zurücksetzen? Folge diesem Link: <a href=\"https://cs.technik.fhnw.ch/wodss5/#/reset/" + user.getLoginData().getResetCode() + "\">LINK</a>";
	}

}
