package app.vcampus.utils;

import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;

/**
 * Utility class for sending emails using QQ Mail's SMTP server.
 */
public class EmailService {
    private final String username = "2385321200@qq.com"; // QQ email address
    private final String password = "sxqmtwysjnhhebgf"; // QQ email authorization code

    /**
     * Sends an email to the specified recipient.
     *
     * @param to the recipient's email address
     * @param subject the subject of the email
     * @param body the body of the email
     */
    public void sendEmail(String to, String subject, String body) {
        // Configure QQ Mail's SMTP server properties
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.qq.com");
        props.put("mail.smtp.port", "587");

        // Create a mail session
        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            // Construct the email message
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject(subject);
            message.setText(body);

            // Send the email
            Transport.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}