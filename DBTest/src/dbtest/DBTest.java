import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class DBTest {
    public static void main(String[] args) {
        // Database URL for DerbyDB (JavaDB)
        String dbURL = "jdbc:derby://localhost:1527/Sales";
        
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        
        
        Scanner scanner = new Scanner(System.in);

        try {
            // Establish connection to the database
            conn = DriverManager.getConnection(dbURL);
            stmt = conn.createStatement();

            boolean running = true;

            while (running) {
                // Display menu
                System.out.println("Menu:");
                System.out.println("1. Display all car makes");
                System.out.println("2. Find oldest model");
                System.out.println("3. Total value of all cars");
                System.out.println("4. Most expensive car");
                System.out.println("5. Least expensive car");
                System.out.println("6. Exit");
                System.out.print("Choose an option: ");
                int choice = scanner.nextInt();

                // Execute the common query to retrieve all cars
                rs = stmt.executeQuery("SELECT * FROM CARS");

                switch (choice) {
                    case 1:
                        displayAllCarMakes(rs);
                        break;
                    case 2:
                        findOldestModel(rs);
                        break;
                    case 3:
                        calculateTotalValue(rs);
                        break;
                    case 4:
                        findMostExpensiveCar(rs);
                        break;
                    case 5:
                        findLeastExpensiveCar(rs);
                        break;
                    case 6:
                        running = false;
                        System.out.println("Exiting...");
                        break;
                    default:
                        System.out.println("Invalid option. Please choose a valid option.");
                        break;
                }
                System.out.println();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            // Close resources
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        scanner.close();
    }

    // Display all car makes
    private static void displayAllCarMakes(ResultSet rs) throws SQLException {
        System.out.println("List of Car Makes:");
        while (rs.next()) {
            String make = rs.getString("make");
            System.out.println(make);
        }
    }

    // Find the oldest car model
    private static void findOldestModel(ResultSet rs) throws SQLException {
        int oldestYear = Integer.MAX_VALUE;
        String oldestCar = "";

        while (rs.next()) {
            int year = rs.getInt("manufacture_year");
            if (year < oldestYear) {
                oldestYear = year;
                oldestCar = rs.getString("make") + " " + rs.getString("model");
            }
        }

        System.out.println("Oldest Car: " + oldestCar + " (" + oldestYear + ")");
    }

    // Calculate the total value of all cars
    private static void calculateTotalValue(ResultSet rs) throws SQLException {
        double totalValue = 0;

        while (rs.next()) {
            totalValue += rs.getDouble("price");
        }

        System.out.println("Total value of all cars: R" + totalValue);
    }

    // Find the most expensive car
    private static void findMostExpensiveCar(ResultSet rs) throws SQLException {
        double maxPrice = Double.MIN_VALUE;
        String expensiveCar = "";

        while (rs.next()) {
            double price = rs.getDouble("price");
            if (price > maxPrice) {
                maxPrice = price;
                expensiveCar = rs.getString("make") + " " + rs.getString("model");
            }
        }

        System.out.println("Most Expensive Car: " + expensiveCar + " (R" + maxPrice + ")");
    }

    // Find the least expensive car
    private static void findLeastExpensiveCar(ResultSet rs) throws SQLException {
        double minPrice = Double.MAX_VALUE;
        String cheapCar = "";

        while (rs.next()) {
            double price = rs.getDouble("price");
            if (price < minPrice) {
                minPrice = price;
                cheapCar = rs.getString("make") + " " + rs.getString("model");
            }
        }

        System.out.println("Least Expensive Car: " + cheapCar + " (R" + minPrice + ")");
    }
}
