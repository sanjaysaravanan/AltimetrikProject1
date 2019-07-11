package project.datalayer;

public interface SAASInterface {
	void createConnection();
	void listInvoiceDetails();
	boolean listUnprovedInvoice();
	boolean approveInvoice(String invoiceNo);
	void closeConnection();
}
