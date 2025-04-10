package hexa.org.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import hexa.org.entity.Customer;
import hexa.org.entity.Product;
import hexa.org.exception.CustomerNotFoundException;
import hexa.org.exception.OrderNotFoundException;
import hexa.org.exception.ProductNotFoundException;
import hexa.org.util.DBConnection;

public class OrderProcessorRepositoryImpl implements OrderProcessorRepository {

	public OrderProcessorRepositoryImpl() {
		super();
	}

	@Override
	public boolean createProduct(Product product) {
		boolean flag=false;
		try {
			Connection con = DBConnection.getConnection();
			PreparedStatement ps = con.prepareStatement("INSERT INTO products (name, price, description, stockQuantity) VALUES (?, ?, ?, ?)");
			ps.setString(1, product.getName());
			ps.setDouble(2, product.getPrice());
			ps.setString(3, product.getDescription());
			ps.setInt(4, product.getStockQuantity());
			
			ps.executeUpdate();
			flag=true;
		}catch(SQLException se) {
			System.out.println("Error While Inserting Product...");
			se.printStackTrace();
		}
		return flag;
	}

	@Override
	public boolean createCustomer(Customer customer) {
		boolean flag = false;
		try {
			Connection con=DBConnection.getConnection();
			PreparedStatement ps = con.prepareStatement("INSERT INTO customers (name, email, password) VALUES (?, ?, ?)");
			ps.setString(1, customer.getName());
			ps.setString(2, customer.getEmail());
			ps.setString(3, customer.getPassword());
			
			ps.executeUpdate();
			flag=true;
			}catch(SQLException se) {
				System.out.println("Error While Inserting Customer...");
				se.printStackTrace();
			}
		return flag;
	}

	@Override
	public boolean deleteProduct(int productId) throws ProductNotFoundException {
	    boolean flag = false;
	    try {
	        Connection con = DBConnection.getConnection();

	        PreparedStatement check = con.prepareStatement("select * from products where product_id = ?");
	        check.setInt(1, productId);
	        ResultSet rs = check.executeQuery();
	        if (!rs.next()) {
	            throw new ProductNotFoundException("Product not found...");
	        }

	        PreparedStatement ps = con.prepareStatement("delete from products where product_id = ?");
	        ps.setInt(1, productId);
	        int rows = ps.executeUpdate();
	        if (rows > 0) {
	            flag = true;
	        }
	    } catch (SQLException se) {
	        System.out.println("Error while deleting product...");
	        se.printStackTrace();
	    }
	    return flag;
	}


	@Override
	public boolean deleteCustomer(int customerId) throws CustomerNotFoundException {
	    boolean flag = false;
	    try {
	        Connection con = DBConnection.getConnection();

	        PreparedStatement check = con.prepareStatement("select * from customers where customer_id = ?");
	        check.setInt(1, customerId);
	        ResultSet rs = check.executeQuery();
	        if (!rs.next()) {
	            throw new CustomerNotFoundException("Customer not found...");
	        }

	        PreparedStatement ps = con.prepareStatement("delete from customers where customer_id = ?");
	        ps.setInt(1, customerId);
	        int rows = ps.executeUpdate();
	        if (rows > 0) {
	            flag = true;
	        }
	    } catch (SQLException se) {
	        System.out.println("Error while deleting customer...");
	        se.printStackTrace();
	    }
	    return flag;
	}


	@Override
	public boolean addToCart(Customer customer, Product product, int quantity) {
		boolean flag=false;
		try {
			Connection con=DBConnection.getConnection();
			PreparedStatement ps = con.prepareStatement("INSERT INTO cart (customer_id, product_id, quantity) VALUES (?, ?, ?)");
			ps.setInt(1,customer.getCustomerId());
			ps.setInt(2, product.getProductId());
			ps.setInt(3, quantity);
			ps.executeUpdate();
			flag=true;
		}catch(SQLException se) {
			System.out.println("Error While Adding To Cart...");
			se.printStackTrace();
		}
		return flag;
	}

	@Override
	public boolean removeFromCart(Customer customer, Product product) {
		boolean flag=false;
		try {
			Connection con=DBConnection.getConnection();
			PreparedStatement ps=con.prepareStatement("DELETE FROM cart WHERE customer_id=? AND product_id=?");
			ps.setInt(1, customer.getCustomerId());
			ps.setInt(2, product.getProductId());
			int rows = ps.executeUpdate();
			if (rows > 0) {
				flag = true;
			}
		}catch(SQLException se) {
			System.out.println("Error While Removing From Cart...");
			se.printStackTrace();
		}
		return flag;
	}

	@Override
	public List<Product> getAllFromCart(Customer customer) throws CustomerNotFoundException {
		List<Product> productList = new ArrayList<>();
		try {
		Connection con=DBConnection.getConnection();
		
		PreparedStatement check = con.prepareStatement("select * from customers where customer_id = ?");
		check.setInt(1, customer.getCustomerId());
		ResultSet rs = check.executeQuery();
		if (!rs.next()) {
		    throw new CustomerNotFoundException("Customer not found...");
		}

		PreparedStatement ps=con.prepareStatement("select p.* from products p join cart c on p.product_id=c.product_id where c.customer_id=?");
		ps.setInt(1,customer.getCustomerId());
		ResultSet cartrs=ps.executeQuery();
		while(cartrs.next()) {
			Product cartItem =new Product();
			cartItem.setProductId(cartrs.getInt("product_id"));
			cartItem.setName(cartrs.getString("name"));
			cartItem.setPrice(cartrs.getDouble("price"));
			cartItem.setDescription(cartrs.getString("description"));
			cartItem.setStockQuantity(cartrs.getInt("stockQuantity"));
			productList.add(cartItem);
			}
		}catch (SQLException se) {
	        System.out.println("Error While Fetching Cart Items...");
	        se.printStackTrace();
	        }
		return productList;
		}
	

