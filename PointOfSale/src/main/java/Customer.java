import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.lang.module.FindException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
public class Customer implements Identifiable {
    private static final long serialVersionUID = 1L;

    private static int nextCustomerId = 1;
    @JsonProperty("id")
    private int customerId;
    private String name;
    private String address;
    private String phone;
    private String email;
    private double amountPayable;
    private double salesLimit;

    public Customer() {
    }

    public Customer(String name, String address, String phone, String email, double amountPayable, double salesLimit) {
        this.customerId = nextCustomerId++;
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.email = email;
        this.amountPayable = amountPayable;
        this.salesLimit = salesLimit;
    }

    @Override
    public int getId() {
        return customerId;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public double getAmountPayable() {
        return amountPayable;
    }

    public double getSalesLimit() {
        return salesLimit;
    }
    public static int generateNextCustomerId() {
        return nextCustomerId + 1;
    }

    public static void setNextCustomerId(int nextCustomerId) {
        Customer.nextCustomerId = nextCustomerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setAmountPayable(double amountPayable) {
        this.amountPayable = amountPayable;
    }

    public void setSalesLimit(double salesLimit) {
        this.salesLimit = salesLimit;
    }

    private Customer getNewCustomer(){

        Scanner scanner = new Scanner(System.in);

        int customerId = Customer.generateNextCustomerId();
        System.out.println("Generated Customer ID: " + customerId);

        System.out.print("Enter Customer Name: ");
        String name = scanner.nextLine().trim();

        System.out.print("Enter Customer Address: ");
        String address = scanner.nextLine().trim();

        System.out.print("Enter Customer Phone: ");
        String phone = scanner.nextLine().trim();

        System.out.print("Enter Customer Email: ");
        String email = scanner.nextLine().trim();

        System.out.print("Enter Sales Limit: ");
        double salesLimit = scanner.nextDouble();
        // Confirm save
        System.out.print("Do you want to save this customer information? (Y/N): ");
        String confirmation = scanner.next().trim().toLowerCase();

        if (confirmation.equals("y")) {
            return new Customer(name, address, phone, email,0.0, salesLimit);
        } else {
            System.out.println("Customer creation canceled.");
            return null;
        }
    }
    private void addNewCustomer(){
        Customer newCustomer = getNewCustomer();
        if(newCustomer!=null){
            FileHandler.writeCustomersToFile(newCustomer);
            System.out.printf("Customer Information successfully saved");
        }
    }
    public void updateCustomer(String name, String address, String phone, String email, double salesLimit) {
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.email = email;
        this.salesLimit = salesLimit;
    }

    private void updateCustomerDetails(){
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter Customer ID to modify details: ");
        int customerIdToModify = scanner.nextInt();
        scanner.nextLine(); // Consume the newline character

        Customer customerToModify = FileHandler.readSingleCustomerFromFile(customerIdToModify);
        if (customerToModify != null) {
            System.out.println("Customer found. Details:");
            System.out.println(customerToModify);

            System.out.println("Enter new customer details (leave blank if no change):");

            System.out.print("Name: ");
            String name = scanner.nextLine().trim();
            if (!name.isEmpty()) {
                customerToModify.setName(name);
            }

            System.out.print("Address: ");
            String address = scanner.nextLine().trim();
            if (!address.isEmpty()) {
                customerToModify.setAddress(address);
            }

            System.out.print("Phone: ");
            String phone = scanner.nextLine().trim();
            if (!phone.isEmpty()) {
                customerToModify.setPhone(phone);
            }

            System.out.print("Email: ");
            String email = scanner.nextLine().trim();
            if (!email.isEmpty()) {
                customerToModify.setEmail(email);
            }

            System.out.print("Sales Limit: ");
            String salesLimitInput = scanner.nextLine().trim();
            if (!salesLimitInput.isEmpty()) {
                double salesLimit = Double.parseDouble(salesLimitInput);
                customerToModify.setSalesLimit(salesLimit);
            }
            System.out.print("Payable Amount: ");
            String payableAmountInput = scanner.nextLine().trim();
            if (!payableAmountInput.isEmpty()) {
                double payableAmount = Double.parseDouble(payableAmountInput);
                customerToModify.setAmountPayable(payableAmount);
            }

            // Confirm save
            System.out.print("Do you want to save these changes? (Y/N): ");
            String confirmation = scanner.next().trim().toLowerCase();

            if (confirmation.equals("y")) {
                FileHandler.modifyCustomerToFile(customerToModify);
                System.out.println("Customer Information successfully saved.");
            } else {
                System.out.println("Customer modification canceled.");
            }
        } else {
            System.out.println("Customer with ID " + customerIdToModify + " not found.");
        }
    }




    @Override
    public String toString() {
        return String.format("%d;%s;%s;%s;%s;%.2f;%.2f", customerId, name, address, phone, email, amountPayable, salesLimit);
    }

    private void findCustomer(){
        Scanner scanner = new Scanner(System.in);

        System.out.println("Please specify at least one of the following to find the customer. Leave all fields blank to return to Customers Menu:");

        System.out.print("Customer ID: ");
        String customerIdInput = scanner.nextLine().trim();

        System.out.print("Name: ");
        String nameInput = scanner.nextLine().trim();

        System.out.print("Email: ");
        String emailInput = scanner.nextLine().trim();

        System.out.print("Phone: ");
        String phoneInput = scanner.nextLine().trim();

        System.out.print("Sales Limit: ");
        String salesLimitInput = scanner.nextLine().trim();

        // Searching for customers based on specified criteria
        List<Customer> foundCustomers = searchCustomers(customerIdInput, nameInput, emailInput, phoneInput, salesLimitInput);

        // Displaying the results
        if (!foundCustomers.isEmpty()) {
            displayCustomers(foundCustomers);
        } else {
            System.out.println("No customers found matching the specified criteria.");
        }
    }

    private static List<Customer> searchCustomers(String customerIdInput, String nameInput, String emailInput, String phoneInput, String salesLimitInput) {
        List<Customer> customerList = FileHandler.readCustomersFromFile();
        List<Customer> foundCustomers = new ArrayList<>();


        for (Customer customer : customerList) {
            try {
                if ((customerIdInput.isEmpty() || Integer.toString(customer.getId()).equals(customerIdInput)) &&
                        (nameInput.isEmpty() || customer.getName().equalsIgnoreCase(nameInput)) &&
                        (emailInput.isEmpty() || customer.getEmail().equalsIgnoreCase(emailInput)) &&
                        (phoneInput.isEmpty() || customer.getPhone().equalsIgnoreCase(phoneInput)) &&
                        (salesLimitInput.isEmpty() || Double.toString(customer.getSalesLimit()).equals(salesLimitInput))) {
                    foundCustomers.add(customer);
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input for sales limit. Please enter a numeric value.");
                return Collections.emptyList();
            }
        }

        return foundCustomers;
    }

    private static void displayCustomers(List<Customer> customers) {
        System.out.println("----------------------------------------------------------------------------------------------------------------------------");
        System.out.printf("%-15s%-20s%-30s%-15s%-30s%-15s%-15s%n", "Customer ID", "Name", "Email", "Phone", "Sales Limit", "Payable Amount", " ");
        System.out.println("----------------------------------------------------------------------------------------------------------------------------");

        for (Customer customer : customers) {
            System.out.printf("%-15d%-20s%-30s%-15s%-30.2f%-15.2f%n", customer.getId(), customer.getName(), customer.getEmail(), customer.getPhone(), customer.getSalesLimit(), customer.getAmountPayable());
        }

        System.out.println("----------------------------------------------------------------------------------------------------------------------------");
    }



    private void removeExistingCustomer(){
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter Item ID to remove: ");
        int customerIdToRemove = scanner.nextInt();


        // Confirm removal with the user
        System.out.print("Are you sure you want to remove the customer with ID " + customerIdToRemove + "? (Y/N): ");
        String confirmation = scanner.next().trim().toLowerCase();


        if (confirmation.equals("y")) {
            // Remove the item
            removeCustomer(customerIdToRemove);
            System.out.println("Customer with ID " + customerIdToRemove + " has been removed.");
        } else {
            System.out.println("Customer removal canceled.");
        }
    }

    private void removeCustomer(int customerIdToRemove) {
        List<Customer> customers = FileHandler.readCustomersFromFile();
        List<Customer> updatedCustomer = new ArrayList<>();

        for (Customer customer : customers) {
            if (customer.getId() != customerIdToRemove) {
                updatedCustomer.add(customer);
            }
        }

        FileHandler.writeAllCustomersToFile(updatedCustomer);
    }
    public void manageUserMenuChoice(int choice){
        switch (choice) {
            case 1:
                addNewCustomer();
                break;
            case 2:
                updateCustomerDetails();
                break;
            case 3:
                findCustomer();
                break;
            case 4:
                removeExistingCustomer();
                break;
            case 5:
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
        }
    }

}
