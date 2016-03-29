package ch.fhnw.wodss.notification;

import java.util.List;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import ch.fhnw.wodss.domain.User;

public abstract class AbstractNotification {

	/**
	 * Flag for test mode.
	 */
	private boolean testMode = true;

	/**
	 * The mail session.
	 */
	private Session session;

	/**
	 * The host.
	 */
	private static final String HOST = "lmailer.fhnw.ch";

	/**
	 * The local host domain. (HELO)
	 */
	private static final String LOCAL_HOST_DOMAIN = "cs.fhnw.ch";

	/**
	 * The port.
	 */
	private static final int PORT = 25;

	/**
	 * The sender of every notification.
	 */
	private static final String SENDER = "wodss5@fhnw.ch";

	/**
	 * The prefix of every notification subject.
	 */
	private static final String SUBJECT_PREFIX = "[WODSS5] ";

	/**
	 * Constructor.
	 */
	AbstractNotification() {
		super();
		init();
	}

	/**
	 * Initializes the mail session.
	 */
	private void init() {
		Properties props = new Properties();
		props.put("mail.smtp.host", HOST);
		props.put("mail.smtp.localhost", LOCAL_HOST_DOMAIN);
		props.put("mail.smtp.port", PORT);
		session = Session.getInstance(props);
	}

	/**
	 * Sends the notification.
	 */
	public void send() {
		if (testMode) {
			System.out.println("");
			System.out.println(
					"============================================== EMAIL NOTIFICATION ==============================================");
			System.out.println("FROM:\t\t" + SENDER);
			System.out.println("TO:\t\t" + getRecipients().toString());
			System.out.println("SUBJECT:\t" + SUBJECT_PREFIX + getSubject());
			System.out.println("MESSAGE:\t" + getMessageBody());
			System.out.println(
					"================================================================================================================");
			System.out.println("");
			return;
		}
		try {
			// Create a default MimeMessage object.
			Message message = new MimeMessage(session);

			// Set From: header field of the header.
			message.setFrom(new InternetAddress(SENDER));

			// Set To: header field of the header.
			for (User recipient : getRecipients()) {
				message.addRecipient(Message.RecipientType.TO, new InternetAddress(recipient.getEmail()));
			}

			// Set Subject: header field
			message.setSubject(SUBJECT_PREFIX + getSubject());

			// Send the actual HTML message, as big as you like
			message.setContent(getMessageBody(), "text/html");

			// Send message
			Transport.send(message);

		} catch (MessagingException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	/**
	 * Returns a list of recipients.
	 * 
	 * @return the list of recipients.
	 */
	abstract List<User> getRecipients();

	/**
	 * Returns the subject of the notification message.
	 * 
	 * @return the subject of the notification message.
	 */
	abstract String getSubject();

	/**
	 * Returns the body of the message. Can be HTML code.
	 * 
	 * @return the body of the message.
	 */
	abstract String getMessageBody();

	/**
	 * @return the testMode
	 */
	public boolean isTestMode() {
		return testMode;
	}

	/**
	 * @param testMode
	 *            the testMode to set
	 */
	public void setTestMode(boolean testMode) {
		this.testMode = testMode;
	}

}
