package com.appababba.product_catalog_api.repository; // Repository package

import com.appababba.product_catalog_api.model.Product; // The Product entity
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository // Marks this as a Spring Data JPA repository bean
public interface ProductRepository extends JpaRepository<Product, Long> {
    // JpaRepository handles standard CRUD for Product (entity) using Long (ID type).
    // It gives us methods like findAll(), findById(), save(), deleteById() automatically.

    // We can add custom find methods later if needed, like:
    // List<Product> findByNameContainingIgnoreCase(String keyword);
    // Spring Data JPA implements these based on the method name.
}