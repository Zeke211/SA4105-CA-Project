package sg.iss.javaspring.ca.checkout.service;

import java.util.List;

import sg.iss.javaspring.ca.checkout.model.CartItem;
import sg.iss.javaspring.ca.checkout.model.Customer;
import sg.iss.javaspring.ca.checkout.model.Order;
import sg.iss.javaspring.ca.checkout.model.OrderItem;
import sg.iss.javaspring.ca.checkout.model.ShoppingCart;

public interface CheckoutService {
    public List<CartItem> findAllCartItems();

    public OrderItem createOrderItem(CartItem cartItem, Order order);

    public Order placeOrder(ShoppingCart shoppingCart, Customer customer);

}
