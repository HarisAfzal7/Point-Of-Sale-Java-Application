
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import java.time.LocalDate;

public class ViewReport {
    private void stockInHand(){
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter the starting Item ID: ");
        int startItemId = scanner.nextInt();

        System.out.print("Enter the ending Item ID: ");
        int endItemId = scanner.nextInt();

        System.out.println("Date: " + getCurrentDateTime()); // Implement getCurrentDateTime() based on your requirement


        System.out.println("--------------------------------------------------------------------------------------------------------------");
        System.out.printf("%-10s%-25s%-15s%-10s%n", "Item ID", "Description", "Price", "Quantity in Hand");
        System.out.println("--------------------------------------------------------------------------------------------------------------");

        List<Item> items = FileHandler.readItemsFromFile();

        for (Item item : items) {
            if (item.getId() >= startItemId && item.getId() <= endItemId) {
                System.out.println(item);
            }
        }

        System.out.println("--------------------------------------------------------------------------------------------------------------");
    }

    private static String getCurrentDateTime() {
        LocalDateTime currentDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm a");
        return currentDateTime.format(formatter);
    }


    private void customerBalance(){
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter Item ID of the Customer: ");
        int customerId = scanner.nextInt();

        Customer customer = FileHandler.readSingleCustomerFromFile(customerId);
        if(customer!=null){
            displayCustomers(customer);
        }else {
            System.out.printf("Customer with ID "+customerId+" Not Found");
        }

    }

    private static void displayCustomers(Customer customer) {
        System.out.println("----------------------------------------------------------------------------------------------------------------------------");
        System.out.printf("%-15s%-20s%-30s%-15s%-30s%-15s%-15s%n", "Customer ID", "Name", "Email", "Phone", "Sales Limit", "Payable Amount", " ");
        System.out.println("----------------------------------------------------------------------------------------------------------------------------");

        System.out.printf("%-15d%-20s%-30s%-15s%-30.2f%-15.2f%n", customer.getId(), customer.getName(), customer.getEmail(), customer.getPhone(), customer.getSalesLimit(), customer.getAmountPayable());

        System.out.println("----------------------------------------------------------------------------------------------------------------------------");
    }
    private void salesReport(){
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter the starting date (MM/dd/yyyy): ");
        String startDateInput = scanner.nextLine().trim();

        System.out.print("Enter the ending date (MM/dd/yyyy): ");
        String endDateInput = scanner.nextLine().trim();

        LocalDate startDate = LocalDate.parse(startDateInput, DateTimeFormatter.ofPattern("MM/dd/yyyy"));
        LocalDate endDate = LocalDate.parse(endDateInput, DateTimeFormatter.ofPattern("MM/dd/yyyy"));

        System.out.println("Sales Report");
        System.out.println("From: " + startDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) +
                "       To: " + endDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        System.out.println("-----------------------------------------------------------------------------------------------------------------");
        System.out.printf("%-10s%-25s%-20s%-15s%n", "Item Id", "Description", "Quantity Sold", "Amount");
        System.out.println("-----------------------------------------------------------------------------------------------------------------");

        double totalSaleAmount = 0.0;

        List<Sale> sales = FileHandler.readSalesFromFile();
        List<Item> items = FileHandler.readItemsFromFile();

        for (Sale sale : sales) {
            // Check if the sale date is within the specified period
            // Assuming Sale class has a LocalDate attribute named 'saleDate'
            LocalDate saleDate = sale.getSaleDate();
            if (saleDate != null && (saleDate.isEqual(startDate) || saleDate.isAfter(startDate)) && saleDate.isBefore(endDate.plusDays(1))) {
                Item item = Item.getItemById(sale.getId(), items);
                double amount = sale.getQuantitySold() * item.getPrice();
                totalSaleAmount += amount;

                System.out.printf("%-10d%-25s%-20d%-15.2f%n", sale.getId(), item.getDescription(), sale.getQuantitySold(), amount);
            }
        }

        System.out.println("-----------------------------------------------------------------------------------------------------------------");
        System.out.printf("%-85s%.2f%n", "Total Sale:", totalSaleAmount);
        System.out.println("-----------------------------------------------------------------------------------------------------------------");
    }


    private void outstandingOrders(){
        Scanner scanner = new Scanner(System.in);

        // Get the start date from the user
        System.out.print("Enter start date (yyyy-MM-dd): ");
        LocalDate startDate = LocalDate.parse(scanner.nextLine());

        // Get the end date from the user
        System.out.print("Enter end date (yyyy-MM-dd): ");
        LocalDate endDate = LocalDate.parse(scanner.nextLine());


        // Read sales data from the file
        List<Sale> sales = FileHandler.readSalesFromFile();


        // Display the outstanding sales report
        displayOutstandingSalesReport(startDate, endDate, sales);
    }

    private static void displayOutstandingSalesReport(LocalDate startDate, LocalDate endDate, List<Sale> sales) {
        System.out.println("Outstanding Orders");
        System.out.printf("From: %s       To: %s%n", startDate, endDate);
        System.out.println("-----------------------------------------------------------------------------------------------------------------");
        System.out.printf("%-10s%-20s%-20s%-20s%n", "Sales ID", "Customer Name", "Total Amount", "Remaining Amount");
        System.out.println("-----------------------------------------------------------------------------------------------------------------");

        double totalRemainingAmount = 0.0;

        for (Sale sale : sales) {
            LocalDate saleDate = sale.getSaleDate();
            if ((saleDate.isEqual(startDate) || saleDate.isAfter(startDate)) &&
                    (saleDate.isEqual(endDate) || saleDate.isBefore(endDate))) {

                Customer customer = FileHandler.readSingleCustomerFromFile(sale.getCustomerId());
                double totalAmount = Receipt.calculateTotalAmount(sale, FileHandler.readItemsFromFile());
                double remainingAmount = totalAmount - Receipt.calculateTotalAmountPaid(sale.getId());

                System.out.printf("%-10d%-20s%-20.2f%-20.2f%n", sale.getId(), customer.getName(), totalAmount, remainingAmount);

                totalRemainingAmount += remainingAmount;
            }
        }

        System.out.println("-----------------------------------------------------------------------------------------------------------------");
        System.out.printf("%-60s%.2f%n", "Total Remaining Amount:", totalRemainingAmount);
        System.out.println("-----------------------------------------------------------------------------------------------------------------");
    }


    public void manageUserMenuChoice(int choice){
        switch (choice) {
            case 1:
                stockInHand();
                break;
            case 2:
                outstandingOrders();
                break;
            case 3:
                customerBalance();
                break;
            case 4:
                salesReport();
                break;
            case 5:
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
        }
    }
}
