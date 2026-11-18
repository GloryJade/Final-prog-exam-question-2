package za.co.mattressgui;

import javax.swing.*;
import java.awt.*;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class MattressSalesForm extends JFrame {

    private JTextField txtCustomerName;
    private JComboBox<String> cmbProduct;
    private JTextField txtPrice;
    private JTextArea txtOutput;
    private JButton btnProcess;

    public MattressSalesForm() {
        setTitle("Product Application");
        setSize(450, 420);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // 1. Menu Bar Setup (Q2.4)
        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("File");
        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.addActionListener(e -> System.exit(0));
        fileMenu.add(exitItem);

        JMenu toolsMenu = new JMenu("Tools");
        JMenuItem processItem = new JMenuItem("Process");
        JMenuItem clearItem = new JMenuItem("Clear");
        processItem.addActionListener(e -> processSale());
        clearItem.addActionListener(e -> clearFields());
        toolsMenu.add(processItem);
        toolsMenu.add(clearItem);

        menuBar.add(fileMenu);
        menuBar.add(toolsMenu);
        setJMenuBar(menuBar);

        // 2. Input Form Panel (Q2.2)
        JPanel inputPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 5, 15));

        inputPanel.add(new JLabel("Customer Name:"));
        txtCustomerName = new JTextField();
        inputPanel.add(txtCustomerName);

        inputPanel.add(new JLabel("Select Product:"));
        String[] products = {"Cozy Night Monarch", "Restonic Barbados", "Sealy Phuket"};
        cmbProduct = new JComboBox<>(products);
        inputPanel.add(cmbProduct);

        inputPanel.add(new JLabel("Product Price (R):"));
        txtPrice = new JTextField();
        inputPanel.add(txtPrice);

        inputPanel.add(new JLabel(""));
        btnProcess = new JButton("Process");
        btnProcess.addActionListener(e -> processSale());
        inputPanel.add(btnProcess);

        add(inputPanel, BorderLayout.NORTH);

        // 3. Display Area (Q2.1)
        txtOutput = new JTextArea();
        txtOutput.setEditable(false);
        txtOutput.setBorder(BorderFactory.createTitledBorder("Sales Details"));
        add(new JScrollPane(txtOutput), BorderLayout.CENTER);
    }

    // Q2.3 Process Logic (Display + Save to data.txt)
    private void processSale() {
        String customerName = txtCustomerName.getText().trim();
        String productName = (String) cmbProduct.getSelectedItem();
        String priceText = txtPrice.getText().trim();

        if (customerName.isEmpty() || priceText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter both Customer Name and Price.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            double price = Double.parseDouble(priceText);
            ProductCalculations calc = new ProductCalculations(price);

            double vat = calc.GetVat();
            double commission = calc.GetCommision();
            double total = calc.GetTotal();

            String receipt = "****************************************\n"
                    + "CUSTOMER NAME: " + customerName + "\n"
                    + "PRODUCT NAME: " + productName + "\n"
                    + String.format("PRODUCT PRICE: R %.2f\n", price)
                    + String.format("VAT (15%%): R %.2f\n", vat)
                    + String.format("COMMISSION (5%%): R %.2f\n", commission)
                    + String.format("TOTAL SALE AMOUNT: R %.2f\n", total)
                    + "****************************************\n";

            // Display in text area & popup message
            txtOutput.setText(receipt);
            JOptionPane.showMessageDialog(this, receipt, "Product Sale Details", JOptionPane.INFORMATION_MESSAGE);

            // Save to data.txt
            saveToFile(receipt);

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid numeric price.", "Invalid Price", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void saveToFile(String content) {
        try (PrintWriter writer = new PrintWriter(new FileWriter("data.txt", true))) {
            writer.println(content);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error saving to data.txt: " + e.getMessage(), "File Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Q2.4 Clear Logic
    private void clearFields() {
        txtCustomerName.setText("");
        txtPrice.setText("");
        cmbProduct.setSelectedIndex(0);
        txtOutput.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new MattressSalesForm().setVisible(true);
        });
    }
}