	@Override
	public boolean placeOrder(Customer customer, List<Map<Product, Integer>> productList, String shippingAddress) throws CustomerNotFoundException, ProductNotFoundException {

	    boolean flag = false;
	    double totalPrice = 0.0;
	    Date orderDate = new Date(System.currentTimeMillis());

	    try {
	        Connection con = DBConnection.getConnection();

	        PreparedStatement checkCustomer = con.prepareStatement("select * from customers where customer_id = ?");
	        checkCustomer.setInt(1, customer.getCustomerId());
	        ResultSet rc = checkCustomer.executeQuery();
	        if (!rc.next()) {
	            throw new CustomerNotFoundException("Customer not found.");
	        }

	        for (Map<Product, Integer> map : productList) {
	            for (Map.Entry<Product, Integer> entry : map.entrySet()) {
	                Product p = entry.getKey();
	                int quantity = entry.getValue();
	                totalPrice += p.getPrice() * quantity;

	                PreparedStatement checkProduct = con.prepareStatement("select * from products where product_id = ?");
	                checkProduct.setInt(1, p.getProductId());
	                ResultSet rp = checkProduct.executeQuery();
	                if (!rp.next()) {
	                    throw new ProductNotFoundException("Product with ID " + p.getProductId() + " not found.");
	                }
	            }
	        }

	        PreparedStatement ps = con.prepareStatement("insert into orders (customer_id, order_date, total_price, shipping_address) values (?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
	        ps.setInt(1, customer.getCustomerId());
	        ps.setDate(2, orderDate);
	        ps.setDouble(3, totalPrice);
	        ps.setString(4, shippingAddress);
	        ps.executeUpdate();

	        ResultSet rs = ps.getGeneratedKeys();
	        int orderId = 0;
	        if (rs.next()) {
	            orderId = rs.getInt(1);
	        }

	        PreparedStatement pstmt = con.prepareStatement("insert into order_items (order_id, product_id, quantity) values (?, ?, ?)");
	        for (Map<Product, Integer> map : productList) {
	            for (Map.Entry<Product, Integer> entry : map.entrySet()) {
	                Product p = entry.getKey();
	                int quantity = entry.getValue();

	                pstmt.setInt(1, orderId);
	                pstmt.setInt(2, p.getProductId());
	                pstmt.setInt(3, quantity);

	                pstmt.executeUpdate();
	            }
	        }

	        PreparedStatement clearCart = con.prepareStatement("delete from cart where customer_id = ?");
	        clearCart.setInt(1, customer.getCustomerId());
	        clearCart.executeUpdate();

	        flag = true;

	    } catch (SQLException se) {
	        System.out.println("Error while placing order...");
	        se.printStackTrace();
	    }

	    return flag;
	}


	
	@Override
	public List<Map<Product, Integer>> getOrdersByCustomer(int customerId) throws CustomerNotFoundException, OrderNotFoundException {
	    List<Map<Product, Integer>> orderList = new ArrayList<>();

	    try {
	        Connection con = DBConnection.getConnection();
	        
	        PreparedStatement checkCustomer = con.prepareStatement("select * from customers where customer_id = ?");
	        checkCustomer.setInt(1, customerId);
	        ResultSet rc = checkCustomer.executeQuery();
	        if (!rc.next()) {
	            throw new CustomerNotFoundException("Customer not found...");
	        }

	        PreparedStatement orderStmt = con.prepareStatement("select order_id from orders where customer_id = ?");
	        orderStmt.setInt(1, customerId);
	        ResultSet orderRs = orderStmt.executeQuery();
	        
	        if (!orderRs.isBeforeFirst()) {
	            throw new OrderNotFoundException("No orders found for customer...");
	        }

	        while (orderRs.next()) {
	            int orderId = orderRs.getInt("order_id");

	            PreparedStatement itemStmt = con.prepareStatement("select product_id, quantity from order_items where order_id = ?");
	            itemStmt.setInt(1, orderId);
	            ResultSet itemRs = itemStmt.executeQuery();

	            Map<Product, Integer> orderMap = new HashMap<>();

	            while (itemRs.next()) {
	                int productId = itemRs.getInt("product_id");
	                int quantity = itemRs.getInt("quantity");

	                PreparedStatement productStmt = con.prepareStatement("select * from products where product_id = ?");
	                productStmt.setInt(1, productId);
	                ResultSet productRs = productStmt.executeQuery();

	                if (productRs.next()) {
	                    Product product = new Product();
	                    product.setProductId(productRs.getInt("product_id"));
	                    product.setName(productRs.getString("name"));
	                    product.setPrice(productRs.getDouble("price"));
	                    product.setDescription(productRs.getString("description"));
	                    product.setStockQuantity(productRs.getInt("stockQuantity"));

	                    orderMap.put(product, quantity);
	                }
	            }

	            orderList.add(orderMap);
	        }

	    } catch (SQLException se) {
	        System.out.println("Error fetching orders...");
	        se.printStackTrace();
	    }

	    return orderList;
	}
}