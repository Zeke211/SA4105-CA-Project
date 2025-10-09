package sg.iss.javaspring.ca.checkout.service;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

import sg.iss.javaspring.ca.checkout.model.CartItem;
import sg.iss.javaspring.ca.checkout.model.Customer;
import sg.iss.javaspring.ca.checkout.model.DiscountCode;
import sg.iss.javaspring.ca.checkout.model.Order;
import sg.iss.javaspring.ca.checkout.model.OrderItem;
import sg.iss.javaspring.ca.checkout.model.PaymentMethod;
import sg.iss.javaspring.ca.checkout.model.ShoppingCart;
import sg.iss.javaspring.ca.checkout.repository.CartItemRepository;
import sg.iss.javaspring.ca.checkout.repository.DiscountCodeRepository;
import sg.iss.javaspring.ca.checkout.repository.OrderItemRepository;
import sg.iss.javaspring.ca.checkout.repository.OrderRepository;
import sg.iss.javaspring.ca.checkout.repository.PaymentMethodRepository;
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

    @Override
    public List<CartItem> findAllCartItems() {
        return cartItemRepository.findAll();
    }

    @Transactional(readOnly = false)
    @Override
    public OrderItem createOrderItem(CartItem cartItem) {
        OrderItem orderItem = new OrderItem();
        orderItem.setProduct(cartItem.getProduct());
        orderItem.setProductName(cartItem.getProduct().getName());
        orderItem.setUnitPrice(cartItem.getUnitPrice());
        orderItem.setQuantity(cartItem.getQuantity());
        orderItem.setItemTotal(orderItem.getQuantity() * orderItem.getUnitPrice());
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

    @Override
    public Order createOrder(List<OrderItem> orderItems) {
        Order newOrder = new Order();
        newOrder.setOrderItems(orderItems);
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

}
