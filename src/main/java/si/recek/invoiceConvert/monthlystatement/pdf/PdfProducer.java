package si.recek.invoiceConvert.monthlystatement.pdf;


import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import si.recek.invoiceConvert.model.Invoice;
import si.recek.invoiceConvert.model.InvoiceEntry;


import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;


public class PdfProducer {
    private LocalDate beginDate;
    private LocalDate endDate;
    private List<Invoice> invoicesInMonth;
    private Document document;

    public PdfProducer(LocalDate beginDate, LocalDate endDate, List<Invoice> invoicesInMonth) {
        this.beginDate = beginDate;
        this.endDate = endDate;
        this.invoicesInMonth = invoicesInMonth;
    }

    public ByteArrayOutputStream producePdf() {
        document = new Document();
        ByteArrayOutputStream bOut = new ByteArrayOutputStream();
        try {
            PdfWriter.getInstance(document, bOut);

            document.open();
            printCompanyTitle();
            printFileTitle();
            printByDocumentsTableSection();
            printArticleSummeryTableSection();
            printTypeSummeryTableSection();
            printTaxSummeryTableSection();
            printPaymentTypeTableSection();
            printDateAndPlaceSection();
            document.close();

        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return bOut;

    }

    public String generateOutputFilePath(int year, int month) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM");
        String monthName = LocalDate.of(year, month, 1).format(formatter);
        monthName = monthName.substring(0, 1).toUpperCase() + monthName.substring(1);
        return String.format("POS_Izpis_Salon_Urška_%s_%d.pdf", monthName, year);

    }

    private ArrayList<String[]> formatDocumentsContent() {
        ArrayList<String []> rows = new ArrayList<>();
        //listDevs.sort((Developer o1, Developer o2)->o1.getAge()-o2.getAge());
        invoicesInMonth.sort((o1, o2) -> Integer.valueOf(o1.getIdentifier()) - Integer.valueOf(o2.getIdentifier()));
        ;
        for(Invoice invoice : invoicesInMonth) {
            String [] values = new String[3];
            values[0] = invoice.getIssuingDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
            if(invoice.getIdentifier().contains("P1-B1")){
                values[1] = String.format("Racun %s",invoice.getIdentifier());
            }else{
                values[1] = String.format("Racun %s/P1/1",invoice.getIdentifier());
            }
            values[2] = String.format("%.2f", invoice.getValue());
            rows.add(values);
        }
        return rows;
    }

    private ArrayList<String[]> formatArticlesContent() {
        ArrayList<String[]> tableRows = new ArrayList<>();
        ArrayList<InvoiceEntry> positiveEntries = new ArrayList<>();
        ArrayList<InvoiceEntry> negativeEntries = new ArrayList<>();
        for(Invoice i : invoicesInMonth){
            for(InvoiceEntry e : i.getEntries()) {
                if (e.getAmount() < 0) {
                    negativeEntries.add(e);
                }else {
                    positiveEntries.add(e);
                }
            }
        }
        TreeMap<String, ArrayList<InvoiceEntry>> positiveEntriesById = new TreeMap<>();
        for (InvoiceEntry e : positiveEntries) {
            if(!positiveEntriesById.containsKey(e.getProduct())) {
                positiveEntriesById.put(e.getProduct(), new ArrayList<>());
            }
            positiveEntriesById.get(e.getProduct()).add(e);
        }
        TreeMap<String, ArrayList<InvoiceEntry>>  negativeEntriesById = new TreeMap<>();
        for (InvoiceEntry e : negativeEntries) {
            if(!negativeEntriesById.containsKey(e.getProduct())) {
                negativeEntriesById.put(e.getProduct(), new ArrayList<>());
            }
            negativeEntriesById.get(e.getProduct()).add(e);
        }
        for(String entryId : positiveEntriesById.keySet()){
            ArrayList<InvoiceEntry> entries = positiveEntriesById.get(entryId);
            int count = 0;
            double value = 0;
            double average;
            for(InvoiceEntry e : entries) {
                count += e.getAmount();
                value += e.getValue() * e.getAmount();
            }
            average = value / count;
            String [] row = new String[4];
            row[0] = entries.get(0).getProduct();
            row[1] = String.format("%d", count);
            row[2] = String.format("%.2f", average);
            row[3] = String.format("%.2f", value);
            tableRows.add(row);
        }

        for(String entryId : negativeEntriesById.keySet()){
            ArrayList<InvoiceEntry> entries = negativeEntriesById.get(entryId);
            int count = 0;
            double value = 0;
            double average;
            for(InvoiceEntry e : entries) {
                count += e.getAmount();
                value += e.getValue();
            }
            average = value / count;
            String [] row = new String[4];
            row[0] = entries.get(0).getProduct();
            row[1] = String.format("%d", count);
            row[2] = String.format("%.", average);
            row[3] = String.format("%.2f",  value);
            tableRows.add(row);
        }

        return tableRows;
    }

    private void printCompanyTitle() throws DocumentException {
        document.add(new Paragraph("Frizerski salon Urška"));
        document.add(new Paragraph("Urška Recek s.p"));
        document.add(new Paragraph("Na terasi 7a"));
        document.add(new Paragraph("2000, Maribor"));
    }

