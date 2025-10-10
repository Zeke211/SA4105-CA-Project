package sg.iss.javaspring.ca.checkout.service;

import java.util.List;
import java.util.Optional;

import sg.iss.javaspring.ca.checkout.model.CartItem;
import sg.iss.javaspring.ca.checkout.model.CheckoutDTO;
import sg.iss.javaspring.ca.checkout.model.Customer;
import sg.iss.javaspring.ca.checkout.model.DiscountCode;
import sg.iss.javaspring.ca.checkout.model.Order;
import sg.iss.javaspring.ca.checkout.model.OrderItem;
import sg.iss.javaspring.ca.checkout.model.PaymentMethod;
import sg.iss.javaspring.ca.checkout.model.Shipment;
import sg.iss.javaspring.ca.checkout.model.ShoppingCart;

public interface CheckoutService {
    public List<CartItem> findAllCartItems();

    public OrderItem createOrderItem(CartItem cartItem, Order order);

    // public Order placeOrder(ShoppingCart shoppingCart, Customer customer);

    public Optional<Order> findOrderById(Integer id);

    public void deleteCartItem(CartItem cartItem);

    public void deleteAllCartItems(List<CartItem> cartItems);

    public void savePaymentMethod(PaymentMethod paymentMethod);

    public DiscountCode findDiscountCodeByCode(String discountCode);

    public List<Double> eachCartItemTotal(List<CartItem> cartItems);

    public double cartTotal(List<Double> eachCartItemTotal);

    public Order createNewOrder();

    public Order setNewOrderAttributes(Order order, List<OrderItem> orderItems, double subTotal, double taxTotal,
            double discountTotal, double grandTotal, String promoCodes);

    public void saveOrder(Order order);

    public double calculateTaxTotal(double netTotal);

    public double getCurrentGST();

    // public Shipment setNewShipmentAttributes(Order order, Shipment shipment);

    public void processOrderSubmission(CheckoutDTO checkoutDTO, Order order);
}
