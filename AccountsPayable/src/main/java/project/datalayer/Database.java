package project.datalayer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import project.buisnesslogic.AcknowledgementForInvoice;
import project.buisnesslogic.Invoice;

public class Database implements SAASInterface {
	Connection conn = null;
	Statement stm = null;
	ResultSet result = null;// it is used to navigate throuh the each record

	public void createConnection() {
		try {
			// loading the driver class obj
			// Class.forName("com.mysql.cj.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/invoicedetails", "root", "system");
			stm = conn.createStatement();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void invoiceIntoDatabase(Invoice invoice) {
		// execute only one query
		try {
			stm.execute("insert into invoice(invoiceno, invoicedate, customerpo, address, finalamount, email) "
					+ "values(" + invoice.getInvoiceNo() + "," + "'" + invoice.getInvoiceDate() + "'" + ","
					+ invoice.getCustomerPo() + "," + "'" + invoice.getAddress() + "'" + "," + invoice.getFinalAmount()
					+ "," + "'" + invoice.getMail() + "'" + ")");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void listInvoiceDetails() {
		try {
			result = stm.executeQuery("select * from invoice");
			if (result.next()) {
				System.out.println(
						"     Invoice No|   Invoice Date|     Customer P.O.|  Ammount|    ApprovedStatus     ");
				System.out.print(String.format("%1$14s", result.getLong(1)));
				System.out.print(String.format("%1$16s", result.getDate(2)));
				System.out.print(String.format("%1$18s", result.getLong(3)));
				System.out.print(String.format("%1$11s", result.getDouble(5)));
				System.out.print(String.format("%1$12s", ((result.getString(6).equals("1")) ? "YES" : "NO")));
				System.out.print("\n");
				while (result.next()) {
					System.out.print(String.format("%1$14s", result.getLong(1)));
					System.out.print(String.format("%1$16s", result.getDate(2)));
					System.out.print(String.format("%1$18s", result.getLong(3)));
					// System.out.println(String.format("%1$15s",
					// result.getString(4)));
					System.out.print(String.format("%1$11s", result.getDouble(5)));
					System.out.print(String.format("%1$12s", ((result.getString(6).equals("1")) ? "YES" : "NO")));
					System.out.print("\n");
				}
			} else {
				System.out.println("No Details Found...");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean listUnprovedInvoice() {
		try {
			ResultSet result1 = null;
			result = stm.executeQuery("select * from invoice where approvestatus=0");
			if (result.next()) {
				System.out.println("List of Invoices yet to be Approved");
				System.out.println(
						"     Invoice No|   Invoice Date|     Customer P.O.|  Ammount|    ApprovedStatus     ");
				System.out.print(String.format("%1$14s", result.getLong(1)));
				System.out.print(String.format("%1$16s", result.getDate(2)));
				System.out.print(String.format("%1$18s", result.getLong(3)));
				System.out.print(String.format("%1$11s", result.getDouble(5)));
				System.out.print(String.format("%1$12s", ((result.getString(6).equals("1")) ? "YES" : "NO")));
				System.out.print("\n");
				while (result.next()) {
					System.out.print(String.format("%1$14s", result.getLong(1)));
					System.out.print(String.format("%1$16s", result.getDate(2)));
					System.out.print(String.format("%1$18s", result.getLong(3)));

					System.out.print(String.format("%1$11s", result.getDouble(5)));
					System.out.print(String.format("%1$12s", ((result.getString(6).equals("1")) ? "YES" : "NO")));
					System.out.print("\n");
				}
			} else {
				System.out.println("No Invoice is Available For Approval.");
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	public boolean approveInvoice(String invoiceNo) {
		try {
			int count = stm.executeUpdate("update invoice set approvestatus=1 where invoiceno=" + invoiceNo);
			if (count == 0)
				return false;
			result = stm.executeQuery("select * from invoice where invoiceno=" + invoiceNo);
			result.next();
			String emaildummy = "Your Invoice No: " + result.getLong(1) + " for $" + result.getDouble(5)
					+ " has been Approved. \nYou can expect the payment soon.";
			AcknowledgementForInvoice ack = new AcknowledgementForInvoice(result.getString(7));
			try {
				ack.sendMail(emaildummy);
				System.out.println("Successfully Approved and mail sent.");
			} catch (AddressException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (MessagingException e) {
				e.printStackTrace();
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}

	public void approveInvoice(Invoice invoice) {
		try {
			invoice.setApprovedStatus(true);
			stm.executeUpdate("update invoice set approvestatus=1 where invoiceno=" + invoice.getInvoiceNo());
			// ack.setMailServerProperties();
			String emaildummy = "Your Invoice No: " + invoice.getInvoiceNo() + " for $" + invoice.getFinalAmount()
					+ " has been Approved. \nYou can expect the payment soon.";
			if (invoice.isApprovedStatus()) {
				AcknowledgementForInvoice ack = new AcknowledgementForInvoice(invoice.getMail());
				ack.sendMail(emaildummy);
				System.out.println("Approved and Email sent Successfully.");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void closeConnection() {
		try {
			if (result != null)
				result.close();
			if (stm != null)
				stm.close();
			if (conn != null)
				conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
