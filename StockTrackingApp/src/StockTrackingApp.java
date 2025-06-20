import javax.swing.*;

public class StockTrackingApp {
    public static void main(String[] args) {
        // Set look and feel
        try {
           UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // Create and show login screen
        SwingUtilities.invokeLater(() -> {
            new LoginScreen().setVisible(true);
        });
    }
}