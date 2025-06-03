package com.GlobalBankProject.GlobalBankApplication.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import org.springframework.stereotype.Service;
import com.GlobalBankProject.GlobalBankApplication.Model.Transaction;
import com.itextpdf.text.Document;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

@Service
public class PdfService {

	public byte[] generateTransactionHistoryPdf(List<Transaction> transactions, String accountNumber)
			throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try {
			Document document = new Document();
			PdfWriter.getInstance(document, out);
			document.open();
			Font fontTitle = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16);
			Paragraph title = new Paragraph("Transaction History - " + accountNumber, fontTitle);
			title.setAlignment(Element.ALIGN_CENTER);
			document.add(title);
			document.add(new Paragraph(" "));
			PdfPTable table = new PdfPTable(6);
			table.setWidthPercentage(100);
			table.setSpacingBefore(10f);
			// Headers
			table.addCell("S.No");
			table.addCell("Date");
			table.addCell("From/To");
			table.addCell("Type");
			table.addCell("Amount");
			table.addCell("Balance");
			int index = 1;
			for (Transaction t : transactions) {
				table.addCell(String.valueOf(index++));
				table.addCell(t.getTimestamp().toString());
				table.addCell(t.getFromAccount() + " / " + t.getToAccount());
				table.addCell(t.getTransactionType());
				table.addCell(String.valueOf(t.getAmount()));
				table.addCell(String.valueOf(t.getBalance()));
			}
			document.add(table);
			document.close();
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		return out.toByteArray();
	}
}
