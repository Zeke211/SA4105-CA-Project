package sg.iss.javaspring.ca.checkout.controller;

import java.util.List;
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
        model.addAttribute("cartItems", checkoutService.findAllCartItems());
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
        model.addAttribute("cartItems", checkoutService.findAllCartItems());
        return "checkout";
    }

    // 3)
    // postmapping
    // create new orderItem obj for each cartItem obj
    // transfer items from cartItem table to orderItem table
    // create entry in order table
    @PostMapping("/checkout/order")
    public String placeOrder() {
        List<CartItem> cartitem = checkoutService.findAllCartItems();
        for (CartItem cartItems : cartitem) {
            checkoutService.createOrderItem(cartItems);
            checkoutService.deleteCartItem(cartItems);
        }
        // return "redirect:/checkout/thank-you"
        return "thank-you";
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
