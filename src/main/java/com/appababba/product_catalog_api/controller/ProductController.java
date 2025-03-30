package com.appababba.product_catalog_api.controller; // Controller package

import com.appababba.product_catalog_api.model.Product;       // Import Product data model
import com.appababba.product_catalog_api.service.ProductService; // Import Product service layer
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*; // Includes @RestController, @RequestMapping, @GetMapping, etc.

import java.util.List;

@RestController // Marks this class to handle REST API requests, returning JSON by default.
@RequestMapping("/api/v1/products") // Sets the base URL path for all methods in this controller.
public class ProductController {

    private final ProductService productService; // Service dependency

    // Injecting the ProductService using constructor injection (recommended)
    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    // GET /api/v1/products - Fetches all products
    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        List<Product> products = productService.getAllProducts();
        // Return the list with HTTP 200 OK status
        return ResponseEntity.ok(products);
    }

    // GET /api/v1/products/{id} - Fetches a single product by its ID
    @GetMapping("/{id}")
    // @PathVariable grabs the 'id' from the URL path
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        // Ask service for product (returns Optional)
        return productService.getProductById(id)
                .map(ResponseEntity::ok) // If found, wrap product in 200 OK response (method reference)
                .orElse(ResponseEntity.notFound().build()); // If not found, return 404 Not Found
    }

    // POST /api/v1/products - Creates a new product
    @PostMapping
    // @RequestBody takes the JSON from the request body and converts it to a Product object
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        Product createdProduct = productService.createProduct(product);
        // Return HTTP 201 Created status, including the newly created product (with ID) in the body
        return ResponseEntity.status(HttpStatus.CREATED).body(createdProduct);
    }

    // PUT /api/v1/products/{id} - Updates an existing product
    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody Product productDetails) {
        // Attempt to update the product via the service (returns Optional)
        return productService.updateProduct(id, productDetails)
                .map(ResponseEntity::ok) // If updated, return 200 OK with the updated product
                .orElse(ResponseEntity.notFound().build()); // If product ID not found, return 404
    }

    // DELETE /api/v1/products/{id} - Deletes a product by its ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        // Ask the service to delete the product
        boolean deleted = productService.deleteProduct(id);
        if (deleted) {
            // If successful, return HTTP 204 No Content (standard success response for DELETE)
            return ResponseEntity.noContent().build();
        } else {
            // If the product ID wasn't found, return 404 Not Found
            return ResponseEntity.notFound().build();
        }
    }
}