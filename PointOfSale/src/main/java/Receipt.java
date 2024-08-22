import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;

import java.io.IOException;

public class Receipt implements Serializable {
    private static final long serialVersionUID = 1L;

    @JsonProperty("receiptNo")
    private int receiptNo;
    @JsonProperty("receiptDate")
    private String receiptDate;
    @JsonProperty("id")
    private int orderId;
    private double amount;

    public Receipt(){}
    public Receipt(int orderId, double amount) {
        this.receiptNo = orderId; // Assuming receiptNo is the same as orderId
        this.receiptDate = FileHandler.getCurrentDate();
        this.orderId = orderId;
        this.amount = amount;
    }

    public int getReceiptNo() {
        return receiptNo;
    }

    public String getReceiptDate() {
        return receiptDate;
    }

    public int getOrderId() {
        return orderId;
    }

    public double getAmount() {
        return amount;
    }

    @Override
    public String toString() {
        return String.format("%d;%s;%d;%.2f", receiptNo, receiptDate, orderId, amount);
    }


    public static List<Receipt> fromJsonArray(String jsonArray) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(jsonArray, new TypeReference<List<Receipt>>() {});
    }


    public static void makePayment() {
        Scanner scanner = new Scanner(System.in);

        // Get sales ID from the user
        System.out.print("Enter Sales ID: ");
        int salesId = scanner.nextInt();

        // Validate sales ID
        Sale sale = FileHandler.readSingleSaleFromFile(salesId);
        if (sale == null) {
            System.out.println("Sale with ID " + salesId + " not found.");
            return;
        }

        // Display sale information
        System.out.println("Sale ID: " + sale.getId());
        Customer customer = FileHandler.readSingleCustomerFromFile(sale.getCustomerId());
        if (customer != null) {
            System.out.println("Customer Name: " + customer.getName());
        }

//        List<Receipt> payments = FileHandler.readReceiptsFromFile();

        double totalSalesAmount = calculateTotalAmount(sale, FileHandler.readItemsFromFile());
        double amountPaid = calculateTotalAmountPaid(salesId);

        System.out.println("Total Sales Amount: " + totalSalesAmount);
        System.out.println("Amount Paid: " + amountPaid);
        System.out.println("Remaining Amount: " + (totalSalesAmount - amountPaid));

        // Get amount to be paid from the user
        System.out.print("Enter Amount to be Paid: ");
        double amountToBePaid = scanner.nextDouble();

        // Record the new payment as a receipt
        Receipt payment = new Receipt(salesId, amountToBePaid);
//        payments.add(payment);

        // Update customer balance
        if (customer != null) {
            customer.setAmountPayable(customer.getAmountPayable() - amountToBePaid);
        }

        // Save the updated payments to file
        FileHandler.writeReceiptsToFile(payment);

        System.out.println("Payment recorded successfully.");
    }

    static double calculateTotalAmountPaid(int salesId) {
        List<Receipt> receipts = FileHandler.readReceiptsFromFile();
        double totalAmountPaid = 0.0;
        for (Receipt receipt : receipts) {
            if (receipt.getOrderId() == salesId) {
                totalAmountPaid += receipt.getAmount();
            }
        }
        return totalAmountPaid;
    }


    static double calculateTotalAmount(Sale sale, List<Item> items) {
        double totalAmount = 0.0;
        for (SaleItem saleItem : sale.getSaleItems()) {
            Item item = Item.getItemById(saleItem.getItemId(), items);
            if (item != null) {
                totalAmount += item.getPrice() * saleItem.getQuantity();
            }
        }
        return totalAmount;
    }
}
