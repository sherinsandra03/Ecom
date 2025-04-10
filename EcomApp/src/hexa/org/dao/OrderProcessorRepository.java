package hexa.org.dao;

import java.util.List;
import java.util.Map;
import hexa.org.entity.Customer;
import hexa.org.entity.Product;
import hexa.org.exception.CustomerNotFoundException;
import hexa.org.exception.OrderNotFoundException;
import hexa.org.exception.ProductNotFoundException;

public interface OrderProcessorRepository {

    boolean createProduct(Product product);

    boolean createCustomer(Customer customer);

    boolean deleteProduct(int productId) throws ProductNotFoundException;

    boolean deleteCustomer(int customerId) throws CustomerNotFoundException;

    boolean addToCart(Customer customer, Product product, int quantity);

    boolean removeFromCart(Customer customer, Product product);

    List<Product> getAllFromCart(Customer customer) throws CustomerNotFoundException;

    boolean placeOrder(Customer customer, List<Map<Product, Integer>> productList, String shippingAddress) throws CustomerNotFoundException, ProductNotFoundException;

    List<Map<Product, Integer>> getOrdersByCustomer(int customerId) throws CustomerNotFoundException, OrderNotFoundException;
}