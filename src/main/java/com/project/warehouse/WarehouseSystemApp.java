package com.project.warehouse;

import com.project.warehouse.entity.Product;
import com.project.warehouse.service.ProductService;
import org.hibernate.internal.util.StringHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import javax.swing.*;
import java.awt.*;
import java.util.List;

@SpringBootApplication
@ComponentScan(basePackages = {"com.project.warehouse"})
public class WarehouseSystemApp extends JFrame {

    private final ProductService productService;
    private JTextArea outputTextArea;
    private JTextField nameTextField;
    private JTextField priceTextField;

    @Autowired
    public WarehouseSystemApp(ProductService productService) {
        this.productService = productService;
        setTitle("Warehouse System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(950, 800);

        initializeComponents();
        displayProducts();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void initializeComponents() {
        outputTextArea = new JTextArea();
        outputTextArea.setEditable(false);
        outputTextArea.setTabSize(3);

        nameTextField = new JTextField();
        priceTextField = new JTextField();

        JButton refreshButton = new JButton("Refresh Products");
        JButton addButton = new JButton("Add Product");
        JButton updateButton = new JButton("Update Product");
        JButton searchButton = new JButton("Search Product");

        System.out.println("addButton: " + addButton);
        System.out.println("updateButton: " + updateButton);


        JButton deleteButton = new JButton("Delete Product");

        refreshButton.addActionListener(e -> displayProducts());
        addButton.addActionListener(e -> addProduct());
        updateButton.addActionListener(e -> updateProduct());
        deleteButton.addActionListener(e -> deleteProduct());
        searchButton.addActionListener(e -> searchProduct());

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
        inputPanel.add(searchButton);
        inputPanel.add(deleteButton);

        mainPanel.add(inputPanel, BorderLayout.SOUTH);
        nameTextField.setPreferredSize(new Dimension(100, 25));
        priceTextField.setPreferredSize(new Dimension(100, 25));
        addButton.setPreferredSize(new Dimension(120, 25));
        updateButton.setPreferredSize(new Dimension(120, 25));
        searchButton.setPreferredSize(new Dimension(120, 25));
        deleteButton.setPreferredSize(new Dimension(120, 25));


        getContentPane().add(mainPanel);
    }

    private void displayProducts() {
        outputTextArea.setText("");
        List<Product> products = productService.getAllProducts();
        setOutputTextAreaWithProductList(products);

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

    private void searchProduct() {

        String productName = JOptionPane.showInputDialog("Enter the Product Name to search:");
        List<Product> existingProduct = productService.findByName(productName);

        outputTextArea.setText("");

        setOutputTextAreaWithProductList(existingProduct);
    }

    public void setOutputTextAreaWithProductList(List<Product> productList) {
        outputTextArea.append("ID\tName\t\tPrice\n");
        for (Product product : productList) {
            outputTextArea.append(product.getId() + "\t" + product.getName() + "\t\t" + product.getQuantity() + "\n");
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
