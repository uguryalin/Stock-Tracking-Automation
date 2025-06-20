import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FileManager {
    private static final String PRODUCTS_FILE = "stok.txt";
    private static final String USERS_FILE = "users.txt";
    private static final String REVENUE_FILE = "daily_revenue.txt";
    
    // Product operations
    public static void saveProducts(List<Product> products) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(PRODUCTS_FILE))) {
            for (Product product : products) {
                writer.println(product.toString());
            }
        } catch (IOException e) {
            System.err.println("Error saving products: " + e.getMessage());
        }
    }
    
    public static List<Product> loadProducts() {
        List<Product> products = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(PRODUCTS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 4) {
                    Product product = new Product(
                        Integer.parseInt(parts[0]),
                        parts[1],
                        Double.parseDouble(parts[2]),
                        Integer.parseInt(parts[3])
                    );
                    products.add(product);
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading products: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.err.println("Error parsing product data: " + e.getMessage());
        }
        return products;
    }
    
    // User operations
    public static void saveUsers(List<User> users) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(USERS_FILE))) {
            for (User user : users) {
                writer.println(user.getUsername() + "," + user.getPassword());
            }
        } catch (IOException e) {
            System.err.println("Error saving users: " + e.getMessage());
            throw new RuntimeException("Failed to save users", e);
        }
    }
    
    public static List<User> loadUsers() {
        List<User> users = new ArrayList<>();
        File file = new File(USERS_FILE);
        
        // Check if file exists
        if (!file.exists()) {
            // Create default admin user
            User defaultAdmin = new User("admin", "admin123");
            users.add(defaultAdmin);
            saveUsers(users);
            return users;
        }
        
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 2) {
                    // Handle usernames or passwords that might contain commas
                    String username = parts[0].trim();
                    String password = parts[1].trim();
                    users.add(new User(username, password));
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading users: " + e.getMessage());
        }
        
        // Create default admin user if no users exist or file was empty
        if (users.isEmpty()) {
            User defaultAdmin = new User("admin", "admin123");
            users.add(defaultAdmin);
            saveUsers(users);
        }
        
        return users;
    }
    
    public static boolean authenticateUser(String username, String password) {
        List<User> users = loadUsers();
        return users.stream().anyMatch(user -> 
            user.getUsername().equals(username) && user.getPassword().equals(password));
    }
    
    /**
     * Check if username already exists
     */
    public static boolean usernameExists(String username) {
        List<User> users = loadUsers();
        return users.stream().anyMatch(user -> 
            user.getUsername().equalsIgnoreCase(username));
    }
    
    /**
     * Add a new user (used by NewUserDialog)
     */
    public static boolean addUser(String username, String password) {
        try {
            if (usernameExists(username)) {
                return false; // Username already exists
            }
            
            List<User> users = loadUsers();
            users.add(new User(username, password));
            saveUsers(users);
            return true;
        } catch (Exception e) {
            System.err.println("Error adding user: " + e.getMessage());
            return false;
        }
    }
 
     //Get total number of users
     
    public static int getUserCount() {
        return loadUsers().size();
    }
    
    // Daily Revenue operations
    public static double loadDailyRevenue() {
        String today = getCurrentDate();
        double revenue = 0.0;
        
        try (BufferedReader reader = new BufferedReader(new FileReader(REVENUE_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    String date = parts[0];
                    double amount = Double.parseDouble(parts[1]);
                    
                    // If it's today's date, load the revenue
                    if (date.equals(today)) {
                        revenue = amount;
                        break;
                    }
                }
            }
        } catch (IOException e) {
            // File doesn't exist or can't be read, return 0
            System.out.println("Revenue file not found or empty, starting with 0 revenue");
        } catch (NumberFormatException e) {
            System.err.println("Error parsing revenue data: " + e.getMessage());
        }
        
        return revenue;
    }
    
    public static void saveDailyRevenue(double revenue) {
        String today = getCurrentDate();
        List<String> revenueData = new ArrayList<>();
        boolean todayUpdated = false;
        
        // Read existing data
        try (BufferedReader reader = new BufferedReader(new FileReader(REVENUE_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    String date = parts[0];
                    
                    if (date.equals(today)) {
                        // Update today's revenue
                        revenueData.add(today + "," + revenue);
                        todayUpdated = true;
                    } else {
                        // Keep other dates as they are
                        revenueData.add(line);
                    }
                }
            }
        } catch (IOException e) {
            // File doesn't exist, will be created
        }
        
        // If today's date wasn't found, add it
        if (!todayUpdated) {
            revenueData.add(today + "," + revenue);
        }
        
        // Write all data to file
        try (PrintWriter writer = new PrintWriter(new FileWriter(REVENUE_FILE))) {
            for (String data : revenueData) {
                writer.println(data);
            }
        } catch (IOException e) {
            System.err.println("Error saving daily revenue: " + e.getMessage());
        }
    }
    
    // Helper method to get current date
    private static String getCurrentDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        return dateFormat.format(new Date());
    }
    
    // Method to get revenue history (optional - for future features)
    public static List<String> getRevenueHistory() {
        List<String> history = new ArrayList<>();
        
        try (BufferedReader reader = new BufferedReader(new FileReader(REVENUE_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    String date = parts[0];
                    double amount = Double.parseDouble(parts[1]);
                    history.add("Date: " + date + " - Revenue: $" + String.format("%.2f", amount));
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading revenue history: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.err.println("Error parsing revenue history: " + e.getMessage());
        }
        
        return history;
    }
    
    // Method to reset daily revenue (optional - for testing or daily reset)
    public static void resetDailyRevenue() {
        saveDailyRevenue(0.0);
    }
    
    // Method to get revenue data in a specific date range
    public static Map<String, Double> getRevenueInRange(String startDate, String endDate) {
        Map<String, Double> revenueMap = new HashMap<>();
        
        try (BufferedReader reader = new BufferedReader(new FileReader(REVENUE_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    String date = parts[0];
                    double amount = Double.parseDouble(parts[1]);
                    
                    // Check if date is within range
                    if (isDateInRange(date, startDate, endDate)) {
                        revenueMap.put(date, amount);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading revenue data: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.err.println("Error parsing revenue data: " + e.getMessage());
        }
        
        return revenueMap;
    }
    
    // Helper method to check if a date is within range
    private static boolean isDateInRange(String dateStr, String startDateStr, String endDateStr) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            Date date = sdf.parse(dateStr);
            Date startDate = sdf.parse(startDateStr);
            Date endDate = sdf.parse(endDateStr);
            
            return !date.before(startDate) && !date.after(endDate);
        } catch (Exception e) {
            return false;
        }
    }
    
    // Utility methods for file management
    
    /**
     * Check if products file exists
     */
    public static boolean productsFileExists() {
        return new File(PRODUCTS_FILE).exists();
    }
    
    /**
     * Check if users file exists
     */
    public static boolean usersFileExists() {
        return new File(USERS_FILE).exists();
    }
    
    /**
     * Check if revenue file exists
     */
    public static boolean revenueFileExists() {
        return new File(REVENUE_FILE).exists();
    }
    
    /**
     * Create backup of all data files
     */
    public static void createBackup() {
        String timestamp = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date());
        
        try {
            // Backup products
            if (productsFileExists()) {
                copyFile(PRODUCTS_FILE, "backup_" + timestamp + "_" + PRODUCTS_FILE);
            }
            
            // Backup users
            if (usersFileExists()) {
                copyFile(USERS_FILE, "backup_" + timestamp + "_" + USERS_FILE);
            }
            
            // Backup revenue
            if (revenueFileExists()) {
                copyFile(REVENUE_FILE, "backup_" + timestamp + "_" + REVENUE_FILE);
            }
            
            System.out.println("Backup created successfully with timestamp: " + timestamp);
            
        } catch (IOException e) {
            System.err.println("Error creating backup: " + e.getMessage());
        }
    }
    
    /**
     * Copy file utility method
     */
    private static void copyFile(String source, String destination) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(source));
             PrintWriter writer = new PrintWriter(new FileWriter(destination))) {
            
            String line;
            while ((line = reader.readLine()) != null) {
                writer.println(line);
            }
        }
    }
}