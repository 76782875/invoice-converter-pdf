package si.recek.invoiceConverter.synchronization;


import si.recek.invoiceConvert.model.Invoice;
import si.recek.invoiceConvert.model.InvoiceEntry;


import java.util.List;

/**
 * Created by Slavko on 19.12.2016.
 */

public class InvoiceSynchronizer {

    
    InvoiceRepository invoiceRepository = InvoiceRepository.getInstance();


    public void synchronizeEntries(List<InputEntry> entries){

        processEntries(entries);
        updateInvoiceBalances();
    }

    public void processEntries(List<InputEntry> entries) {
       for(InputEntry entry : entries){
           processEntry(entry);
       }
    }

    private void processEntry(InputEntry entry) {
        String invoiceIdentifier = entry.getInvoiceID();
        Invoice invoice = invoiceRepository.findByIdentifier(invoiceIdentifier);
        if (invoice == null) {
            invoice = new Invoice();
            invoice.setIdentifier(invoiceIdentifier);
            invoice.setIssuingDate(entry.getDate());
            invoice.setIssuingTime(entry.getTime());
        }
        if(!invoice.getEntries().stream().anyMatch(invoiceEntry -> invoiceEntry.getProduct().equals(entry.getProductNotation()))){
            InvoiceEntry dbEntry = new InvoiceEntry();

            dbEntry.setProduct(entry.getProductNotation());
            dbEntry.setAmount(entry.getAmount());
            dbEntry.setValue(entry.getTotal());
            invoice.addInvoiceEntry(dbEntry);
        }
        invoiceRepository.save(invoice);
    }


    private void updateInvoiceBalances() {
        invoiceRepository.getAllInvoices().stream().forEach(invoice ->
            {invoice.updateValue();
            invoiceRepository.save(invoice);
            });
    }
}