    private void printFileTitle() throws DocumentException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        Paragraph title = new Paragraph(50);
        title.getFont().setSize(15);
        Font boldFont = new Font(title.getFont().getBaseFont()) ;
        boldFont.setStyle(Font.BOLD);
        title.add(new Phrase("Promet od "));
        title.add(new Phrase(beginDate.format(formatter), boldFont));
        title.add(new Phrase(" do "));
        title.add(new Phrase(endDate.format(formatter), boldFont));
        document.add(title);
    }

    private void printByDocumentsTableSection() throws DocumentException {
        Paragraph tableTitle = new Paragraph();
        tableTitle.setSpacingBefore(30);
        tableTitle.setSpacingAfter(5);
        tableTitle.add(new Phrase("Dokumenti"));
        document.add(tableTitle);

        PdfPTable table = new PdfPTable(3);
        table.setWidthPercentage(100);
        String [] headerValues = {"Datum", "Dokumenti", "Bruto"};
        printBoldTableRow(table, headerValues);
        ArrayList<String[]> documentsRows = formatDocumentsContent();
        printTableContent(table,documentsRows);
        String [] footerValues = {"Skupaj", "", getDocumentsTotal()};
        printBoldTableRow(table, footerValues);
        document.add(table);

    }

    private void printArticleSummeryTableSection() throws DocumentException {
        Paragraph tableTitle = new Paragraph();
        tableTitle.setSpacingBefore(30);
        tableTitle.setSpacingAfter(5);
        tableTitle.add(new Phrase("Rekapitulacija artikliov"));
        document.add(tableTitle);

        PdfPTable table = new PdfPTable(4);
        table.setWidthPercentage(100);
        float[] columnWidths = {3f, 1f, 1f, 1f};
        table.setWidths(columnWidths);
        String [] headerValues = {"Artikel", "Kolicina", "Cena", "Vrednost"};
        printBoldTableRow(table, headerValues);
        ArrayList<String[]> articleRows = formatArticlesContent();
        printTableContent(table, articleRows);
        String [] footerValues = {"Skupaj",  "", "", getDocumentsTotal()};
        printBoldTableRow(table, footerValues);
        document.add(table);

    }

    private void printTypeSummeryTableSection() throws DocumentException{
        Paragraph tableTitle = new Paragraph();
        tableTitle.setSpacingBefore(30);
        tableTitle.setSpacingAfter(5);
        tableTitle.add(new Phrase("Rekapitulacija po vrsti"));
        document.add(tableTitle);

        PdfPTable table = new PdfPTable(5);
        table.setWidthPercentage(100);
        String [] headerValues = {"Šifra", "Artikel", "Osnova", "DDV", "Vrednost"};
        printBoldTableRow(table, headerValues);
        ArrayList<String[]> rows = new ArrayList<>();
        String [] cells = {"S", "Storitev", getDocumentsTotal(), "0,00", getDocumentsTotal()};
        rows.add(cells);
        printTableContent(table, rows);
        String [] footerValues = {"Skupaj", "", "", "", getDocumentsTotal()};
        printBoldTableRow(table, footerValues);
        document.add(table);
    }

    private void printTaxSummeryTableSection() throws DocumentException{
        Paragraph tableTitle = new Paragraph();
        tableTitle.setSpacingBefore(30);
        tableTitle.setSpacingAfter(5);
        tableTitle.add(new Phrase("Rekapitulacija po davkih"));
        document.add(tableTitle);

        PdfPTable table = new PdfPTable(4);
        table.setWidthPercentage(100);
        String [] headerValues = {"DDV %", "Artikel", "Osnova", "DDV"};
        printBoldTableRow(table, headerValues);
        ArrayList<String[]> rows = new ArrayList<>();
        String [] cells = {"S", "Storitev", getDocumentsTotal(), "0,00"};
        rows.add(cells);
        printTableContent(table, rows);
        String [] footerValues = {"Skupaj", "", getDocumentsTotal(), ""};
        printBoldTableRow(table, footerValues);
        document.add(table);
    }

    private void printPaymentTypeTableSection() throws DocumentException{
        Paragraph tableTitle = new Paragraph();
        tableTitle.setSpacingBefore(30);
        tableTitle.setSpacingAfter(5);
        tableTitle.add(new Phrase("Rekapitulacija po nacinu placila"));
        document.add(tableTitle);

        PdfPTable table = new PdfPTable(3);
        table.setWidthPercentage(100);
        String [] headerValues = {"Šifra", "Nacin placila", "Vrednost"};
        printBoldTableRow(table, headerValues);
        ArrayList<String[]> rows = new ArrayList<>();
        String [] cells = {"G", "Gotovina", getDocumentsTotal()};
        rows.add(cells);
        printTableContent(table, rows);
        String [] footerValues = {"Skupaj",  "", getDocumentsTotal()};
        printBoldTableRow(table, footerValues);
        document.add(table);
    }

    private void printDateAndPlaceSection() throws DocumentException{
        Paragraph p = new Paragraph();
        p.setSpacingBefore(30);
        p.add(new Phrase("V Mariboru, "));
        p.add(new Phrase(LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))));
        document.add(p);
    }

    private String getDocumentsTotal() {
        double total = invoicesInMonth.stream()
                        .map(Invoice::getValue)
                        .mapToDouble(Double::doubleValue).sum();
        return String.format("%.2f", total);
    }

    private void printBoldTableRow(PdfPTable table, String[] headerValues) {
        for(String value : headerValues) {
            Paragraph headerCell = new Paragraph();
            headerCell.getFont().setStyle(Font.BOLD);
            headerCell.add(new Phrase(value));
            PdfPCell cell = new PdfPCell(headerCell);
            cell.setIndent(5);
            table.addCell(cell);
        }
    }

    private void printTableContent(PdfPTable table, ArrayList<String[]> documentsRows) {
        for (String [] cells : documentsRows) {
            for (String cellValue : cells) {
                Paragraph pCell = new Paragraph();
                pCell.getFont().setSize(10);
                pCell.add(new Phrase(cellValue));
                PdfPCell cell = new PdfPCell(pCell);
                cell.setIndent(5);
                cell.setBorderWidthBottom(0);
                table.addCell(cell);
            }
        }
    }
}
