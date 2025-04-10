package hexa.org.entity;

import java.util.Date;

public class Order {
	private int orderId;
	private int customerId;
	private Date orderDate;
	private double totalPrice;
	private String shippingAddress;
	
	public Order() {
		super();
	}

	public Order(int orderId, int customerId, Date orderDate, double totalPrice, String shippingAddress) {
		super();
		this.orderId = orderId;
		this.customerId = customerId;
		this.orderDate = orderDate;
		this.totalPrice = totalPrice;
		this.shippingAddress = shippingAddress;
	}

	public int getOrderId() {
		return orderId;
	}

	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}

	public int getCustomerId() {
		return customerId;
	}

	public void setCustomerId(int customerId) {
		this.customerId = customerId;
	}

	public Date getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(Date orderDate) {
		this.orderDate = orderDate;
	}

	public double getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(double totalPrice) {
		this.totalPrice = totalPrice;
	}

	public String getShippingAddress() {
		return shippingAddress;
	}

	public void setShippingAddress(String shippingAddress) {
		this.shippingAddress = shippingAddress;
	}
@Override
	public String toString() {
		return "Order [orderId=" + orderId + ", customerId=" + customerId + ", orderDate=" + orderDate + ", totalPrice="
				+ totalPrice + ", shippingAddress=" + shippingAddress + "]";
	}
	

}