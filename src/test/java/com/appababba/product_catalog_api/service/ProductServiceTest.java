package com.appababba.product_catalog_api.service; // Package declaration

import com.appababba.product_catalog_api.model.Product;
import com.appababba.product_catalog_api.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach; // Setup before each test
import org.junit.jupiter.api.Test; // Marks a test method
import org.junit.jupiter.api.extension.ExtendWith; // Use JUnit extensions (like Mockito)
import org.mockito.InjectMocks; // Auto-inject mocks into the test subject
import org.mockito.Mock; // Create a mock object
import org.mockito.junit.jupiter.MockitoExtension; // Initialize Mockito for JUnit 5

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat; // Using AssertJ for assertions
// import static org.junit.jupiter.api.Assertions.*; // Alternative: JUnit assertions
import static org.mockito.ArgumentMatchers.any; // Matcher for any object
import static org.mockito.ArgumentMatchers.anyLong; // Matcher for any long
import static org.mockito.BDDMockito.given; // BDD style for setting up mocks
import static org.mockito.BDDMockito.then; // BDD style for verifying mocks
import static org.mockito.Mockito.*; // Include core Mockito static methods


@ExtendWith(MockitoExtension.class) // Enable Mockito
class ProductServiceTest {

    @Mock // Mock the repository dependency
    private ProductRepository productRepository;

    @InjectMocks // Create ProductService instance and inject the mock repository
    private ProductService productService;

    private Product product1;
    private Product product2;

    // Runs before each @Test method
    @BeforeEach
    void setUp() {
        // Create some sample product data for tests
        product1 = new Product(1L, "Test Product 1", "Description 1", 10.99);
        product2 = new Product(2L, "Test Product 2", "Description 2", 25.50);
    }

    @Test
    void getAllProducts_shouldReturnListOfProducts() {
        // Arrange: Define what the mock repository should return
        given(productRepository.findAll()).willReturn(Arrays.asList(product1, product2));

        // Act: Call the service method we're testing
        List<Product> products = productService.getAllProducts();

        // Assert: Check if the results are correct
        assertThat(products).isNotNull();
        assertThat(products).hasSize(2);
        assertThat(products).containsExactly(product1, product2);
        // Verify repository's findAll was called exactly once
        then(productRepository).should(times(1)).findAll();
    }

    @Test
    void getProductById_whenProductExists_shouldReturnProduct() {
        // Arrange
        given(productRepository.findById(1L)).willReturn(Optional.of(product1));

        // Act
        Optional<Product> foundProductOpt = productService.getProductById(1L);

        // Assert
        assertThat(foundProductOpt).isPresent();
        assertThat(foundProductOpt.get()).isEqualTo(product1);
        then(productRepository).should(times(1)).findById(1L); // Check findById call
    }

    @Test
    void getProductById_whenProductDoesNotExist_shouldReturnEmpty() {
        // Arrange
        given(productRepository.findById(99L)).willReturn(Optional.empty());

        // Act
        Optional<Product> foundProductOpt = productService.getProductById(99L);

        // Assert
        assertThat(foundProductOpt).isNotPresent(); // Check it's empty
        then(productRepository).should(times(1)).findById(99L);
    }

    @Test
    void createProduct_shouldReturnSavedProduct() {
        // Arrange
        Product productToSave = new Product(null, "New Prod", "New Desc", 5.00); // New product, no ID yet
        Product expectedSavedProduct = new Product(3L, "New Prod", "New Desc", 5.00); // What we expect back (with ID)
        given(productRepository.save(any(Product.class))).willReturn(expectedSavedProduct);

        // Act
        Product actualSavedProduct = productService.createProduct(productToSave);

        // Assert
        assertThat(actualSavedProduct).isNotNull();
        assertThat(actualSavedProduct.getId()).isEqualTo(3L); // Check ID was assigned
        assertThat(actualSavedProduct.getName()).isEqualTo("New Prod");
        then(productRepository).should(times(1)).save(productToSave); // Verify save was called
    }

    @Test
    void updateProduct_whenProductExists_shouldReturnUpdatedProduct() {
        // Arrange
        Long existingId = 1L;
        Product detailsToUpdate = new Product(null, "Updated Name", "Updated Desc", 99.99);
        // Mock finding the existing one
        given(productRepository.findById(existingId)).willReturn(Optional.of(product1));
        // Mock the save operation returning the updated product
        Product expectedUpdatedProduct = new Product(existingId, "Updated Name", "Updated Desc", 99.99);
        given(productRepository.save(any(Product.class))).willReturn(expectedUpdatedProduct);

        // Act
        Optional<Product> updatedProductOpt = productService.updateProduct(existingId, detailsToUpdate);

        // Assert
        assertThat(updatedProductOpt).isPresent();
        Product updatedProduct = updatedProductOpt.get();
        assertThat(updatedProduct.getId()).isEqualTo(existingId);
        assertThat(updatedProduct.getName()).isEqualTo("Updated Name");
        assertThat(updatedProduct.getPrice()).isEqualTo(99.99);

        then(productRepository).should(times(1)).findById(existingId);
        // Check that save was called with the correctly updated product details
        then(productRepository).should(times(1)).save(argThat(p ->
            p.getId().equals(existingId) && p.getName().equals("Updated Name")
        ));
    }

     @Test
    void updateProduct_whenProductDoesNotExist_shouldReturnEmpty() {
        // Arrange
        Long nonExistentId = 99L;
        Product detailsToUpdate = new Product(null, "Updated Name", "Updated Desc", 99.99);
        given(productRepository.findById(nonExistentId)).willReturn(Optional.empty()); // Mock finding nothing

        // Act
        Optional<Product> updatedProductOpt = productService.updateProduct(nonExistentId, detailsToUpdate);

        // Assert
        assertThat(updatedProductOpt).isNotPresent(); // Should be empty
        then(productRepository).should(times(1)).findById(nonExistentId);
        then(productRepository).should(never()).save(any(Product.class)); // Ensure save was NOT called
    }


    @Test
    void deleteProduct_whenProductExists_shouldReturnTrue() {
        // Arrange
        Long existingId = 1L;
        given(productRepository.findById(existingId)).willReturn(Optional.of(product1)); // Mock finding it
        // No need to mock void 'delete' method - Mockito does nothing by default

        // Act
        boolean deleted = productService.deleteProduct(existingId);

        // Assert
        assertThat(deleted).isTrue(); // Should return true on success
        then(productRepository).should(times(1)).findById(existingId);
        then(productRepository).should(times(1)).delete(product1); // Verify delete was called with the product
    }

    @Test
    void deleteProduct_whenProductDoesNotExist_shouldReturnFalse() {
        // Arrange
        Long nonExistentId = 99L;
         given(productRepository.findById(nonExistentId)).willReturn(Optional.empty()); // Mock finding nothing

        // Act
         boolean deleted = productService.deleteProduct(nonExistentId);

        // Assert
         assertThat(deleted).isFalse(); // Should return false if not found
         then(productRepository).should(times(1)).findById(nonExistentId);
         then(productRepository).should(never()).delete(any(Product.class)); // Verify delete was NOT called
    }
}