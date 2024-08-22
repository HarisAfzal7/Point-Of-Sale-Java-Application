import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
public class Item  implements Identifiable {
    private static final long serialVersionUID = 1L;

    private static int nextItemId = 1;
    @JsonProperty("id")
    private int itemId;
    private String description;
    private double price;
    private int quantity;
    private String creationDate;

    public Item() {
    }

    public static int getNextItemId() {
        return nextItemId;
    }

    @Override
    public int getId() {
        return itemId;
    }

    public String getDescription() {
        return description;
    }

    public double getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public static void setNextItemId(int nextItemId) {
        Item.nextItemId = nextItemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public Item(String description, double price, int quantity) {
        this.itemId = nextItemId++;
        this.description = description;
        this.price = price;
        this.quantity = quantity;
        this.creationDate = getCurrentDate();
    }

    // Getters and setters

    @Override
    public String toString() {
        return String.format("%d;%s;%.2f;%d;%s;", itemId, description, price, quantity, creationDate);
    }

    private String getCurrentDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        return sdf.format(new Date());
    }
    public static Item getItemById(int itemId, List<Item> items) {
        for (Item item : items) {
            if (item.getId() == itemId) {
                return item;
            }
        }
        return null;
    }
    private void addNewItem(){
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter product description: ");
        String description = scanner.nextLine();

        System.out.print("Enter product price: ");
        double price = scanner.nextDouble();

        System.out.print("Enter product quantity: ");
        int quantity = scanner.nextInt();
        scanner.nextLine();

        Item newItem = new Item(description, price, quantity);

        // Display the entered details
        System.out.println("Item details entered:");
        System.out.println("Description: " + description);
        System.out.println("Price: " + price);
        System.out.println("Quantity: " + quantity);

        // Confirm from the user whether to save to a file or not
        System.out.print("Do you want to save this information to a file? (Y/N): ");
        String saveChoice = scanner.next().toLowerCase();

        if (saveChoice.equals("y")) {
            // Save to file
            FileHandler.writeItemsToFile(newItem);
            System.out.println("Item Information successfully saved.");
        } else {
            System.out.println("Item information not saved.");
        }

    }

    private void updateItemDetails(){
        Scanner scanner = new Scanner(System.in);
        // Ask the user to enter the Item Id
        System.out.print("Enter Item Id to modify: ");
        int itemIdToModify = scanner.nextInt();
        scanner.nextLine();

        // Search for the item with the entered Item Id

        Item itemToModify = FileHandler.readSingleItemFromFile(itemIdToModify);

        if(itemToModify!=null){

            System.out.println("Current Item Details:");
            System.out.println(itemToModify);

            // Ask the user to enter the modified item details
            System.out.println("Enter modified item details (leave blank if no change):");

            System.out.print("New Description: ");
            String newDescription = scanner.nextLine().trim();

            System.out.print("New Price: ");
            double newPrice = scanner.nextDouble();

            System.out.print("New Quantity: ");
            int newQuantity = scanner.nextInt();

            // Update the item details if user provided new values
            if (!newDescription.isEmpty()) {
                itemToModify.setDescription(newDescription);
            }
            itemToModify.setPrice(newPrice);
            itemToModify.setQuantity(newQuantity);

            // Confirm from the user whether to save the updated information to a file
            System.out.print("Do you want to save the updated information? (Y/N): ");
            scanner.nextLine(); // Consume the newline character left by nextInt
            String saveChoice = scanner.nextLine().toLowerCase();

            if (saveChoice.equals("y")) {
                // Save to file
                FileHandler.modifyItemToFile(itemToModify);
                System.out.println("Item information successfully saved.");
            } else {
                System.out.println("Item information not saved.");
            }
        } else {
            System.out.println("Item not found with the provided Item Id.");
        }

        // Close the scanner
    }


    private void findItem(){
        Scanner scanner = new Scanner(System.in);

        System.out.println("Please specify at least one of the following to find the item. Leave all fields blank to return to Customers Menu:");
        System.out.print("Item ID: ");
        String itemIdInput = scanner.nextLine().trim();

        System.out.print("Description: ");
        String descriptionInput = scanner.nextLine().trim();

        System.out.print("Price: ");
        String priceInput = scanner.nextLine().trim();

        System.out.print("Quantity: ");
        String quantityInput = scanner.nextLine().trim();

        System.out.print("Creation Date: ");
        String creationDateInput = scanner.nextLine().trim();

        // Searching for the item
        List<Item> foundItems = searchItems(itemIdInput, descriptionInput, priceInput, quantityInput, creationDateInput);

        // Displaying the results
        if (!foundItems.isEmpty()) {
            displayItems(foundItems);
        } else {
            System.out.println("No items found matching the specified criteria.");
        }
    }
    private static void displayItems(List<Item> items) {
        System.out.println("----------------------------------------------------------------------------------------------------------");
        System.out.printf("%-10s%-25s%-15s%-10s%n", "Item ID", "Description", "Price", "Quantity");
        System.out.println("----------------------------------------------------------------------------------------------------------");

        for (Item item : items) {
            System.out.println(item);
        }

        System.out.println("----------------------------------------------------------------------------------------------------------");
    }


    private static List<Item> searchItems(String itemIdInput, String descriptionInput, String priceInput, String quantityInput, String creationDateInput) {
        List<Item> items = FileHandler.readItemsFromFile();
        List<Item> foundItems = new ArrayList<>();

        for (Item item : items) {
            if ((itemIdInput.isEmpty() || Integer.toString(item.getId()).equals(itemIdInput)) &&
                    (descriptionInput.isEmpty() || item.getDescription().equalsIgnoreCase(descriptionInput)) &&
                    (priceInput.isEmpty() || Double.toString(item.getPrice()).equals(priceInput)) &&
                    (quantityInput.isEmpty() || Integer.toString(item.getQuantity()).equals(quantityInput)) &&
                    (creationDateInput.isEmpty() || item.getCreationDate().equalsIgnoreCase(creationDateInput))) {
                foundItems.add(item);
            }
        }

        return foundItems;
    }

    private void removeExistingItem(){
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter Item ID to remove: ");
        int itemIdToRemove = scanner.nextInt();


        // Confirm removal with the user
        System.out.print("Are you sure you want to remove the item with ID " + itemIdToRemove + "? (Y/N): ");
        String confirmation = scanner.next().trim().toLowerCase();


        if (confirmation.equals("y")) {
            // Remove the item
            removeItem(itemIdToRemove);
            System.out.println("Item with ID " + itemIdToRemove + " has been removed.");
        } else {
            System.out.println("Item removal canceled.");
        }

    }
    private void removeItem(int itemId) {
        List<Item> items = FileHandler.readItemsFromFile();
        List<Item> updatedItems = new ArrayList<>();

        for (Item item : items) {
            if (item.getId() != itemId) {
                updatedItems.add(item);
            }
        }

        FileHandler.writeAllItemsToFile(updatedItems);
    }
    public void manageUserMenuChoice(int choice){
        switch (choice) {
            case 1:
                addNewItem();
                break;
            case 2:
                updateItemDetails();
                break;
            case 3:
                findItem();
                break;
            case 4:
                removeExistingItem();
                break;
            case 5:
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
        }
    }
}
