import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginScreen extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    
    public LoginScreen() {
        initComponents();
    }
    
    private void initComponents() {
        setTitle("Stock Tracking System - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 350);
        setLocationRelativeTo(null);
        setResizable(false);
        
        // Main panel
        JPanel mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        
        // Title
        JLabel titleLabel = new JLabel("STOCK TRACKING SYSTEM");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(Color.BLUE);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 10, 30, 10);
        mainPanel.add(titleLabel, gbc);
        
        // Username
        gbc.gridwidth = 1;
        gbc.insets = new Insets(5, 10, 5, 10);
        JLabel usernameLabel = new JLabel("Username:");
        gbc.gridx = 0; gbc.gridy = 1;
        mainPanel.add(usernameLabel, gbc);
        
        usernameField = new JTextField(20);
        usernameField.setPreferredSize(new Dimension(250, 35));
        gbc.gridx = 1; gbc.gridy = 1;
        mainPanel.add(usernameField, gbc);
        
        // Password
        JLabel passwordLabel = new JLabel("Password:");
        gbc.gridx = 0; gbc.gridy = 2;
        mainPanel.add(passwordLabel, gbc);
        
        passwordField = new JPasswordField(20);
        passwordField.setPreferredSize(new Dimension(250, 35));
        gbc.gridx = 1; gbc.gridy = 2;
        mainPanel.add(passwordField, gbc);
        
        // Login button
        loginButton = new JButton("Login");
        loginButton.setPreferredSize(new Dimension(100, 30));
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 10, 10, 10);
        mainPanel.add(loginButton, gbc);
        
        // Default credentials info
        JLabel infoLabel = new JLabel("<html><center>Default Login:<br>Username: admin<br>Password: admin123</center></html>");
        infoLabel.setFont(new Font("Arial", Font.ITALIC, 10));
        infoLabel.setForeground(Color.GRAY);
        gbc.gridy = 4;
        gbc.insets = new Insets(10, 10, 10, 10);
        mainPanel.add(infoLabel, gbc);
        
        add(mainPanel);
        
        // Event listeners
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performLogin();
            }
        });
        
        // Enter key support
        getRootPane().setDefaultButton(loginButton);
    }
    
    private void performLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());
        
        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter both username and password!", 
                "Login Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (FileManager.authenticateUser(username, password)) {
            JOptionPane.showMessageDialog(this, "Login successful!", 
                "Success", JOptionPane.INFORMATION_MESSAGE);
            this.dispose();
            new MainScreen().setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Invalid username or password!", 
                "Login Error", JOptionPane.ERROR_MESSAGE);
            passwordField.setText("");
        }
    }
}
