package project.buisnesslogic;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class AcknowledgementForInvoice {

	String mail;

	public AcknowledgementForInvoice(String mail) {
		this.mail = mail;
	}

	public void sendMail(String message) throws AddressException, MessagingException {
		Properties properties = new Properties();
		// Session mailSession;
		// MimeMessage emailMessage;
		try {
			properties.load(new FileReader(
					"C:\\Users\\ssaravanan6384\\workspace\\PDFExtractorProject\\src\\sampletry\\config.properties"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// AcknowledgementForInvoice javaEmail = new
		// AcknowledgementForInvoice();

		/*
		 * javaEmail.setMailServerProperties();
		 * javaEmail.createEmailMessage(message); javaEmail.sendEmail();
		 */

		String[] toEmails = { mail };
		String emailSubject = "Invoice Approval";
		String emailBody = message;

		Session mailSession = Session.getDefaultInstance(properties);
		MimeMessage emailMessage = new MimeMessage(mailSession);

		for (int i = 0; i < toEmails.length; i++) {
			emailMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(toEmails[i]));
		}

		emailMessage.setSubject(emailSubject);
		emailMessage.setContent(emailBody, "text/html");// for a html email
		// emailMessage.setText(emailBody);// for a text email

		/*
		 * String emailHost = "smtp.gmail.com"; String fromUser =
		 * "saravanansanjay194@gmail.com";//just the id alone without @gmail.com
		 * String fromUserEmailPassword = "9790889427";
		 */

		Transport transport = mailSession.getTransport("smtp");

		transport.connect(properties.getProperty("emailHost"), properties.getProperty("userName"),
				properties.getProperty("password"));
		transport.sendMessage(emailMessage, emailMessage.getAllRecipients());
		transport.close();
		// System.out.println("Email sent successfully.");
	}
}