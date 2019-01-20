package si.recek.invoiceConvert.monthlystatement.model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

/**
 * Created by Slavko on 18. 11. 2015.
 */
public class Invoice {

    private int invoiceNumber;
    private String identifier;
    private LocalDate issuingDate;
    private LocalTime issuingTime;
    private double value;
    private ArrayList<Entry> entries;


    public Invoice(){
        entries = new ArrayList<Entry>();
    }

    public int getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(int invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public LocalDate getIssuingDate() {
        return issuingDate;
    }

    public void setIssuingDate(LocalDate issuingDate) {
        this.issuingDate = issuingDate;
    }

    public ArrayList<Entry> getEntries() {
        return entries;
    }

    public LocalTime getIssuingTime() {
        return issuingTime;
    }

    public void setIssuingTime(LocalTime issuingTime) {
        this.issuingTime = issuingTime;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public void addEntry(Entry e) {
        entries.add(e);
        e.setInvoice(this);
    }

    @Override
    public String toString() {
        return "Invoice{" +
                "invoiceID= " + identifier +
                "value= " +  value +
                '}';
    }

    public void updateValue() {
        double balance = 0.0;
        for(Entry e : entries){
            balance += e.getValue();
        }
        setValue(balance);
    }
}
