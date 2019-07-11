package project.buisnesslogic;

public class Invoice {
	private long invoiceNo = 0;
	private String invoiceDate = "";
	private long customerPo = 0;
	private String address = "";
	private double finalAmount = 0.0;
	private boolean approvedStatus = false;
	private String mail = "";

	public Invoice(String mail) {
		this.mail = mail;
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public boolean isApprovedStatus() {
		return approvedStatus;
	}

	public void setApprovedStatus(boolean approvedStatus) {
		this.approvedStatus = approvedStatus;
	}

	public long getInvoiceNo() {
		return invoiceNo;
	}

	public void setInvoiceNo(long invoiceNo) {
		this.invoiceNo = invoiceNo;
	}

	public String getInvoiceDate() {
		return invoiceDate;
	}

	public void setInvoiceDate(String invoiceDate) {
		this.invoiceDate = invoiceDate;
	}

	public long getCustomerPo() {
		return customerPo;
	}

	public void setCustomerPo(long customerPo) {
		this.customerPo = customerPo;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public double getFinalAmount() {
		return finalAmount;
	}

	public void setFinalAmount(double finalAmount) {
		this.finalAmount = finalAmount;
	}

	public void setInvoice(String pdfFileInText) {
		// System.out.println(pdfFileInText);
		String lines[] = pdfFileInText.split("\\r?\\n");
		String str = "";
		for (int i = 0; i < lines.length; i++) {
			str = lines[i];
			if (str.equals("Invoice No")) {
				this.setInvoiceNo(Long.parseLong(lines[i + 1]));
			}
			if (str.equals("Invoice Date")) {
				// lines[i+1] = lines[i+1].replace("/", "-");
				lines[i + 1] = "20" + lines[i + 1].substring(6, 8) + "-" + lines[i + 1].substring(3, 5) + "-"
						+ lines[i + 1].substring(0, 2);
				this.setInvoiceDate(lines[i + 1]);
			}
			if (str.equals("Customer P.O.")) {
				String str1 = lines[i + 1];
				if (str1.startsWith("PO#"))
					this.setCustomerPo(Long.parseLong(str1.substring(3)));
				else
					this.setCustomerPo(Long.parseLong(str1));
			}
			if (str.equals("Ship To")) {
				String address = "";
				for (int j = 1; j <= 4; j++)
					address += lines[i + j];
				this.setAddress(address);
			}
			if (str.equals("Total Invoice")) {
				String temp;
				if (lines[i - 1].equals("Sales Tax"))
					temp = lines[i + 4];
				else
					temp = lines[i + 3];
				String temp1 = temp.replace("$", "");
				String temp2 = temp1.replaceAll(",", "");
				this.setFinalAmount(Double.parseDouble(temp2));
			}
		}
	}

	void displayInvoice() {

	}
}
