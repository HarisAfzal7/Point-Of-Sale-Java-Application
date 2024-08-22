import java.io.Serializable;

public class SaleLineItem implements Serializable {
    private static final long serialVersionUID = 1L;

    private int lineNo;
    private int orderId;
    private int itemId;
    private int quantity;
    private double amount;

    public SaleLineItem(int orderId, int itemId, int quantity, double amount) {
        this.lineNo = orderId; // Assuming lineNo is the same as orderId
        this.orderId = orderId;
        this.itemId = itemId;
        this.quantity = quantity;
        this.amount = amount;
    }

    public int getLineNo() {
        return lineNo;
    }

    public int getOrderId() {
        return orderId;
    }

    public int getItemId() {
        return itemId;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getAmount() {
        return amount;
    }

    @Override
    public String toString() {
        return String.format("%d;%d;%d;%d;%.2f", lineNo, orderId, itemId, quantity, amount);
    }
}
