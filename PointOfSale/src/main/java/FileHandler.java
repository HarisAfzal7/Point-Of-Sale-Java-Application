import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class FileHandler {
    private static final String ITEM_FILE_PATH = "Files/items.txt";
    private static final String CUSTOMER_FILE_PATH = "Files/customers.txt";
    private static final String SALE_FILE_PATH = "Files/sales.txt";
    private static final String RECEIPT_FILE_PATH = "Files/receipts.txt";

    public static List<Item> readItemsFromFile() {
        return readFromFile(ITEM_FILE_PATH, Item.class);
    }

    public static void writeItemsToFile(Item item) {
        writeToFile(item, ITEM_FILE_PATH);
    }
    public static void writeAllItemsToFile(List<Item> items) {
        writeAllToFile(items, ITEM_FILE_PATH);
    }

    public static List<Customer> readCustomersFromFile() {
        return readFromFile(CUSTOMER_FILE_PATH, Customer.class);
    }

    public static void writeCustomersToFile(Customer customers) {
        writeToFile(customers, CUSTOMER_FILE_PATH);
    }
    public static void writeAllCustomersToFile(List<Customer> customers) {
        writeAllToFile(customers, CUSTOMER_FILE_PATH);
    }

    public static List<Sale> readSalesFromFile() {
        return readFromFile(SALE_FILE_PATH, Sale.class);
    }

    public static void writeSalesToFile(Sale sales) {
        writeToFile(sales, SALE_FILE_PATH);
    }

    public static List<Receipt> readReceiptsFromFile() {
        return readFromFile(RECEIPT_FILE_PATH, Receipt.class);
    }

    public static void writeReceiptsToFile(Receipt receipts) {
        writeToFile(receipts, RECEIPT_FILE_PATH);
    }

    public static Item readSingleItemFromFile(int targetItemID) {
        return readSingleFromFile(ITEM_FILE_PATH, targetItemID, Item.class);
    }

    public static Customer readSingleCustomerFromFile(int targetCustomer) {
        return readSingleFromFile(CUSTOMER_FILE_PATH, targetCustomer, Customer.class);
    }

    public static Sale readSingleSaleFromFile(int targetCustomer) {
        return readSingleFromFile(SALE_FILE_PATH, targetCustomer, Sale.class);
    }

    public static void modifyItemToFile(Item needToModifyItem){
        modifyInFile(needToModifyItem, ITEM_FILE_PATH, Item.class);
    }
    public static void modifyCustomerToFile(Customer needToModifyCustomer){
        modifyInFile(needToModifyCustomer, CUSTOMER_FILE_PATH, Customer.class);
    }


    public static String getCurrentDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        return sdf.format(new Date());
    }

//    private static <T> List<T> readFromFile(String filePath) {
//        System.out.println("Reading from file: " + filePath);
//        List<T> items = new ArrayList<>();
//
//        try {
//            ObjectMapper objectMapper = new ObjectMapper();
//            File file = new File(filePath);
//
//            if (file.length() > 0) {
//                items = objectMapper.readValue(file, new TypeReference<List<T>>() {});
//                System.out.printf(items.toString());
//            } else {
//                System.out.println("File is empty.");
//            }
//        } catch (IOException e) {
//            // Handle exceptions
//            e.printStackTrace();
//        }
//
//        return items;
//    }
//

//    private static <T> List<T> readFromFile(String filePath, Class<T> valueType) {
//        System.out.println("Reading from file: " + filePath);
//        List<T> items = new ArrayList<>();
//
//        try {
//            ObjectMapper objectMapper = new ObjectMapper();
//            File file = new File(filePath);
//
//            if (file.length() > 0) {
//                JsonNode root = objectMapper.readTree(file);
//
//                if (root.isArray()) {
//                    Iterator<JsonNode> elements = root.elements();
//
//                    while (elements.hasNext()) {
//                        JsonNode element = elements.next();
//                        T item = objectMapper.treeToValue(element, valueType);
//                        items.add(item);
//                    }
//
//                    System.out.println(items);
//                } else {
//                    System.out.println("File does not contain a JSON array.");
//                }
//            } else {
//                System.out.println("File is empty.");
//            }
//        } catch (IOException e) {
//            // Handle exceptions
//            e.printStackTrace();
//        }
//
//        return items;
//    }

