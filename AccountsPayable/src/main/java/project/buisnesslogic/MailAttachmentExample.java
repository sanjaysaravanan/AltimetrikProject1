package project.buisnesslogic;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.NoSuchProviderException;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.MimeBodyPart;

public class MailAttachmentExample {
	private String saveDirectory;

	/**
	 * Sets the directory where attached files will be stored.
	 * 
	 * @param dir
	 *            absolute path of the directory
	 */
	public void setSaveDirectory(String dir) {
		this.saveDirectory = dir;
	}

	/**
	 * Downloads new messages and saves attachments to disk if any.
	 * 
	 * @param host
	 * @param port
	 * @param userName
	 * @param password
	 */
	public String[] downloadEmailAttachments() {
		String[] from = { "", "" };
		Properties properties = new Properties();
		try {
			properties.load(new FileReader(
					"C:\\Users\\ssaravanan6384\\workspace\\PDFExtractorProject\\src\\sampletry\\config.properties"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		Session session = Session.getDefaultInstance(properties);

		try {
			// connects to the message store
			Store store = session.getStore("pop3");
			store.connect(properties.getProperty("userName"), properties.getProperty("password"));

			// opens the inbox folder
			Folder folderInbox = store.getFolder("INBOX");
			folderInbox.open(Folder.READ_ONLY);

			// fetches new messages from server
			Message[] arrayMessages = folderInbox.getMessages();

			for (int i = 0; i < arrayMessages.length; i++) {
				Message message = arrayMessages[i];
				Address[] fromAddress = message.getFrom();
				String fromMail = fromAddress[0].toString();
				from[0] = fromMail.substring(fromMail.indexOf('<') + 1, fromMail.indexOf('>'));
				String subject = message.getSubject();
				String sentDate = message.getSentDate().toString();

				String contentType = message.getContentType();
				String messageContent = "";

				// store attachment file name, separated by comma
				String attachFiles = "";

				if (contentType.contains("multipart")) {
					// content may contain attachments
					Multipart multiPart = (Multipart) message.getContent();
					int numberOfParts = multiPart.getCount();
					for (int partCount = 0; partCount < numberOfParts; partCount++) {
						MimeBodyPart part = (MimeBodyPart) multiPart.getBodyPart(partCount);
						if (Part.ATTACHMENT.equalsIgnoreCase(part.getDisposition())) {
							// this part is attachment
							String fileName = part.getFileName();
							attachFiles += fileName + ", ";
							part.saveFile(saveDirectory + File.separator + fileName);
						} else {
							// this part may be the message content
							messageContent = part.getContent().toString();
						}
					}

					if (attachFiles.length() > 1) {
						attachFiles = attachFiles.substring(0, attachFiles.length() - 2);
					}
				} else if (contentType.contains("text/plain") || contentType.contains("text/html")) {
					Object content = message.getContent();
					if (content != null) {
						messageContent = content.toString();
					}
				}
				from[1] = attachFiles;
				// print out details of each message
				/*
				 * System.out.println("Message #" + (i + 1) + ":");
				 * System.out.println("\t From: " + fromMail);
				 * System.out.println("\t Subject: " + subject);
				 * System.out.println("\t Sent Date: " + sentDate);
				 * System.out.println("\t Message: " + messageContent);
				 */
				// System.out.println("\t Attachments: " + attachFiles);
			}

			// disconnect
			folderInbox.close(false);
			store.close();
		} catch (NoSuchProviderException ex) {
			System.out.println("No provider for pop3.");
			ex.printStackTrace();
		} catch (MessagingException ex) {
			System.out.println("Could not connect to the message store");
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		} catch (Exception e) {

		}
		return from;
	}

	/**
	 * Runs this program with Gmail POP3 server
	 */
	public String[] storeAttachments() {

		String[] str = null;
		String saveDirectory = "C:\\Users\\ssaravanan6384\\Desktop\\Project\\EmailExtractedAttachment";

		MailAttachmentExample receiver = new MailAttachmentExample();
		receiver.setSaveDirectory(saveDirectory);
		str = receiver.downloadEmailAttachments();

		return str;
	}
}
