import java.io.*;
import java.nio.file.*;
import java.util.*;

public class FileListMaker {
    private static List<String> itemList = new ArrayList<>();
    private static boolean needsToBeSaved = false;
    private static String currentFileName = null;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("\nMenu:");
            System.out.println("A - Add an item");
            System.out.println("D - Delete an item");
            System.out.println("I - Insert an item");
            System.out.println("M - Move an item");
            System.out.println("V - View list");
            System.out.println("C - Clear list");
            System.out.println("O - Open a list file");
            System.out.println("S - Save the current list");
            System.out.println("Q - Quit");
            System.out.print("Choose an option: ");

            String choice = scanner.nextLine().toUpperCase();

            try {
                switch (choice) {
                    case "A":
                        addItem(scanner);
                        break;
                    case "D":
                        deleteItem(scanner);
                        break;
                    case "I":
                        insertItem(scanner);
                        break;
                    case "M":
                        moveItem(scanner);
                        break;
                    case "V":
                        viewList();
                        break;
                    case "C":
                        clearList();
                        break;
                    case "O":
                        openList(scanner);
                        break;
                    case "S":
                        saveList();
                        break;
                    case "Q":
                        if (needsToBeSaved) {
                            promptToSaveBeforeExit(scanner);
                        }
                        running = false;
                        break;
                    default:
                        System.out.println("Invalid option. Please try again.");
                }
            } catch (IOException e) {
                System.out.println("File error: " + e.getMessage());
            }
        }
        scanner.close();
    }

    private static void addItem(Scanner scanner) {
        System.out.print("Enter an item to add: ");
        String item = scanner.nextLine();
        itemList.add(item);
        needsToBeSaved = true;
    }

    private static void deleteItem(Scanner scanner) {
        viewList();
        System.out.print("Enter index to delete (0-based): ");
        int index = Integer.parseInt(scanner.nextLine());
        if (index >= 0 && index < itemList.size()) {
            itemList.remove(index);
            needsToBeSaved = true;
        } else {
            System.out.println("Invalid index.");
        }
    }

    private static void insertItem(Scanner scanner) {
        System.out.print("Enter item to insert: ");
        String item = scanner.nextLine();
        System.out.print("Enter index to insert at (0-based): ");
        int index = Integer.parseInt(scanner.nextLine());
        if (index >= 0 && index <= itemList.size()) {
            itemList.add(index, item);
            needsToBeSaved = true;
        } else {
            System.out.println("Invalid index.");
        }
    }

    private static void moveItem(Scanner scanner) {
        viewList();
        System.out.print("Enter index of item to move: ");
        int fromIndex = Integer.parseInt(scanner.nextLine());
        System.out.print("Enter new index: ");
        int toIndex = Integer.parseInt(scanner.nextLine());
        if (fromIndex >= 0 && fromIndex < itemList.size() && toIndex >= 0 && toIndex <= itemList.size()) {
            String item = itemList.remove(fromIndex);
            itemList.add(toIndex, item);
            needsToBeSaved = true;
        } else {
            System.out.println("Invalid indices.");
        }
    }

    private static void viewList() {
        System.out.println("\nCurrent List:");
        for (int i = 0; i < itemList.size(); i++) {
            System.out.println(i + ": " + itemList.get(i));
        }
    }

    private static void clearList() {
        itemList.clear();
        needsToBeSaved = true;
        System.out.println("List cleared.");
    }

    private static void openList(Scanner scanner) throws IOException {
        if (needsToBeSaved) {
            promptToSaveBeforeLoad(scanner);
        }
        System.out.print("Enter filename to open: ");
        currentFileName = scanner.nextLine();
        loadFromFile(currentFileName);
    }

    private static void saveList() throws IOException {
        if (currentFileName == null) {
            System.out.print("Enter filename to save as: ");
            Scanner scanner = new Scanner(System.in);
            currentFileName = scanner.nextLine();
        }
        saveToFile(currentFileName);
        needsToBeSaved = false;
    }

    private static void loadFromFile(String filename) throws IOException {
        itemList = Files.readAllLines(Paths.get(filename + ".txt"));
        needsToBeSaved = false;
        System.out.println("List loaded from " + filename + ".txt");
    }

    private static void saveToFile(String filename) throws IOException {
        Files.write(Paths.get(filename + ".txt"), itemList);
        System.out.println("List saved to " + filename + ".txt");
    }

    private static void promptToSaveBeforeExit(Scanner scanner) throws IOException {
        System.out.print("You have unsaved changes. Save before exiting? (Y/N): ");
        if (scanner.nextLine().equalsIgnoreCase("Y")) {
            saveList();
        }
    }

    private static void promptToSaveBeforeLoad(Scanner scanner) throws IOException {
        System.out.print("You have unsaved changes. Save before loading new file? (Y/N): ");
        if (scanner.nextLine().equalsIgnoreCase("Y")) {
            saveList();
        }
    }
}
