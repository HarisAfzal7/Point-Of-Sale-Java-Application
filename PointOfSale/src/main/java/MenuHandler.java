import java.util.Scanner;

public class MenuHandler {
    private static final Scanner scanner = new Scanner(System.in);

    public MenuHandler() {
    }

    public static void displayItemsMenu() {
        System.out.println("Items Menu\n" +
                "1- Add new Item\n" +
                "2- Update Item details\n" +
                "3- Find Items\n" +
                "4- Remove Existing Item\n" +
                "5- Back to Main Menu\n" +
                "Press 1 to 5 to select an option:");
    }

    public static void displayCustomersMenu() {
        System.out.println("Customers Menu\n" +
                "1- Add new Customer\n" +
                "2- Update Customer details\n" +
                "3- Find Customer\n" +
                "4- Remove Existing Customer\n" +
                "5- Back to Main Menu\n" +
                "Press 1 to 5 to select an option:");
    }

    public static void displayReportsMenu() {
        System.out.println("Reports Menu\n" +
                "1- Stock in hand\n" +
                "2- Outstanding orders\n" +
                "3- Customer Balance\n" +
                "4- Sales Receipts\n" +
                "5- Back to Main Menu\n" +
                "Press 1 to 5 to select an option:");
    }

    public static void displayMakeSaleMenu() {
        System.out.println("Make New Sale\n" +
                "Sales Date: " + FileHandler.getCurrentDate() + "\n" +
                "Enter Customer ID: ");
    }

    public static void displaySaleConfirmationMenu() {
        System.out.println("Press 1 to Enter New Item\n" +
                "Press 2 to End Sale\n" +
                "Press 3 to Remove an existing Item from the current sale\n" +
                "Press 4 to Cancel Sale\n" +
                "Choose from option 1 – 4:");
    }

    public static int getUserChoice() {
        System.out.print("Enter your choice: ");
        return scanner.nextInt();
    }
}
