package ch.fhnw;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class JavaMailTester {

  /**
   * The host.
   */
  private static final String HOST = "lmailer.fhnw.ch";

  /**
   * The local host domain. (HELO)
   */
  private static final String LOCAL_HOST_DOMAIN = "server1070";

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


  public static void main(String[] args){
    Properties props = new Properties();
    props.put("mail.smtp.host", HOST);
    props.put("mail.smtp.localhost", LOCAL_HOST_DOMAIN);
    props.put("mail.smtp.port", PORT);
    Session session = Session.getInstance(props);
  

    try {
        // Create a default MimeMessage object.
        Message message = new MimeMessage(session);

        // Set From: header field of the header.
        message.setFrom(new InternetAddress(SENDER));

        // Set To: header field of the header.
        message.addRecipient(Message.RecipientType.TO, new InternetAddress("tobias.giess@gmail.com"));


        // Set Subject: header field
        message.setSubject(SUBJECT_PREFIX + " TEST");

        // Send the actual HTML message, as big as you like
        message.setContent("TEST", "text/html");

        // Send message
        Transport.send(message);

      } catch (MessagingException e) {
        e.printStackTrace();
        throw new RuntimeException(e);
      }

    }
}