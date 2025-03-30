package com.appababba.product_catalog_api.service; // Service layer package

import com.appababba.product_catalog_api.model.Product; // Product model
import com.appababba.product_catalog_api.repository.ProductRepository; // Product JPA repository
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // For DB transactions

import java.util.List;
import java.util.Optional;

@Service // Defines this as a Spring Service bean
public class ProductService {

    // Repository dependency - final means it's required
    private final ProductRepository productRepository;

    // Constructor Injection (preferred way)
    @Autowired // Spring injects the repo bean here
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    // Get all products from the database
    @Transactional(readOnly = true) // Read-only transaction optimization
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    // Get a single product by its ID
    @Transactional(readOnly = true)
    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    // Create a new product
    @Transactional // Read-write transaction needed
    public Product createProduct(Product product) {
        // Optional: Add validation logic here before saving?
        return productRepository.save(product);
    }

    // Update an existing product by ID
    @Transactional
    public Optional<Product> updateProduct(Long id, Product productDetails) {
        // Find the product by ID
        return productRepository.findById(id)
                .map(existingProduct -> { // If found, update its fields
                    existingProduct.setName(productDetails.getName());
                    existingProduct.setDescription(productDetails.getDescription());
                    existingProduct.setPrice(productDetails.getPrice());
                    // Save the updated product and return it
                    return productRepository.save(existingProduct);
                }); // Returns empty Optional if findById didn't find anything
    }

    // Delete a product by ID
    @Transactional
    public boolean deleteProduct(Long id) {
        // Find the product by ID
        return productRepository.findById(id)
                .map(product -> { // If found, delete it
                    productRepository.delete(product);
                    return true; // Indicate success
                }).orElse(false); // If not found, return false
    }
}