//    private static <T> List<T> readFromFile(String filePath, Class<T> valueType) {
//        System.out.println("Reading from file: " + filePath);
//        List<T> items = new ArrayList<>();
//
//        try {
//            ObjectMapper objectMapper = new ObjectMapper();
//            File file = new File(filePath);
//
//            if (file.length() > 0) {
//                // Read the root node directly
//                JsonNode root = objectMapper.readTree(file);
//
//                // Directly deserialize the single object
//                T item = objectMapper.treeToValue(root, valueType);
//                items.add(item);
//            } else {
//                System.out.println("File is empty.");
//            }
//        } catch (IOException e) {
//            // Handle exceptions
//            e.printStackTrace();
//        }
//
//        return items;
//    }

    private static <T> List<T> readFromFile(String filePath, Class<T> valueType) {
        System.out.println("Reading from file: " + filePath);
        List<T> items = new ArrayList<>();

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            File file = new File(filePath);

            if (file.length() > 0) {
                // Read each line as a separate object
                List<String> lines = Files.readAllLines(Paths.get(filePath));
                for (String line : lines) {
                    T item = objectMapper.readValue(line, valueType);
                    items.add(item);
                }
            } else {
                System.out.println("File is empty.");
            }
        } catch (IOException e) {
            // Handle exceptions
            e.printStackTrace();
        }

        return items;
    }



//    private static  <T extends Identifiable> T readSingleFromFile(String filePath, int targetItemID) {
//        System.out.println("Reading from file: " + filePath);
//        T item = null;
//        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath))) {
//            if (ois.available() > 0) {
//                while ((item = (T) ois.readObject()) != null) {
//                    if (item.getId() ==  targetItemID) {
//                        return item;
//                    }
//                }
//            } else {
//                System.out.println("File is empty.");
//            }
//        } catch (EOFException e) {
////            System.out.println("Reached end of file unexpectedly.");
////            e.printStackTrace();
//        } catch (IOException | ClassNotFoundException e) {
//            // Handle exceptions
////            e.printStackTrace();
//        }
//        return null;
//    }


//    private static <T extends Identifiable> T readSingleFromFile(String filePath, int targetItemId) {
//        System.out.println("Reading from file: " + filePath);
//        ObjectMapper objectMapper = new ObjectMapper();
//        T item = null;
//        try {
//            List<T> items = objectMapper.readValue(new File(filePath), new TypeReference<List<T>>() {});
//            System.out.printf(items.toString());
//            for (T currentItem : items) {
//                System.out.printf(String.valueOf(currentItem));
//                if (currentItem.getId() == targetItemId) {
//                    System.out.printf(String.valueOf(currentItem));
//                    return currentItem;
//                }
//            }
//        } catch (IOException e) {
//            // Handle exceptions
//            e.printStackTrace();
//        }
//        return null;
//    }

    private static <T extends Identifiable> T readSingleFromFile(String filePath, int targetItemId, Class<T> valueType) {
        System.out.println("Reading from file: " + filePath);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        List<T> items = readFromFile(filePath, valueType);
        for (T currentItem : items) {
            if (currentItem.getId() == targetItemId) {
                return currentItem;
            }
        }
        return null;
    }

//    private static <T> void writeToFile(T item, String filePath) {
//        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
//            oos.writeObject(item);
//        } catch (IOException e) {
//            // Handle exceptions
//            e.printStackTrace();
//        }
//    }

    private static <T> void writeToFile(T item, String filePath) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        try (FileWriter fileWriter = new FileWriter(filePath, true);
             BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {

            // Convert the item to JSON string
            String json = objectMapper.writeValueAsString(item);

            // Append the JSON string to the file
            bufferedWriter.write(json);
            bufferedWriter.newLine(); // Add a new line for each entry

        } catch (IOException e) {
            // Handle exceptions
            e.printStackTrace();
        }
    }

//    private static <T> void writeToFile(T item, String filePath) {
//        ObjectMapper objectMapper = new ObjectMapper();
//        try {
//            List<T> items = readFromFile(filePath);
//            items.add(item);
//
//            objectMapper.writeValue(new File(filePath), items);
//        } catch (IOException e) {
//            // Handle exceptions
//            e.printStackTrace();
//        }
//    }



    private static <T extends Identifiable> void modifyInFile(T modifiedItem, String filePath, Class<T> valueType) {

        List<T> items = readFromFile(filePath, valueType);

        for (int i = 0; i < items.size(); i++) {
            T currentItem = items.get(i);
            if (currentItem.getId() == modifiedItem.getId()) {
                items.set(i, modifiedItem);
                break;
            }
        }

        writeAllToFile(items, filePath);
    }

//    private static <T> void writeAllToFile(List<T> items, String filePath) {
//        System.out.println("Writing to file: " + filePath);
//        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
//            for (T item : items) {
//                oos.writeObject(item);
//            }
//        } catch (IOException e) {
//            // Handle exceptions
//            e.printStackTrace();
//        }
//    }

    private static <T> void writeAllToFile(List<T> items, String filePath) {
        System.out.println("Writing to file: " + filePath);
        try (FileWriter fileWriter = new FileWriter(filePath);
             BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {

            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());

            // Convert each item to JSON string and write to the file
            for (T item : items) {
                String json = objectMapper.writeValueAsString(item);
                bufferedWriter.write(json);
                bufferedWriter.newLine();
            }

        } catch (IOException e) {
            // Handle exceptions
            e.printStackTrace();
        }
    }
}
