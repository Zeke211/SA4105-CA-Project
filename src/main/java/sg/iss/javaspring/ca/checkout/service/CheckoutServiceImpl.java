package sg.iss.javaspring.ca.checkout.service;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

import sg.iss.javaspring.ca.checkout.model.CartItem;
import sg.iss.javaspring.ca.checkout.model.CheckoutDTO;
import sg.iss.javaspring.ca.checkout.model.Customer;
import sg.iss.javaspring.ca.checkout.model.DiscountCode;
import sg.iss.javaspring.ca.checkout.model.Order;
import sg.iss.javaspring.ca.checkout.model.OrderItem;
import sg.iss.javaspring.ca.checkout.model.PaymentMethod;
import sg.iss.javaspring.ca.checkout.model.Shipment;
import sg.iss.javaspring.ca.checkout.model.ShoppingCart;
import sg.iss.javaspring.ca.checkout.repository.CartItemRepository;
import sg.iss.javaspring.ca.checkout.repository.DiscountCodeRepository;
import sg.iss.javaspring.ca.checkout.repository.OrderItemRepository;
import sg.iss.javaspring.ca.checkout.repository.OrderRepository;
import sg.iss.javaspring.ca.checkout.repository.PaymentMethodRepository;
import sg.iss.javaspring.ca.checkout.repository.ShipmentRepository;
import sg.iss.javaspring.ca.checkout.repository.ShoppingCartRepository;

@Service
@Transactional(readOnly = true)
public class CheckoutServiceImpl implements CheckoutService {

    @Autowired
    CartItemRepository cartItemRepository;
    @Autowired
    OrderItemRepository orderItemRepository;
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    ShoppingCartRepository shoppingCartRepository;
    @Autowired
    PaymentMethodRepository paymentMethodRepository;
    @Autowired
    DiscountCodeRepository discountCodeRepository;
    @Autowired
    ShipmentRepository shipmentRepository;

    @Override
    public List<CartItem> findAllCartItems() {
        return cartItemRepository.findAll();
    }

    @Transactional(readOnly = false)
    @Override
    public OrderItem createOrderItem(CartItem cartItem, Order order) {
        OrderItem orderItem = new OrderItem();
        orderItem.setProduct(cartItem.getProduct());
        orderItem.setProductName(cartItem.getProduct().getName());
        orderItem.setUnitPrice(cartItem.getUnitPrice());
        orderItem.setQuantity(cartItem.getQuantity());
        orderItem.setItemTotal(orderItem.getQuantity() * orderItem.getUnitPrice());
        orderItem.setOrders(order);
        return orderItemRepository.save(orderItem);
    }

    // @Transactional(readOnly = false)
    // @Override
    // public Order placeOrder(ShoppingCart shoppingCart, Customer customer) {
    // Order newOrder = new Order();
    // newOrder.setCustomer(customer);
    // Order saveOrder = orderRepository.save(newOrder);
    // for (CartItem cartItem : shoppingCart.getCartItems()) {
    // createOrderItem(cartItem, saveOrder);
    // }
    // shoppingCartRepository.delete(shoppingCart);
    // return saveOrder;
    // }

    @Override
    public Optional<Order> findOrderById(Integer id) {
        return orderRepository.findById(id);
    }

    @Override
    public void deleteCartItem(CartItem cartItem) {
        cartItemRepository.delete(cartItem);
    }

    @Transactional(readOnly = false)
    @Override
    public void deleteAllCartItems(List<CartItem> cartItems) {
        cartItemRepository.deleteAll(cartItems);
    }

    @Transactional(readOnly = false)
    @Override
    public void savePaymentMethod(PaymentMethod paymentMethod) {
        paymentMethodRepository.save(paymentMethod);
    }

    @Override
    public DiscountCode findDiscountCodeByCode(String discountCode) {
        return discountCodeRepository.findDiscountCodeByCode(discountCode);
    }

    @Override
    public List<Double> eachCartItemTotal(List<CartItem> cartItems) {
        List<Double> eachCartItemTotalPrice = new LinkedList<Double>();
        for (CartItem cartItem : cartItems) {
            eachCartItemTotalPrice.add(cartItem.getQuantity() * cartItem.getUnitPrice());
        }
        return eachCartItemTotalPrice;
    }

    @Override
    public double cartTotal(List<Double> eachCartItemTotal) {
        double cartTotal = 0;
        for (int i = 0; i < eachCartItemTotal.size(); i++) {
            cartTotal += eachCartItemTotal.get(i);
        }
        return cartTotal;
    }

    @Transactional(readOnly = false)
    @Override
    public Order createNewOrder(Customer customer) {
        Order newOrder = new Order();
        newOrder.setCustomer(customer);
        return orderRepository.save(newOrder);
    }

    @Override
    public double calculateTaxTotal(double netTotal) {
        double taxTotal = netTotal * (1 + getCurrentGST());
        return taxTotal;
    }

    @Override
    public double getCurrentGST() {
        return 0.09;
    }

    @Transactional(readOnly = false)
    @Override
    public Order setNewOrderAttributes(Order order, List<OrderItem> orderItems, double subTotal, double taxTotal,
            double discountTotal, double grandTotal, String promoCodes) {
        order.setCreatedAt(LocalDateTime.now());
        order.setSubTotal(subTotal);
        order.setTaxTotal(taxTotal);
        order.setDiscountTotal(discountTotal);
        order.setGrandTotal(grandTotal);
        order.setPromoCodes(promoCodes);
        order.setOrderItems(orderItems);
        return orderRepository.save(order);
    }

    @Transactional(readOnly = false)
    @Override
    public void saveOrder(Order order) {
        orderRepository.save(order);
    }

    // change to set shipment attributes
    // remove the create new shipment since processOrderSubmission is creating that
    // instead
    // @Transactional(readOnly = false)
    // @Override
    // public Shipment setNewShipmentAttributes(Order order, Shipment shipment) {
    // // Shipment shipment = new Shipment();
    // shipment.setOrders(order);
    // shipmentRepository.save(shipment);
    // return shipment;
    // }

    @Transactional(readOnly = false)
    @Override
    public void processOrderSubmission(CheckoutDTO checkoutDTO, Order order, Customer customer) {
        // transfer PaymentMethod attributes from checkoutDTO to PaymentMethod
        PaymentMethod paymentMethod = new PaymentMethod();
        paymentMethod.setExpiryMonth(checkoutDTO.getExpiryMonth());
        paymentMethod.setExpiryYear(checkoutDTO.getExpiryYear());
        paymentMethod.setCardHolderName(checkoutDTO.getCardHolderName());
        paymentMethod.setCustomer(customer);
        paymentMethodRepository.save(paymentMethod);
        // transfer Shipment attributes from checkoutDTO to Shipment
        Shipment shipment = new Shipment();
        shipment.setOrders(order);
        shipment.setCreatedAt(LocalDateTime.now());
        if (checkoutDTO.getServiceLevel().equalsIgnoreCase("standard")) {
            shipment.setSeviceLevel("standard");
            shipment.setCourierName("NinjaVan");
        } else {
            shipment.setSeviceLevel("express");
            shipment.setCourierName("SGExpress");
        }
        shipmentRepository.save(shipment);
    }
}
