import java.io.*;
import java.util.*;

/* Expense Class */
class Expense implements Serializable {
    String date;      // format: YYYY-MM
    String category;
    double amount;

    public Expense(String date, String category, double amount) {
        this.date = date;
        this.category = category;
        this.amount = amount;
    }

    @Override
    public String toString() {
        return date + "," + category + "," + amount;
    }
}

/* Main Class */
public class ExpenseTrackers {

    static ArrayList<Expense> expenses = new ArrayList<>();
    static final String FILE_NAME = "expenses.txt";

    public static void main(String[] args) {
        loadFromFile();
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\n---- Expense Tracker ----");
            System.out.println("1. Add Expense");
            System.out.println("2. View All Expenses");
            System.out.println("3. Sort by Month");
            System.out.println("4. Search Expense by Month");
            System.out.println("5. Category-wise Analysis");
            System.out.println("6. Exit");
            System.out.print("Choose option: ");

            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1 -> addExpense(sc);
                case 2 -> viewExpenses();
                case 3 -> sortByMonth();
                case 4 -> searchByMonth(sc);
                case 5 -> categoryAnalysis();
                case 6 -> {
                    saveToFile();
                    System.out.println("Data saved. Exiting...");
                    return;
                }
                default -> System.out.println("Invalid choice!");
            }
        }
    }

    /* Add Expense */
    static void addExpense(Scanner sc) {
        System.out.print("Enter month (YYYY-MM): ");
        String date = sc.nextLine();
        System.out.print("Enter category: ");
        String category = sc.nextLine();
        System.out.print("Enter amount: ");
        double amount = sc.nextDouble();

        expenses.add(new Expense(date, category, amount));
        System.out.println("Expense added successfully!");
    }

    /* View Expenses */
    static void viewExpenses() {
        if (expenses.isEmpty()) {
            System.out.println("No expenses found.");
            return;
        }
        expenses.forEach(System.out::println);
    }

    /* Sorting Month-wise */
    static void sortByMonth() {
        expenses.sort(Comparator.comparing(e -> e.date));
        System.out.println("Expenses sorted by month.");
    }

    /* Binary Search by Month */
    static void searchByMonth(Scanner sc) {
        sortByMonth(); // Required before binary search

        System.out.print("Enter month to search (YYYY-MM): ");
        String target = sc.nextLine();

        int low = 0, high = expenses.size() - 1;
        boolean found = false;

        while (low <= high) {
            int mid = (low + high) / 2;
            int cmp = expenses.get(mid).date.compareTo(target);

            if (cmp == 0) {
                System.out.println("Expense found: " + expenses.get(mid));
                found = true;
                break;
            } else if (cmp < 0) {
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }

        if (!found)
            System.out.println("No expense found for this month.");
    }

    /* Category-wise Analysis */
    static void categoryAnalysis() {
        HashMap<String, Double> map = new HashMap<>();

        for (Expense e : expenses) {
            map.put(e.category, map.getOrDefault(e.category, 0.0) + e.amount);
        }

        System.out.println("\nCategory-wise Expense:");
        for (String key : map.keySet()) {
            System.out.println(key + " : ₹" + map.get(key));
        }
    }

    /* File Handling - Save */
    static void saveToFile() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (Expense e : expenses) {
                bw.write(e.toString());
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving file.");
        }
    }

    /* File Handling - Load */
    static void loadFromFile() {
        File file = new File(FILE_NAME);
        if (!file.exists()) return;

        try (BufferedReader br = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                expenses.add(new Expense(
                        data[0],
                        data[1],
                        Double.parseDouble(data[2])
                ));
            }
        } catch (IOException e) {
            System.out.println("Error loading file.");
        }
    }
}
