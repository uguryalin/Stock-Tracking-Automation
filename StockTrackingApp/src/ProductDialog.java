import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ProductDialog extends JDialog {
    private JTextField nameField;
    private JTextField priceField;
    private JTextField quantityField;
    private JButton saveButton, cancelButton;
    private boolean confirmed = false;
    private Product product;
    
    public ProductDialog(Frame parent, String title, Product existingProduct) {
        super(parent, title, true);
        this.product = existingProduct;
        initComponents();
        
        if (existingProduct != null) {
            populateFields(existingProduct);
        }
    }
    
    private void initComponents() {
        setSize(350, 250);
        setLocationRelativeTo(getParent());
        setResizable(false);
        
        JPanel mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // Product Name
        JLabel nameLabel = new JLabel("Product Name:");
        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.WEST;
        mainPanel.add(nameLabel, gbc);
        
        nameField = new JTextField(20);
        gbc.gridx = 1; gbc.gridy = 0; gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(nameField, gbc);
        
        // Price
        JLabel priceLabel = new JLabel("Price ($):");
        gbc.gridx = 0; gbc.gridy = 1; gbc.fill = GridBagConstraints.NONE;
        mainPanel.add(priceLabel, gbc);
        
        priceField = new JTextField(20);
        gbc.gridx = 1; gbc.gridy = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(priceField, gbc);
        
        // Quantity
        JLabel quantityLabel = new JLabel("Quantity:");
        gbc.gridx = 0; gbc.gridy = 2; gbc.fill = GridBagConstraints.NONE;
        mainPanel.add(quantityLabel, gbc);
        
        quantityField = new JTextField(20);
        gbc.gridx = 1; gbc.gridy = 2; gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(quantityField, gbc);
        
        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout());
        saveButton = new JButton("Save");
        cancelButton = new JButton("Cancel");
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(buttonPanel, gbc);
        
        add(mainPanel);
        
        // Event listeners
        saveButton.addActionListener(e -> saveProduct());
        cancelButton.addActionListener(e -> dispose());
        
        getRootPane().setDefaultButton(saveButton);
    }
    
    private void populateFields(Product product) {
        nameField.setText(product.getProductName());
        priceField.setText(String.valueOf(product.getPrice()));
        quantityField.setText(String.valueOf(product.getQuantity()));
    }
    
    private void saveProduct() {
        String name = nameField.getText().trim();
        String priceText = priceField.getText().trim();
        String quantityText = quantityField.getText().trim();
        
        // Validation
        if (name.isEmpty()) {
            showError("Product name cannot be empty!");
            return;
        }
        
        double price;
        try {
            price = Double.parseDouble(priceText);
            if (price < 0) {
                showError("Price cannot be negative!");
                return;
            }
        } catch (NumberFormatException e) {
            showError("Please enter a valid price!");
            return;
        }
        
        int quantity;
        try {
            quantity = Integer.parseInt(quantityText);
            if (quantity < 0) {
                showError("Quantity cannot be negative!");
                return;
            }
        } catch (NumberFormatException e) {
            showError("Please enter a valid quantity!");
            return;
        }
        
        // Create product
        this.product = new Product(0, name, price, quantity);
        this.confirmed = true;
        dispose();
    }
    
    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Input Error", JOptionPane.ERROR_MESSAGE);
    }
    
    public boolean isConfirmed() {
        return confirmed;
    }
    
    public Product getProduct() {
        return product;
    }
}