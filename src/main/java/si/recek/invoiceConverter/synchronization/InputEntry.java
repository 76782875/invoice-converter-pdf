package si.recek.invoiceConverter.synchronization;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * Created by Slavko on 18.12.2016.
 */
public class InputEntry {

    LocalDate date;
    String invoiceID;
    String placeID;
    String deviceID;
    LocalTime time;
    String productNotation;
    int amount;
    double total;

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getInvoiceID() {
        return invoiceID;
    }

    public void setInvoiceID(String invoiceID) {
        this.invoiceID = invoiceID;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public String getProductNotation() {
        return productNotation;
    }

    public void setProductNotation(String productNotation) {
        this.productNotation = productNotation;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
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

    public void setDateFromString(String s) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMdd");
        LocalDate date = LocalDate.parse(s, dtf);
        setDate(date);
    }

    public void setTimeFromString(String s) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
        LocalTime time = LocalTime.parse(s, dtf);
        setTime(time);
    }
}
