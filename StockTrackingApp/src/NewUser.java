import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class NewUser extends JDialog {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private JButton saveButton, cancelButton;
    private boolean confirmed = false;
    
    public NewUser(JFrame parent) {
        super(parent, "Add New User", true);
        initComponents();
    }
    
    private void initComponents() {
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(400, 250);
        setLocationRelativeTo(getParent());
        setResizable(false);
        
        // Main panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        
        // Title panel
        JPanel titlePanel = new JPanel();
        JLabel titleLabel = new JLabel("Create New User Account");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setForeground(new Color(52, 73, 94));
        titlePanel.add(titleLabel);
        mainPanel.add(titlePanel, BorderLayout.NORTH);
        
        // Form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Username field
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Username:"), gbc);
        
        
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        usernameField = new JTextField(15);
        formPanel.add(usernameField, gbc);
        
        // Password field
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        formPanel.add(new JLabel("Password:"), gbc);
        
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        passwordField = new JPasswordField(15);
        formPanel.add(passwordField, gbc);
        
        // Confirm Password field
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        formPanel.add(new JLabel("Confirm Password:"), gbc);
        
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        confirmPasswordField = new JPasswordField(15);
        formPanel.add(confirmPasswordField, gbc);
        
        mainPanel.add(formPanel, BorderLayout.CENTER);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout());
        
        saveButton = new JButton("Save User");
        saveButton.setBackground(new Color(46, 204, 113));
        saveButton.setForeground(Color.WHITE);
        saveButton.setFont(new Font("Arial", Font.BOLD, 12));
        saveButton.setPreferredSize(new Dimension(100, 35));
        
        cancelButton = new JButton("Cancel");
        cancelButton.setBackground(new Color(231, 76, 60));
        cancelButton.setForeground(Color.WHITE);
        cancelButton.setFont(new Font("Arial", Font.BOLD, 12));
        cancelButton.setPreferredSize(new Dimension(100, 35));
        
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
        
        // Event listeners
        saveButton.addActionListener(e -> saveUser());
        cancelButton.addActionListener(e -> dispose());
        
        // Enter key listener for password fields
        ActionListener enterListener = e -> saveUser();
        usernameField.addActionListener(enterListener);
        passwordField.addActionListener(enterListener);
        confirmPasswordField.addActionListener(enterListener);
        
        // Focus on username field
        SwingUtilities.invokeLater(() -> usernameField.requestFocus());
    }
    
    private void saveUser() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());
        
        // Validation
        if (username.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a username!", 
                "Empty Username", JOptionPane.ERROR_MESSAGE);
            usernameField.requestFocus();
            return;
        }
        
        if (username.length() < 3) {
            JOptionPane.showMessageDialog(this, "Username must be at least 3 characters long!", 
                "Invalid Username", JOptionPane.ERROR_MESSAGE);
            usernameField.requestFocus();
            return;
        }
        
        if (password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a password!", 
                "Empty Password", JOptionPane.ERROR_MESSAGE);
            passwordField.requestFocus();
            return;
        }
        
        if (password.length() < 4) {
            JOptionPane.showMessageDialog(this, "Password must be at least 4 characters long!", 
                "Invalid Password", JOptionPane.ERROR_MESSAGE);
            passwordField.requestFocus();
            return;
        }
        
        if (!password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(this, "Passwords do not match!", 
                "Password Mismatch", JOptionPane.ERROR_MESSAGE);
            confirmPasswordField.requestFocus();
            return;
        }
        
        // Check if username already exists and add user
        try {
            if (FileManager.usernameExists(username)) {
                JOptionPane.showMessageDialog(this, "Username already exists! Please choose a different username.", 
                    "Username Exists", JOptionPane.ERROR_MESSAGE);
                usernameField.requestFocus();
                return;
            }
            
            // Add new user using FileManager
            boolean success = FileManager.addUser(username, password);
            
            if (success) {
                JOptionPane.showMessageDialog(this, 
                    "User '" + username + "' created successfully!\n" +
                    "Total users: " + FileManager.getUserCount(), 
                    "Success", JOptionPane.INFORMATION_MESSAGE);
                
                confirmed = true;
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Failed to create user. Please try again.", 
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Error creating user: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public boolean isConfirmed() {
        return confirmed;
    }
}