package project.buisnesslogic;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.encryption.InvalidPasswordException;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.PDFTextStripperByArea;

import java.io.File;
import java.io.IOException;

public class ReadPdf {
	File file = null;

	public ReadPdf(File doc) {
		this.file = doc;
	}

	public String extractText() throws IOException {
		String pdfFileText = "";
		PDDocument doc = PDDocument.load(file);
		if (!doc.isEncrypted()) {
			PDFTextStripperByArea stripper = new PDFTextStripperByArea();
			stripper.setSortByPosition(true);

			PDFTextStripper tStripper = new PDFTextStripper();

			pdfFileText = tStripper.getText(doc);
			// System.out.println(pdfFileInText);
		}
		doc.close();
		return pdfFileText;
	}
}
