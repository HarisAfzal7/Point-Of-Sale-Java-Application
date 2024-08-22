import java.util.List;
import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final List<Item> items = FileHandler.readItemsFromFile();
    private static final List<Customer> customers = FileHandler.readCustomersFromFile();
    private static final List<Sale> sales = FileHandler.readSalesFromFile();
    private static  final MenuHandler menuHandler = new MenuHandler();
    private static final Item item = new Item();
    private static final Customer customer = new Customer();
    private static final Sale sale = new Sale();

    public static void main(String[] args) {
        boolean exit = false;

        while (!exit) {
            displayMainMenu();
            int choice = getUserChoice();

            switch (choice) {
                case 1:
                    manageItems();
                    break;
                case 2:
                    manageCustomers();
                    break;
                case 3:
                    makeNewSale();
                    break;
                case 4:
                    makePayment();
                    break;
                case 5:
                    printReports();
                    break;
                case 6:
                    exit = true;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
        scanner.close();
    }

    private static void displayMainMenu() {
        System.out.println("Main Menu\n" +
                "1- Manage Items\n" +
                "2- Manage Customers\n" +
                "3- Make New Sale\n" +
                "4- Make Payment\n" +
                "5- Print Reports\n" +
                "6- Exit\n" +
                "Press 1 to 6 to select an option:");
    }

    private static int getUserChoice() {
        System.out.print("Enter your choice: ");
        return scanner.nextInt();
    }

    private static void manageItems() {
        do {
            menuHandler.displayItemsMenu();
            int itemsChoice = menuHandler.getUserChoice();
            if(itemsChoice>0 && itemsChoice<5){
                item.manageUserMenuChoice(itemsChoice);
            }else if(itemsChoice==5){
                break;
            }else {
                System.out.printf("Please select the correct option between 1-5:");
            }
        }while(true);
    }

    private static void manageCustomers() {
        do {
            menuHandler.displayCustomersMenu();
            int customerChoice = menuHandler.getUserChoice();
            if (customerChoice > 0 && customerChoice < 5) {
                customer.manageUserMenuChoice(customerChoice);
            } else if (customerChoice == 5) {
                break;
            } else {
                System.out.printf("Please select the correct option between 1-5:");
            }
        }while(true);
    }

    private static void makeNewSale() {
        menuHandler.displayMakeSaleMenu();
        Sale.makeNewSaleRecord();
    }

    private static void makePayment() {
        Receipt.makePayment();
    }


    private static void printReports() {
        ViewReport viewReport = new ViewReport();
        do {
            menuHandler.displayReportsMenu();
            int reportChoice = menuHandler.getUserChoice();
            if(reportChoice>0&& reportChoice<5){
                viewReport.manageUserMenuChoice(reportChoice);
            }else if(reportChoice==5){
                break;
            }else {
                System.out.printf("Please select the correct option between 1-5:");
            }

        }while (true);
    }
}
