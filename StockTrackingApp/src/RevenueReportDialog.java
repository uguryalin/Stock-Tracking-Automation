import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import java.text.ParseException;

public class RevenueReportDialog extends JDialog {
    private JTextField startDateField, endDateField;
    private JButton generateReportButton, closeButton, currentMonthButton, lastMonthButton;
    private JTable revenueTable;
    private DefaultTableModel tableModel;
    private JLabel totalRevenueLabel;
    private JTextArea summaryArea;
    
    public RevenueReportDialog(JFrame parent) {
        super(parent, "Revenue Report", true);
        initComponents();
        loadCurrentMonthData();
    }
    
    private void initComponents() {
        setSize(700, 600);
        setLocationRelativeTo(getParent());
        setLayout(new BorderLayout());
        
        // Title panel
        JPanel titlePanel = new JPanel();
        JLabel titleLabel = new JLabel("REVENUE REPORT");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(new Color(52, 152, 219));
        titlePanel.add(titleLabel);
        add(titlePanel, BorderLayout.NORTH);
        
        // Date selection panel
        JPanel datePanel = new JPanel(new FlowLayout());
        datePanel.setBorder(BorderFactory.createTitledBorder("Select Date Range"));
        
        datePanel.add(new JLabel("Start Date (dd/MM/yyyy):"));
        startDateField = new JTextField(10);
        datePanel.add(startDateField);
        
        datePanel.add(new JLabel("End Date (dd/MM/yyyy):"));
        endDateField = new JTextField(10);
        datePanel.add(endDateField);
        
        generateReportButton = new JButton("Generate Report");
        generateReportButton.setBackground(new Color(46, 204, 113));
        generateReportButton.setForeground(Color.WHITE);
        generateReportButton.addActionListener(e -> generateReport());
        datePanel.add(generateReportButton);
        
        // Quick selection buttons
        JPanel quickSelectPanel = new JPanel(new FlowLayout());
        quickSelectPanel.setBorder(BorderFactory.createTitledBorder("Quick Select"));
        
        currentMonthButton = new JButton("Current Month");
        currentMonthButton.addActionListener(e -> setCurrentMonth());
        quickSelectPanel.add(currentMonthButton);
        
        lastMonthButton = new JButton("Last Month");
        lastMonthButton.addActionListener(e -> setLastMonth());
        quickSelectPanel.add(lastMonthButton);
        
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(datePanel, BorderLayout.NORTH);
        topPanel.add(quickSelectPanel, BorderLayout.SOUTH);
        
        add(topPanel, BorderLayout.NORTH);
        
        // Main content panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        
        // Table setup
        String[] columnNames = {"Date", "Daily Revenue ($)", "Day of Week"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        revenueTable = new JTable(tableModel);
        revenueTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        revenueTable.getTableHeader().setReorderingAllowed(false);
        
        // Set column widths
        revenueTable.getColumnModel().getColumn(0).setPreferredWidth(100);
        revenueTable.getColumnModel().getColumn(1).setPreferredWidth(120);
        revenueTable.getColumnModel().getColumn(2).setPreferredWidth(100);
        
        JScrollPane scrollPane = new JScrollPane(revenueTable);
        scrollPane.setPreferredSize(new Dimension(600, 300));
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        
        // Summary panel
        JPanel summaryPanel = new JPanel(new BorderLayout());
        summaryPanel.setBorder(BorderFactory.createTitledBorder("Summary"));
        
        totalRevenueLabel = new JLabel("Total Revenue: $0.00");
        totalRevenueLabel.setFont(new Font("Arial", Font.BOLD, 16));
        totalRevenueLabel.setForeground(new Color(39, 174, 96));
        totalRevenueLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        summaryArea = new JTextArea(4, 50);
        summaryArea.setEditable(false);
        summaryArea.setBackground(getBackground());
        summaryArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        summaryPanel.add(totalRevenueLabel, BorderLayout.NORTH);
        summaryPanel.add(new JScrollPane(summaryArea), BorderLayout.CENTER);
        
        mainPanel.add(summaryPanel, BorderLayout.SOUTH);
        add(mainPanel, BorderLayout.CENTER);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout());
        closeButton = new JButton("Close");
        closeButton.addActionListener(e -> dispose());
        buttonPanel.add(closeButton);
        
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    private void setCurrentMonth() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, 1);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        startDateField.setText(sdf.format(cal.getTime()));
        
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        endDateField.setText(sdf.format(cal.getTime()));
    }
    
    private void setLastMonth() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, -1);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        startDateField.setText(sdf.format(cal.getTime()));
        
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        endDateField.setText(sdf.format(cal.getTime()));
    }
    
    private void loadCurrentMonthData() {
        setCurrentMonth();
        generateReport();
    }
    
    private void generateReport() {
        String startDateStr = startDateField.getText().trim();
        String endDateStr = endDateField.getText().trim();
        
        if (startDateStr.isEmpty() || endDateStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter both start and end dates!", 
                "Missing Dates", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            Date startDate = sdf.parse(startDateStr);
            Date endDate = sdf.parse(endDateStr);
            
            if (startDate.after(endDate)) {
                JOptionPane.showMessageDialog(this, "Start date cannot be after end date!", 
                    "Invalid Date Range", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            Map<String, Double> revenueData = FileManager.getRevenueInRange(startDateStr, endDateStr);
            displayRevenueData(revenueData, startDate, endDate);
            
        } catch (ParseException e) {
            JOptionPane.showMessageDialog(this, "Invalid date format! Please use dd/MM/yyyy format.", 
                "Invalid Date Format", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void displayRevenueData(Map<String, Double> revenueData, Date startDate, Date endDate) {
        // Clear existing data
        tableModel.setRowCount(0);
        
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE", new Locale("en"));
        
        double totalRevenue = 0.0;
        int daysWithSales = 0;
        double maxDailyRevenue = 0.0;
        String maxRevenueDate = "";
        
        // Generate all dates in range
        Calendar cal = Calendar.getInstance();
        cal.setTime(startDate);
        
        List<String[]> tableData = new ArrayList<>();
        
        while (!cal.getTime().after(endDate)) {
            String dateStr = sdf.format(cal.getTime());
            String dayOfWeek = dayFormat.format(cal.getTime());
            
            double dailyRevenue = revenueData.getOrDefault(dateStr, 0.0);
            totalRevenue += dailyRevenue;
            
            if (dailyRevenue > 0) {
                daysWithSales++;
                if (dailyRevenue > maxDailyRevenue) {
                    maxDailyRevenue = dailyRevenue;
                    maxRevenueDate = dateStr;
                }
            }
            
            String[] row = {
                dateStr,
                String.format("%.2f", dailyRevenue),
                dayOfWeek
            };
            tableData.add(row);
            
            cal.add(Calendar.DAY_OF_MONTH, 1);
        }
        
        // Sort by date (newest first)
        tableData.sort((a, b) -> {
            try {
                Date date1 = sdf.parse(a[0]);
                Date date2 = sdf.parse(b[0]);
                return date2.compareTo(date1);
            } catch (ParseException e) {
                return 0;
            }
        });
        
        // Add to table
        for (String[] row : tableData) {
            tableModel.addRow(row);
        }
        
        // Update total revenue label
        totalRevenueLabel.setText("Total Revenue: $" + String.format("%.2f", totalRevenue));
        
        // Update summary
        updateSummary(totalRevenue, daysWithSales, maxDailyRevenue, maxRevenueDate, startDate, endDate);
    }
    
    private void updateSummary(double totalRevenue, int daysWithSales, double maxDailyRevenue, 
                              String maxRevenueDate, Date startDate, Date endDate) {
        StringBuilder summary = new StringBuilder();
        
        // Calculate total days in range
        long diffInMillies = Math.abs(endDate.getTime() - startDate.getTime());
        long totalDays = diffInMillies / (24 * 60 * 60 * 1000) + 1;
        
        double averageDaily = daysWithSales > 0 ? totalRevenue / daysWithSales : 0.0;
        
        summary.append("Report Summary:\n");
        summary.append("• Total Days in Range: ").append(totalDays).append("\n");
        summary.append("• Days with Sales: ").append(daysWithSales).append("\n");
        summary.append("• Days without Sales: ").append(totalDays - daysWithSales).append("\n");
        summary.append("• Average Daily Revenue: $").append(String.format("%.2f", averageDaily)).append("\n");
        
        if (maxDailyRevenue > 0) {
            summary.append("• Highest Daily Revenue: $").append(String.format("%.2f", maxDailyRevenue))
                   .append(" on ").append(maxRevenueDate);
        } else {
            summary.append("• No sales recorded in this period");
        }
        
        summaryArea.setText(summary.toString());
    }
}