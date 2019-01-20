package si.recek.invoiceConvert.monthlystatement.model;

/**
 * Created by Slavko on 18. 11. 2015.
 */
public class Entry {

    private int entryNumber;
    private String entryId;
    private String description;
    private int amount;
    private double value;

    public int getEntryNumber() {
        return entryNumber;
    }

    public void setEntryNumber(int entryNumber) {
        this.entryNumber = entryNumber;
    }

    public String getEntryId() {
        return entryId;
    }

    public void setEntryId(String entryId) {
        this.entryId = entryId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public Invoice getInvoice() {
        return invoice;
    }

    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
    }

    private Invoice invoice;

    @Override
    public String toString() {
        return "Entry{" +
                "description='" + description + '\'' +
                ", amount=" + amount +
                ", value=" + value +
                '}';
    }
}
