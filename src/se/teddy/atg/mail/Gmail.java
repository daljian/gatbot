package se.teddy.atg.mail;


import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class Gmail {

  public static void sendNotification(String subject, String notification) {


    final String username = "gatbotnotify@gmail.com";
    final String password = "F6$XdTY90#!SkiResort";
    Properties props = new Properties();
    props.put("mail.smtp.auth", "true");
    props.put("mail.smtp.starttls.enable", "true");
    props.put("mail.smtp.host", "smtp.gmail.com");
    props.put("mail.smtp.port", "587");

    Session session = Session.getInstance(props,
            new javax.mail.Authenticator() {
              protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
              }
            });

    try {

      Message message = new MimeMessage(session);
      message.setFrom(new InternetAddress("gatbotnotify@gmail.com"));
      message.setRecipients(Message.RecipientType.TO,
              InternetAddress.parse("goran@familjenengdahl.se"));
      message.setSubject(subject);
      message.setText(notification);

      Transport.send(message);

    } catch (MessagingException e) {
      throw new RuntimeException(e);
    }
  }
}
