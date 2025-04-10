package hexa.org.main;
import java.util.*;
import hexa.org.dao.*;
import hexa.org.entity.*;
import hexa.org.exception.*;


public class EcomApp {
	 public static void main(String[] args) {
		 Scanner sc = new Scanner(System.in);
		 OrderProcessorRepository o = new OrderProcessorRepositoryImpl();
		 
		 while (true) {
	            System.out.println("\n.......... E-Commerce System..........");
	            System.out.println("1. Register Customer");
	            System.out.println("2. Create Product");
	            System.out.println("3. Delete Product");
	            System.out.println("4. Add to Cart");
	            System.out.println("5. View Cart");
	            System.out.println("6. Place Order");
	            System.out.println("7. View Customer Orders");
	            System.out.println("0. Exit");
	            System.out.print("Enter your option: ");

	            int option = sc.nextInt();
	            sc.nextLine(); 

	            switch (option) {
	                case 1:
	                    System.out.println("\n Register Customer");
	                    System.out.println("Enter name: ");
	                    String name = sc.nextLine();
	                    System.out.println("Enter email: ");
	                    String email = sc.nextLine();
	                    System.out.println("Enter password: ");
	                    String password = sc.nextLine();
	                    
	                    Customer newCustomer = new Customer();
	                    newCustomer.setName(name);
	                    newCustomer.setEmail(email);
	                    newCustomer.setPassword(password);
	                    
	                    boolean isRegistered = o.createCustomer(newCustomer);
	                    
	                    if(isRegistered) {
	                    	System.out.println("Customer registered successfully!");
	                    }else {
	                    	System.out.println("Failed to register customer.");
	                    }
	                    break;
	                    
	                case 2:
	                    System.out.println("\n Create Product");

	                    System.out.print("Enter Product Name: ");
	                    String pname = sc.nextLine();
	                    System.out.print("Enter Price: ");
	                    double price = sc.nextDouble();
	                    sc.nextLine();
	                    System.out.print("Enter Description: ");
	                    String desc = sc.nextLine();
	                    System.out.print("Enter Stock Quantity: ");
	                    int stock = sc.nextInt();
	                    sc.nextLine();
	                    
	                    Product product = new Product();
	                    product.setName(pname);
	                    product.setPrice(price);
	                    product.setDescription(desc);
	                    product.setStockQuantity(stock);

	                    boolean isProductCreated = o.createProduct(product);

	                    if (isProductCreated) {
	                        System.out.println("Product added successfully!");
	                    } else {
	                        System.out.println("Failed to add product.");
	                    }
	                    break;

	                case 3:
	                    System.out.println("\n Delete Product");

	                    System.out.print("Enter Product ID to delete: ");
	                    int productIdToDelete = sc.nextInt();
	                    sc.nextLine();

	                    try {
	                        boolean isDeleted = o.deleteProduct(productIdToDelete);

	                        if (isDeleted) {
	                            System.out.println("Product deleted successfully!");
	                        } else {
	                            System.out.println("Failed to delete product.");
	                        }
	                    } catch (ProductNotFoundException e) {
	                        System.out.println(e.getMessage());
	                    }
	                    break;

	                case 4:
	                    System.out.println("\n Add to Cart");
	                    System.out.print("Enter Customer ID: ");
	                    int customerIdToAdd = sc.nextInt();
	                    System.out.print("Enter Product ID: ");
	                    int productIdToAdd = sc.nextInt();
	                    System.out.print("Enter Quantity: ");
	                    int quantityToAdd = sc.nextInt();
	                    sc.nextLine();

	                    Customer customerToAdd = new Customer();
	                    customerToAdd.setCustomerId(customerIdToAdd);

	                    Product productToAdd = new Product();
	                    productToAdd.setProductId(productIdToAdd);

	                    boolean isAdded = o.addToCart(customerToAdd, productToAdd, quantityToAdd);

	                    if (isAdded) {
	                        System.out.println("Product added to cart successfully!");
	                    } else {
	                        System.out.println("Failed to add product to cart.");
	                    }
	                    break;

	                case 5:
	                    System.out.println("\n View Cart");

	                    System.out.print("Enter Customer ID to view cart: ");
	                    int customerIdToView = sc.nextInt();
	                    sc.nextLine();

	                    Customer customerToView = new Customer();
	                    customerToView.setCustomerId(customerIdToView); 
	                    try {
	                        List<Product> cartProducts = o.getAllFromCart(customerToView);  

	                        if (cartProducts.isEmpty()) {
	                            System.out.println("Cart is empty.");
	                        } else {
	                            System.out.println("\nProducts in your Cart:");
	                            for (Product cartProduct : cartProducts) { 
	                                System.out.println("Product ID: " + cartProduct.getProductId() +
	                                                   ", Name: " + cartProduct.getName() +
	                                                   ", Price: " + cartProduct.getPrice() +
	                                                   ", Stock Quantity: " + cartProduct.getStockQuantity());
	                            }
	                        }
	                    } catch (CustomerNotFoundException e) {
	                        System.out.println(e.getMessage());
	                    }
	                    break;


	                case 6:
	                    System.out.println("\n Place Order");

	                    System.out.print("Enter Customer ID: ");
	                    int customerIdForOrder = sc.nextInt();
	                    sc.nextLine();

	                    Customer customerForOrder = new Customer();
	                    customerForOrder.setCustomerId(customerIdForOrder);

	                    List<Product> cartProductsForOrder = new ArrayList<>();
	                    try {
	                        cartProductsForOrder = o.getAllFromCart(customerForOrder);
	                    } catch (CustomerNotFoundException e) {
	                        System.out.println(e.getMessage());
	                        break;
	                    }

	                    if (cartProductsForOrder.isEmpty()) {
	                        System.out.println("Cart is empty, cannot place order.");
	                        break;
	                    }

	                    System.out.println("Products in your cart:");
	                    Map<Product, Integer> selectedProducts = new HashMap<>();
	                    for (Product orderProduct : cartProductsForOrder) {  
	                        System.out.println("Product ID: " + orderProduct.getProductId() +
	                                           ", Name: " + orderProduct.getName() +
	                                           ", Price: " + orderProduct.getPrice());
	                    }

	                    System.out.print("Enter Shipping Address: ");
	                    String shippingAddressForOrder = sc.nextLine();

	                    for (Product orderProduct : cartProductsForOrder) {  
	                        System.out.print("Enter quantity for " + orderProduct.getName() + ": ");
	                        int quantity = sc.nextInt();
	                        sc.nextLine(); 
	                        selectedProducts.put(orderProduct, quantity);
	                    }

	                    try {
	                        boolean isOrderPlaced = o.placeOrder(customerForOrder, List.of(selectedProducts), shippingAddressForOrder); 
	                        if (isOrderPlaced) {
	                            System.out.println("Order placed successfully!");
	                        } else {
	                            System.out.println("Failed to place order.");
	                        }
	                    } catch (CustomerNotFoundException | ProductNotFoundException e) {
	                        System.out.println(e.getMessage());
	                    }
	                    break;


	                    
	                case 7:
	                    System.out.println("\n View Customer Orders");

	                    System.out.print("Enter Customer ID: ");
	                    int customerIdForOrders = sc.nextInt();
	                    sc.nextLine();

	                    try {
	                        List<Map<Product, Integer>> customerOrders = o.getOrdersByCustomer(customerIdForOrders);

	                        if (customerOrders.isEmpty()) {
	                            System.out.println("No orders found for this customer.");
	                        } else {
	                            System.out.println("\nOrders for Customer ID: " + customerIdForOrders);
	                            for (Map<Product, Integer> order : customerOrders) {
	                                for (Map.Entry<Product, Integer> entry : order.entrySet()) {
	                                    Product product1 = entry.getKey();
	                                    int quantity = entry.getValue();
	                                    System.out.println("Product: " + product1.getName() + ", Quantity: " + quantity);
	                                }
	                            }
	                        }
	                    } catch (CustomerNotFoundException e) {
	                        System.out.println(e.getMessage());
	                    } catch (OrderNotFoundException e) {
	                        System.out.println(e.getMessage());
	                    }
	                    break;


	                case 0:
	                    System.out.println("Exited...");
	                    sc.close();
	                    return;
	                default:
	                    System.out.println("Invalid choice");
	            }
	        }
	    }
	 }