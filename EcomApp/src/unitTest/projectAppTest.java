package unitTest;

import org.junit.jupiter.api.Test;

import hexa.org.entity.Customer;
import hexa.org.entity.Product;
import hexa.org.exception.CustomerNotFoundException;
import hexa.org.exception.ProductNotFoundException;
import hexa.org.dao.OrderProcessorRepository;
import hexa.org.dao.OrderProcessorRepositoryImpl;
import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class projectAppTest {
	@Test
	void testProductCreation() {
	    Product product = new Product();
	    product.setName("Laptop");
	    product.setPrice(1000.0);
	    product.setDescription("High-performance laptop");
	    product.setStockQuantity(10);

	    OrderProcessorRepository o = new OrderProcessorRepositoryImpl();
	    boolean result = o.createProduct(product);  

	    assertTrue(result);  
	}
	
	@Test
    void testAddProductToCart() {
        OrderProcessorRepository orderProcessor = new OrderProcessorRepositoryImpl();

        Customer customer = new Customer();
        customer.setCustomerId(1); 

        Product product = new Product();
        product.setProductId(2);  
        product.setName("Laptop");
        product.setPrice(1000.0);
        product.setDescription("High-performance laptop");
        product.setStockQuantity(10);

        boolean isAdded = orderProcessor.addToCart(customer, product, 2);
        assertTrue(isAdded, "Product should be added to the cart successfully.");
    }
	
	@Test
    void testPlaceOrder() throws CustomerNotFoundException, ProductNotFoundException {
        OrderProcessorRepository orderProcessor = new OrderProcessorRepositoryImpl();

        Customer customer = new Customer();
        customer.setCustomerId(1);  

        Product product = new Product();
        product.setProductId(2); 
        product.setName("Laptop");
        product.setPrice(1000.0);
        product.setDescription("High-performance laptop");
        product.setStockQuantity(10);

        Map<Product, Integer> orderProducts = new HashMap<>();
        orderProducts.put(product, 2); 

        boolean isOrderPlaced = orderProcessor.placeOrder(customer, List.of(orderProducts), "123 Main Street");
        assertTrue(isOrderPlaced, "Order should be placed successfully.");
    }
	
	@Test
    void testCustomerNotFoundException() {
        OrderProcessorRepository orderProcessor = new OrderProcessorRepositoryImpl();

        Customer customer = new Customer();
        customer.setCustomerId(9999); 

        try {
            orderProcessor.getAllFromCart(customer);
            fail("Expected CustomerNotFoundException to be thrown");
        } catch (CustomerNotFoundException e) {
            System.out.println("Exception thrown as expected: " + e.getMessage());
        }
    }
	
}
