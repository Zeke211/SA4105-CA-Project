package sg.iss.javaspring.ca.checkout.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import sg.iss.javaspring.ca.checkout.model.CartItem;
import sg.iss.javaspring.ca.checkout.model.Customer;
import sg.iss.javaspring.ca.checkout.model.Order;
import sg.iss.javaspring.ca.checkout.model.ShoppingCart;
import sg.iss.javaspring.ca.checkout.service.CheckoutService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class CheckoutController {
    @Autowired
    CheckoutService checkoutService;

    // 1)
    // getmapping basic cart page
    // use CartItem entity
    @GetMapping("/cart")
    public String viewCart(Model model) {
        model.addAttribute("shoppingCart", checkoutService.findAllCartItems());
        return "cart";
    }

    // 2)
    // postmapping on cart page (checkout button)
    // getmapping more ideal here since its just displaying data for now and not
    // changing data
    // click button on cart page that will go to checkout page
    // still use CartItem entity
    @GetMapping("/checkout")
    public String viewCheckout(Model model) {
        model.addAttribute("shoppingCart", checkoutService.findAllCartItems());
        return "checkout";
    }

    // 3)
    // postmapping
    // create new orderItem obj?
    // transfer items from cartItem table to orderItem table
    // create entry in order table
    @PostMapping("/checkout/order")
    public String placeOrder(Customer customer, Model model) {
        ShoppingCart shoppingCart = customer.getShoppingCart();
        Order order = checkoutService.placeOrder(shoppingCart, customer);
        model.addAttribute("order", order);
        return "redirect:/checkout/thank-you";
    }

    @GetMapping("/checkout/{id}")
    public String orderConfirmation(@PathVariable("id") Integer id, Model model) {
        Optional<Order> inOrder = checkoutService.findOrderById(id);
        if (inOrder.isPresent()) {
            Order order = inOrder.get();
            model.addAttribute("order", order);
        }
        return "thank-you";
    }

}
