import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.ArrayList;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainScreen extends JFrame {
    private JTable productTable;
    private DefaultTableModel tableModel;
    private List<Product> products;
    private JButton addButton, editButton, deleteButton, refreshButton, logoutButton, sellButton, revenueReportButton, newUserButton;
    private JLabel dailyRevenueLabel;
    private double dailyRevenue = 0.0;
    
    public MainScreen() {
        products = new ArrayList<>();
        loadDailyRevenue();
        initComponents();
        loadProducts();
    }
    
    private void initComponents() {
        setTitle("Stock Tracking System - Main Screen");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 650);
        setLocationRelativeTo(null);
        
        // Main panel
        setLayout(new BorderLayout());
        
        // Title panel with New User button
        JPanel titlePanel = new JPanel(new BorderLayout());
        
        // Title label (left side)
        JLabel titleLabel = new JLabel("PRODUCT INVENTORY");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(Color.BLUE);
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        titlePanel.add(titleLabel, BorderLayout.CENTER);
        
        // New User button (right side)
        newUserButton = new JButton("New User");
        newUserButton.setBackground(new Color(155, 89, 182));
        newUserButton.setForeground(Color.WHITE);
        newUserButton.setFont(new Font("Arial", Font.BOLD, 12));
        newUserButton.setPreferredSize(new Dimension(100, 30));
        
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rightPanel.add(newUserButton);
        titlePanel.add(rightPanel, BorderLayout.EAST);
        
        add(titlePanel, BorderLayout.NORTH);
        
        // Table setup
        String[] columnNames = {"Product ID", "Product Name", "Price ($)", "Quantity"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        productTable = new JTable(tableModel);
        productTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        productTable.getTableHeader().setReorderingAllowed(false);
        
        JScrollPane scrollPane = new JScrollPane(productTable);
        add(scrollPane, BorderLayout.CENTER);
        
        // Bottom panel containing buttons and revenue display
        JPanel bottomPanel = new JPanel(new BorderLayout());
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        
        addButton = new JButton("Add Product");
        
        editButton = new JButton("Edit Product");
        deleteButton = new JButton("Delete Product");
        
        sellButton = new JButton("Sell Product");
        revenueReportButton = new JButton("Revenue Report");
        refreshButton = new JButton("Refresh");
        logoutButton = new JButton("Logout");
        
        
        deleteButton.setBackground(new Color(255,0, 0));
        deleteButton.setForeground(Color.white);
        deleteButton.setFont(new Font("Arial", Font.BOLD, 12));
        // Set sell button color to make it stand out
        sellButton.setBackground(new Color(46, 204, 113));
        sellButton.setForeground(Color.WHITE);
        sellButton.setFont(new Font("Arial", Font.BOLD, 12));
        
        // Set revenue report button color
        revenueReportButton.setBackground(new Color(52, 152, 219));
        revenueReportButton.setForeground(Color.WHITE);
        revenueReportButton.setFont(new Font("Arial", Font.BOLD, 12));
        
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(sellButton);
        buttonPanel.add(revenueReportButton);
        buttonPanel.add(refreshButton);
        buttonPanel.add(logoutButton);
        
        // Revenue panel
        JPanel revenuePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        dailyRevenueLabel = new JLabel();
        updateRevenueDisplay();
        dailyRevenueLabel.setFont(new Font("Arial", Font.BOLD, 14));
        dailyRevenueLabel.setForeground(new Color(39, 174, 96));
        revenuePanel.add(dailyRevenueLabel);
        
        bottomPanel.add(buttonPanel, BorderLayout.WEST);
        bottomPanel.add(revenuePanel, BorderLayout.EAST);
        
        add(bottomPanel, BorderLayout.SOUTH);
        
        // Event listeners
        addButton.addActionListener(e -> openAddProductDialog());
        editButton.addActionListener(e -> openEditProductDialog());
        deleteButton.addActionListener(e -> deleteSelectedProduct());
        sellButton.addActionListener(e -> openSellProductDialog());
        revenueReportButton.addActionListener(e -> openRevenueReport());
        refreshButton.addActionListener(e -> loadProducts());
        logoutButton.addActionListener(e -> logout());
        newUserButton.addActionListener(e -> openNewUserDialog());
    }
    
    private void loadProducts() {
        products = FileManager.loadProducts();
        updateTable();
    }
    
    private void updateTable() {
        tableModel.setRowCount(0);
        for (Product product : products) {
            Object[] row = {
                product.getProductId(),
                product.getProductName(),
                String.format("%.2f", product.getPrice()),
                product.getQuantity()
            };
            tableModel.addRow(row);
        }
    }
    
    private void openAddProductDialog() {
        ProductDialog dialog = new ProductDialog(this, "Add Product", null);
        dialog.setVisible(true);
        
        if (dialog.isConfirmed()) {
            Product newProduct = dialog.getProduct();
            // Generate new ID
            int maxId = products.stream().mapToInt(Product::getProductId).max().orElse(0);
            newProduct.setProductId(maxId + 1);
            
            products.add(newProduct);
            FileManager.saveProducts(products);
            updateTable();
            JOptionPane.showMessageDialog(this, "Product added successfully!", 
                "Success", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private void openEditProductDialog() {
        int selectedRow = productTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a product to edit!", 
                "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        Product selectedProduct = products.get(selectedRow);
        ProductDialog dialog = new ProductDialog(this, "Edit Product", selectedProduct);
        dialog.setVisible(true);
        
        if (dialog.isConfirmed()) {
            Product updatedProduct = dialog.getProduct();
            updatedProduct.setProductId(selectedProduct.getProductId());
            products.set(selectedRow, updatedProduct);
            FileManager.saveProducts(products);
            updateTable();
            JOptionPane.showMessageDialog(this, "Product updated successfully!", 
                "Success", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private void openSellProductDialog() {
        int selectedRow = productTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a product to sell!", 
                "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        Product selectedProduct = products.get(selectedRow);
        
        if (selectedProduct.getQuantity() <= 0) {
            JOptionPane.showMessageDialog(this, "This product is out of stock!", 
                "Out of Stock", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        String quantityStr = JOptionPane.showInputDialog(this, 
            "Enter quantity to sell for '" + selectedProduct.getProductName() + "':\n" +
            "Available stock: " + selectedProduct.getQuantity() + "\n" +
            "Price per unit: $" + String.format("%.2f", selectedProduct.getPrice()),
            "Sell Product", JOptionPane.QUESTION_MESSAGE);
        
        if (quantityStr != null && !quantityStr.trim().isEmpty()) {
            try {
                int sellQuantity = Integer.parseInt(quantityStr.trim());
                
                if (sellQuantity <= 0) {
                    JOptionPane.showMessageDialog(this, "Please enter a valid quantity (greater than 0)!", 
                        "Invalid Quantity", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                if (sellQuantity > selectedProduct.getQuantity()) {
                    JOptionPane.showMessageDialog(this, 
                        "Not enough stock! Available: " + selectedProduct.getQuantity(), 
                        "Insufficient Stock", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                // Calculate sale amount
                double saleAmount = sellQuantity * selectedProduct.getPrice();
                
                // Confirm sale
                int confirm = JOptionPane.showConfirmDialog(this,
                    "Confirm Sale:\n" +
                    "Product: " + selectedProduct.getProductName() + "\n" +
                    "Quantity: " + sellQuantity + "\n" +
                    "Unit Price: $" + String.format("%.2f", selectedProduct.getPrice()) + "\n" +
                    "Total Amount: $" + String.format("%.2f", saleAmount),
                    "Confirm Sale", JOptionPane.YES_NO_OPTION);
                
                if (confirm == JOptionPane.YES_OPTION) {
                    // Update stock
                    selectedProduct.setQuantity(selectedProduct.getQuantity() - sellQuantity);
                    
                    // Update daily revenue
                    dailyRevenue += saleAmount;
                    saveDailyRevenue();
                    updateRevenueDisplay();
                    
                    // Save changes
                    FileManager.saveProducts(products);
                    updateTable();
                    
                    JOptionPane.showMessageDialog(this, 
                        "Sale completed successfully!\n" +
                        "Amount: $" + String.format("%.2f", saleAmount) + "\n" +
                        "Remaining stock: " + selectedProduct.getQuantity(), 
                        "Sale Completed", JOptionPane.INFORMATION_MESSAGE);
                }
                
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter a valid number!", 
                    "Invalid Input", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void deleteSelectedProduct() {
        int selectedRow = productTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a product to delete!", 
                "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        Product selectedProduct = products.get(selectedRow);
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to delete '" + selectedProduct.getProductName() + "'?", 
            "Confirm Delete", JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            products.remove(selectedRow);
            FileManager.saveProducts(products);
            updateTable();
            JOptionPane.showMessageDialog(this, "Product deleted successfully!", 
                "Success", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private void openNewUserDialog() {
        NewUser dialog = new NewUser(this);
        dialog.setVisible(true);
    }
    
    private void updateRevenueDisplay() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String today = dateFormat.format(new Date());
        dailyRevenueLabel.setText("Daily Revenue (" + today + "): $" + String.format("%.2f", dailyRevenue));

    }
    
    private void loadDailyRevenue() {
        // Bu metod günlük ciroyu dosyadan yükler
        // FileManager'a günlük ciro yükleme metodu eklemeniz gerekebilir
        try {
            dailyRevenue = FileManager.loadDailyRevenue();
        } catch (Exception e) {
            dailyRevenue = 0.0;
        }
    }
    
    private void saveDailyRevenue() {
        // Bu metod günlük ciroyu dosyaya kaydeder
        // FileManager'a günlük ciro kaydetme metodu eklemeniz gerekebilir
        try {
            FileManager.saveDailyRevenue(dailyRevenue);
        } catch (Exception e) {
            System.err.println("Error saving daily revenue: " + e.getMessage());
        }
    }
    
    private void openRevenueReport() {
        RevenueReportDialog reportDialog = new RevenueReportDialog(this);
        reportDialog.setVisible(true);
    }
    
    private void logout() {
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to logout?", 
            "Confirm Logout", JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            this.dispose();
            new LoginScreen().setVisible(true);
        }
    }
}