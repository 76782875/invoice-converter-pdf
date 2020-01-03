package si.recek.invoiceConvert.model;

import si.recek.invoiceConvert.model.InvoiceEntry;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by Slavko on 04.01.2016.
 */

public class Invoice {

    Long id;
    int invoiceNumber;
    String identifier;
    String placeID;
    String deviceID;
    LocalDate issuingDate;
    LocalTime issuingTime;
    double value;
    double discount;

    List<InvoiceEntry> entries;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Invoice(){
        entries = new ArrayList<InvoiceEntry>();
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

    public List<InvoiceEntry> getEntries() {
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

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public String getPlaceID() {
        return placeID;
    }

    public void setPlaceID(String placeID) {
        this.placeID = placeID;
    }

    public String getDeviceID() {
        return deviceID;
    }

    public void setDeviceID(String deviceID) {
        this.deviceID = deviceID;
    }

    public void addInvoiceEntry(InvoiceEntry e) {
        entries.add(e);
        e.setInvoice(this);
    }


    @Override
    public String toString() {
        return "si.recek.invoiceConvert.model.Invoice{" +
                "invoiceID= " + identifier +
                "value= " +  value +
                '}';
    }

    public void updateValue() {
        double balance = 0.0;
        for(InvoiceEntry e : entries){
            balance += e.getValue();
        }
        setValue(balance);
    }

    public LocalDateTime getIssuingDateTime() {
        return getIssuingDate().atTime(getIssuingTime());
    }
}
