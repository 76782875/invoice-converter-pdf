package si.recek.invoiceConvert.model;

/**
 * Created by Slavko on 04.01.2016.
 */

public class InvoiceEntry {


    Long id;
    int amount;
    double value;
    double discount;

    Invoice invoice;

    String product;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    @Override
        public String toString() {
            return "Entry{" +
                    ", amount=" + amount +
                    ", value=" + value +
                    "PK: " + getId() +
                    '}';
        }
}
