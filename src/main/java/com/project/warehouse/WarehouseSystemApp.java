package com.project.warehouse;

import com.project.warehouse.entity.Product;
import com.project.warehouse.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.swing.*;
import java.awt.*;
import java.util.List;

@SpringBootApplication

public class WarehouseSystemApp extends JFrame {
    @Autowired
    private ProductService productService;
    private JTextArea outputTextArea;
    private JTextField nameTextField;
    private JTextField priceTextField;

    public WarehouseSystemApp() {

        setTitle("Warehouse System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(750, 400);

        initializeComponents();

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void initializeComponents() {
        outputTextArea = new JTextArea();
        outputTextArea.setEditable(false);

        nameTextField = new JTextField();
        priceTextField = new JTextField();

        JButton refreshButton = new JButton("Refresh Products");
        JButton addButton = new JButton("Add Product");
        JButton updateButton = new JButton("Update Product");

        System.out.println("addButton: " + addButton);
        System.out.println("updateButton: " + updateButton);


        JButton deleteButton = new JButton("Delete Product");

        refreshButton.addActionListener(e -> displayProducts());
        addButton.addActionListener(e -> addProduct());
        updateButton.addActionListener(e -> updateProduct());
        deleteButton.addActionListener(e -> deleteProduct());

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(refreshButton, BorderLayout.NORTH);
        mainPanel.add(new JScrollPane(outputTextArea), BorderLayout.CENTER);

        JPanel inputPanel = new JPanel(new FlowLayout());
        inputPanel.add(new JLabel("Name:"));
        inputPanel.add(nameTextField);
        inputPanel.add(new JLabel("Price:"));
        inputPanel.add(priceTextField);
        inputPanel.add(addButton);
        inputPanel.add(updateButton);
        inputPanel.add(deleteButton);

        mainPanel.add(inputPanel, BorderLayout.SOUTH);
        nameTextField.setPreferredSize(new Dimension(100, 25));
        priceTextField.setPreferredSize(new Dimension(100, 25));
        addButton.setPreferredSize(new Dimension(120, 25));
        updateButton.setPreferredSize(new Dimension(120, 25));
        deleteButton.setPreferredSize(new Dimension(120, 25));


        getContentPane().add(mainPanel);
    }

    private void displayProducts() {
        outputTextArea.setText("");
        List<Product> products = productService.getAllProducts();

        for (Product product : products) {
            outputTextArea.append("ID: " + product.getId() + "\tName: " + product.getName() + "\tPrice: " + product.getQuantity() + "\n");
        }
    }

    private void addProduct() {
        String name = nameTextField.getText();
        double price = Double.parseDouble(priceTextField.getText());

        Product newProduct = new Product();
        newProduct.setName(name);
        newProduct.setQuantity(price);

        productService.saveProduct(newProduct);

        displayProducts();
    }

    private void updateProduct() {
        // Assuming you have a selected product ID
        Long productId = Long.parseLong(JOptionPane.showInputDialog("Enter the Product ID to update:"));
        Product existingProduct = productService.getProductById(productId);

        if (existingProduct != null) {
            String newName = nameTextField.getText();
//            double newPrice = Double.parseDouble(priceTextField.getText());

            existingProduct.setName(newName);
//            existingProduct.setQuantity(newPrice);

            productService.saveProduct(existingProduct);

            displayProducts();
        } else {
            JOptionPane.showMessageDialog(this, "Product not found");
        }
    }

    private void deleteProduct() {
        // Assuming you have a selected product ID
        Long productId = Long.parseLong(JOptionPane.showInputDialog("Enter the Product ID to delete:"));

        productService.deleteProduct(productId);

        displayProducts();
    }

    public static void main(String[] args) {
        SpringApplication.run(WarehouseSystemApp.class, args);
    }
}
