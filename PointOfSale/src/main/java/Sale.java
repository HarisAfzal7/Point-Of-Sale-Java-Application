import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.File;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Sale implements Identifiable {

    private static final long serialVersionUID = 1L;

    private static int nextSaleId = 1;
    @JsonProperty("id")
    private int saleId;
    private int customerId;

    public void setSaleId(int saleId) {
        this.saleId = saleId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public void setSaleItems(List<SaleItem> saleItems) {
        this.saleItems = saleItems;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setSaleDate(LocalDate saleDate) {
        this.saleDate = saleDate;
    }
    @JsonProperty("date")
    private LocalDate saleDate;
    private List<SaleItem> saleItems;
    private String status;

    private int quantitySold;
    public Sale() {
    }

    public Sale(int customerId) {
        this.saleId = nextSaleId++;
        this.customerId = customerId;
        this.saleDate = LocalDate.parse(FileHandler.getCurrentDate());
        this.status = "Pending"; // You can have more meaningful status values
    }

    public List<SaleItem> getSaleItems() {
        return saleItems;
    }

    @Override
    public int getId() {
        return saleId;
    }

    public void setQuantitySold(int quantitySold) {
        this.quantitySold = quantitySold;
    }

    public LocalDate getSaleDate() {
        return saleDate;
    }

    public int getCustomerId() {
        return customerId;
    }


    public int getQuantitySold() {
        int totalQuantity = 0;
        if (saleItems != null) {
            for (SaleItem saleItem : saleItems) {
                totalQuantity += saleItem.getQuantity();
            }
        }
        setQuantitySold(totalQuantity);
        return totalQuantity;
    }

    public String getStatus() {
        return status;
    }

    public void updateStatus(String newStatus) {
        this.status = newStatus;
    }

    @Override
    public String toString() {
        return String.format("%d;%d;%s;%s", saleId, customerId, saleDate, status);
    }



    public static int generateNextSaleId() {
        return nextSaleId++;
    }

    public static void setNextSaleId(int nextSaleId) {
        Sale.nextSaleId = nextSaleId;
    }

    public void addSaleItem(SaleItem saleItem) {
        if (saleItems == null) {
            saleItems = new ArrayList<>();
        }
        saleItems.add(saleItem);
    }

    public static void makeNewSaleRecord() {
        List<Customer> customers = FileHandler.readCustomersFromFile();
        List<Item> items = FileHandler.readItemsFromFile();
//        List<Sale> sales = FileHandler.readSalesFromFile();

        Scanner scanner = new Scanner(System.in);

        // Create a new sale
        Sale sale = new Sale();
        sale.setSaleId(Sale.generateNextSaleId());
        sale.setSaleDate(LocalDate.now());
        sale.setStatus("Pending");

        // Get customer ID
        System.out.print("Enter Customer ID: ");
        int customerId = scanner.nextInt();
        sale.setCustomerId(customerId);

        Customer customer = FileHandler.readSingleCustomerFromFile(customerId);
        if (customer == null) {
            System.out.println("Customer with ID " + customerId + " not found.");
            return;
        }

        System.out.println("Sales Date: " + sale.getSaleDate());

        // Initialize sale items list
        sale.setSaleItems(new ArrayList<>());

        // Add items to the sale
        while (true) {
            System.out.print("Enter Item ID: ");
            int itemId = scanner.nextInt();

            Item item = getItemById(itemId, items);
            if (item == null) {
                System.out.println("Item with ID " + itemId + " not found.");
                continue;
            }

            System.out.println("Description: " + item.getDescription());
            System.out.println("Price: Rs. " + item.getPrice());

            System.out.print("Enter Quantity: ");
            int quantity = scanner.nextInt();

            double subTotal = item.getPrice() * quantity;
            System.out.println("Sub-Total: Rs. " + subTotal);

            SaleItem saleItem = new SaleItem();
            saleItem.setItemId(itemId);
            saleItem.setQuantity(quantity);

            sale.addSaleItem(saleItem);

            System.out.println("Press 1 to Enter New Item");
            System.out.println("Press 2 to End Sale");
            System.out.println("Press 3 to Remove an existing Item from the current sale");
            System.out.println("Press 4 to Cancel Sale");

            System.out.print("Choose from option 1 – 4: ");
            int choice = scanner.nextInt();

            if (choice == 2) {
                break;
            } else if (choice == 3) {
                // Remove an existing item from the current sale (implement this if needed)
                System.out.println("Option 3 is not implemented in this example.");
            } else if (choice == 4) {
                // Cancel the sale
                System.out.println("Sale canceled.");
                return;
            }
        }

        // Calculate total sale amount
        double totalAmount = calculateTotalAmount(sale, items);

        // Check if the total sale amount exceeds the customer's sales limit
        if (totalAmount > customer.getSalesLimit()) {
            System.out.println("Error: Total sale amount exceeds the customer's sales limit.");
            return;
        }

        // Update customer balance
        customer.setAmountPayable(customer.getAmountPayable() + totalAmount);


        // Add the sale to the list
//        sales.add(sale);

        // Display the sales summary
        displaySalesSummary(sale, items);

        // Save the sales information to the file
        FileHandler.modifyCustomerToFile(customer);
        FileHandler.writeSalesToFile(sale);

        System.out.println("Press any key to continue...");
        // Consume the newline character
        scanner.nextLine();
        // Wait for any key to be pressed (doesn't capture the actual key)
        scanner.nextLine();
    }

    static double calculateTotalAmount(Sale sale, List<Item> items) {
        double totalAmount = 0.0;
        for (SaleItem saleItem : sale.getSaleItems()) {
            Item item = getItemById(saleItem.getItemId(), items);
            if (item != null) {
                totalAmount += item.getPrice() * saleItem.getQuantity();
            }
        }
        return totalAmount;
    }

    private static void displaySalesSummary(Sale sale, List<Item> items) {
        System.out.println("Sales ID: " + sale.getId());
        System.out.println("Customer ID: " + sale.getCustomerId());
        System.out.println("Sales Date: " + sale.getSaleDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));

        Customer customer = FileHandler.readSingleCustomerFromFile(sale.getCustomerId());
        if (customer != null) {
            System.out.println("Name: " + customer.getName());
        }

        System.out.println("----------------------------------------------------------------------------------------------------------");
        System.out.printf("%-10s%-25s%-10s%-10s%n", "Item Id", "Description", "Quantity", "Amount");
        System.out.println("----------------------------------------------------------------------------------------------------------");

        double totalSaleAmount = 0.0;

        for (SaleItem saleItem : sale.getSaleItems()) {
            Item item = getItemById(saleItem.getItemId(), items);
            if (item != null) {
                double amount = item.getPrice() * saleItem.getQuantity();
                totalSaleAmount += amount;

                System.out.printf("%-10d%-25s%-10d%-10.2f%n", item.getId(), item.getDescription(), saleItem.getQuantity(), amount);
            }
        }

        System.out.println("----------------------------------------------------------------------------------------------------------");
        System.out.printf("%-55s%.2f%n", "Total Sales:", totalSaleAmount);
        System.out.println("----------------------------------------------------------------------------------------------------------");
    }
    private static Item getItemById(int itemId, List<Item> items) {
        for (Item item : items) {
            if (item.getId() == itemId) {
                return item;
            }
        }
        return null;
    }

}



class SaleItem implements Serializable {
    private static final long serialVersionUID = 1L;



    private int itemId;
    private int quantity;

    public int getItemId() {
        return itemId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    // Constructors, getters, setters...
}