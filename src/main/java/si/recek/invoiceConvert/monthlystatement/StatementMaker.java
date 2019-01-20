
package si.recek.invoiceConvert.monthlystatement;



import si.recek.invoiceConvert.Application;
import si.recek.invoiceConvert.model.Invoice;
import si.recek.invoiceConvert.model.Invoice;
import si.recek.invoiceConvert.monthlystatement.pdf.PdfProducer;

import java.io.*;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static si.recek.invoiceConvert.Application.workingDir;


/**
 * Created by Slavko on 19. 11. 2015.
 */

public class StatementMaker {

    public void createStatement(List<Invoice> invoices) {
        LocalDate.now().minusMonths(1);
        LocalDate firstInMonth = getPeriodBegin(invoices);
        LocalDate lastInMonth = getPeriodEnd(invoices);
        ByteArrayOutputStream bos = createPDF(firstInMonth, lastInMonth, invoices);
        try(OutputStream outputStream = new FileOutputStream(String.valueOf(Paths.get(workingDir).resolve(generateOutputFilePath(firstInMonth.getYear(), firstInMonth.getMonthValue()))))) {
            bos.writeTo(outputStream);
        }catch (IOException e){
            e.printStackTrace();
        }

    }

    private LocalDate getPeriodBegin(List<Invoice> invoices) {
        Invoice firstInvoice = invoices.get(0);
        return firstInvoice.getIssuingDate().with(java.time.temporal.TemporalAdjusters.firstDayOfMonth());
    }

    private LocalDate getPeriodEnd(List<Invoice> invoices) {
        Invoice lastInvoice = invoices.get(invoices.size()-1);
        return lastInvoice.getIssuingDate().with(java.time.temporal.TemporalAdjusters.lastDayOfMonth());
    }

    public String generateOutputFilePath(int year, int month) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM");
        String monthName = LocalDate.of(year, month, 1).format(formatter);
        monthName = monthName.substring(0, 1).toUpperCase() + monthName.substring(1);
        return String.format("POS_Izpis_Salon_Urška_%s_%d.pdf", monthName, year);

    }


    private ByteArrayOutputStream createPDF(LocalDate beginDate, LocalDate endDate, List<Invoice> invoicesInMonth) {
        PdfProducer pdfProducer = new PdfProducer(beginDate, endDate, invoicesInMonth);
        return pdfProducer.producePdf();
    }

}
