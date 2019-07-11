package project.accountspayable;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import project.buisnesslogic.Invoice;
import project.buisnesslogic.MailAttachmentExample;
import project.buisnesslogic.ReadPdf;
import project.datalayer.Database;

public class App {

	static Database b = new Database();

	public static void main(String[] args) {
		Thread thread = new Thread(new App().new App1());
		Scanner scan = new Scanner(System.in);
		b.createConnection();
		thread.start();
		try {
			while (true) {
				System.out.println(
						"************************************************************************************\n");
				System.out.println("Accounts Payable Application");
				System.out.println();
				System.out.println(
						"************************************************************************************\n");
				System.out.println("Available Options");
				System.out.println("1.List All Invoice Details");
				System.out.println("2.Approve Invoice");
				System.out.println("3.Exit");
				System.out.println(
						"************************************************************************************");
				System.out.println("Enter Your Option : ");
				int choice = scan.nextInt();
				switch (choice) {
				case 1:
					System.out.println();
					System.out.println(
							"************************************************************************************");
					b.listInvoiceDetails();
					Thread.sleep(1200);
					System.out.println("Go back (y/n) : ");
					while (scan.next().equals("n")) {
						Thread.sleep(12000);
						System.out.println("Go back (y/n) : ");
					}
					break;
				case 2:
					while (true) {
						System.out.println();
						System.out.println(
								"************************************************************************************");
						if (b.listUnprovedInvoice()) {
							System.out.println("Enter a valid Invoice No For Approval : ");
							String invoiceNo = scan.next();
							if (!b.approveInvoice(invoiceNo)) {
								System.out.println("Please Enter a valid Invoice No:");
							} else {
								System.out.println("Do you wish to approve more (y/n) : ");
								if (scan.next().equals("y")) {
									continue;
								} else {
									break;
								}
							}
						} else {
							Thread.sleep(1200);
							break;
						}
					}
					break;
				case 3:
					System.out.println("Thank You !!!");
					System.out.println(
							"************************************************************************************");
					System.exit(0);
					break;
				}
				System.out.println();
				System.out.println(
						"************************************************************************************");
				System.out.println();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			b.closeConnection();
			scan.close();
		}
	}

	private class App1 implements Runnable {

		public void run() {
			while (true) {
				try {
					MailAttachmentExample attachments = new MailAttachmentExample();
					String[] fromMail1 = attachments.storeAttachments();
					if (fromMail1[1] != null)
						if (!fromMail1[1].equals("")) {
							// System.out.println("Invoice Downloaded\n");
							File file = new File(
									"C:\\Users\\ssaravanan6384\\Desktop\\Project\\EmailExtractedAttachment\\"
											+ fromMail1[1]);
							Invoice invoice = new Invoice(fromMail1[0]);
							ReadPdf readPdf = new ReadPdf(file);
							String pdfText = readPdf.extractText();

							if (!pdfText.equals("")) {
								invoice.setInvoice(pdfText);
								b.invoiceIntoDatabase(invoice);
							} else {
								continue;
							}
						} else
							continue;
					else
						continue;
				} catch (Exception e) {
					e.printStackTrace();
				}
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

	}

}
