package si.recek.invoiceConverter.synchronization;

import si.recek.invoiceConvert.model.Invoice;

import java.util.ArrayList;
import java.util.List;

public class InvoiceRepository {
    static InvoiceRepository instance = null;
    List<Invoice> invoices = new ArrayList<>();

    protected InvoiceRepository(){

    }

    public static InvoiceRepository getInstance() {
        if(instance == null){
            instance = new InvoiceRepository();
        }
        return instance;
    }

    public Invoice findByIdentifier(String invoiceIdentifier) {
        for (Invoice i :invoices) {
            if(i.getIdentifier().equals(invoiceIdentifier)){
                return i;
            }

        }
        return null;
    }

    public void save(Invoice invoice) {
        if(!invoices.contains(invoice)){
            invoices.add(invoice);
        }

    }

    public List<Invoice> getAllInvoices(){
        return invoices;
    }
}
