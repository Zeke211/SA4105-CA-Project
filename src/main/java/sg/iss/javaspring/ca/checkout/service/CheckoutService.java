package sg.iss.javaspring.ca.checkout.service;

import java.util.List;
import java.util.Optional;

import sg.iss.javaspring.ca.checkout.model.CartItem;
import sg.iss.javaspring.ca.checkout.model.Customer;
import sg.iss.javaspring.ca.checkout.model.DiscountCode;
import sg.iss.javaspring.ca.checkout.model.Order;
import sg.iss.javaspring.ca.checkout.model.OrderItem;
import sg.iss.javaspring.ca.checkout.model.PaymentMethod;
import sg.iss.javaspring.ca.checkout.model.ShoppingCart;

public interface CheckoutService {
    public List<CartItem> findAllCartItems();

    public OrderItem createOrderItem(CartItem cartItem);

    // public Order placeOrder(ShoppingCart shoppingCart, Customer customer);

    public Optional<Order> findOrderById(Integer id);

    public void deleteCartItem(CartItem cartItem);

    public void deleteAllCartItems(List<CartItem> cartItems);

    public void savePaymentMethod(PaymentMethod paymentMethod);

    public DiscountCode findDiscountCodeByCode(String discountCode);

    public List<Double> eachCartItemTotal(List<CartItem> cartItems);

    public double cartTotal(List<Double> eachCartItemTotal);
}
