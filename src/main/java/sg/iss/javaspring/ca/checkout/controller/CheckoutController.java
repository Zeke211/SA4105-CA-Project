package sg.iss.javaspring.ca.checkout.controller;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import sg.iss.javaspring.ca.checkout.model.CartItem;
import sg.iss.javaspring.ca.checkout.model.Customer;
import sg.iss.javaspring.ca.checkout.model.DiscountCode;
import sg.iss.javaspring.ca.checkout.model.Order;
import sg.iss.javaspring.ca.checkout.model.PaymentMethod;
import sg.iss.javaspring.ca.checkout.model.ShoppingCart;
import sg.iss.javaspring.ca.checkout.service.CheckoutService;
import sg.iss.javaspring.ca.checkout.validator.PaymentMethodValidator;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class CheckoutController {
    @Autowired
    CheckoutService checkoutService;
    @Autowired
    private PaymentMethodValidator paymentMethodValidator;

    @InitBinder
    private void initPaymentMethodValidator(WebDataBinder binder) {
        binder.addValidators(paymentMethodValidator);
    }

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
    public String viewCheckout(Model model, HttpSession sessionObj) {

        // Display cart item details
        List<CartItem> cartItems = checkoutService.findAllCartItems();
        model.addAttribute("cartItems", cartItems);

        // Display total costs of each type of cart item
        // List<Double> eachCartItemTotal = new LinkedList<Double>();
        // for (CartItem cartItem : cartItems) {
        // eachCartItemTotal.add(cartItem.getQuantity() * cartItem.getUnitPrice());
        // }
        List<Double> eachCartItemTotal = checkoutService.eachCartItemTotal(cartItems);
        model.addAttribute("CartItemTotal", eachCartItemTotal);

        // Display cart total
        // double cartTotal = 0;
        // for (int i = 0; i < eachCartItemTotal.size(); i++) {
        // cartTotal += eachCartItemTotal.get(i);
        // }
        double cartTotal = checkoutService.cartTotal(eachCartItemTotal);
        // check if newCartTotal is present in session
        // display taxed amount
        // calcualte taxTotal
        // display taxTotal
        Object newCartTotal = sessionObj.getAttribute("newCartTotal");
        double tax = 0.0;
        double grandTotal = 0.0;
        if (newCartTotal != null) {
            // Display new total after discount
            model.addAttribute("cartTotal", newCartTotal);
            grandTotal = checkoutService.calculateTaxTotal((double) newCartTotal);
            tax = grandTotal - (double) newCartTotal;

        } else {
            model.addAttribute("cartTotal", cartTotal);
            grandTotal = checkoutService.calculateTaxTotal((double) cartTotal);
            tax = grandTotal - (double) cartTotal;
        }
        // Display GST amount
        model.addAttribute("tax", tax);
        // Display grandTotal
        model.addAttribute("grandTotal", grandTotal);
        // Display payment form
        model.addAttribute("paymentMethod", new PaymentMethod());
        return "checkout";
    }

    // 3) postmapping(/checkout/discountCode)
    // apply discount code logic to total price
    @PostMapping("/checkout/discountCode")
    public String applyDiscountCode(@RequestParam String code, HttpSession sessionObj,
            RedirectAttributes redirectAttributes) {
        List<CartItem> cartItems = checkoutService.findAllCartItems();
        List<Double> eachCartItemTotal = checkoutService.eachCartItemTotal(cartItems);
        double originalCartTotal = checkoutService.cartTotal(eachCartItemTotal);
        double newCartTotal = originalCartTotal;
        if (checkoutService.findDiscountCodeByCode(code) != null
                && checkoutService.findDiscountCodeByCode(code).getCode().equalsIgnoreCase(code)) {
            DiscountCode discountCode = checkoutService.findDiscountCodeByCode(code);
            double discountPercent = discountCode.getDiscountPercent();
            newCartTotal = originalCartTotal * (1.0 - discountPercent);
            sessionObj.setAttribute("newCartTotal", newCartTotal);
        } else {
            redirectAttributes.addFlashAttribute("invalidCode", "Invalid Discount Code");
        }
        return "redirect:/checkout";
    }

    // 4)
    // postmapping
    // create new orderItem obj for each cartItem obj
    // transfer items from cartItem table to orderItem table
    // create entry in order table
    @PostMapping("/checkout/order")
    public String placeOrder(@Valid @ModelAttribute("paymentMethod") PaymentMethod paymentMethod,
            BindingResult bindingResult, HttpSession sessionObj, Model model) {
        if (bindingResult.hasErrors()) {
            // need to re-add the logic to display the cart items since forwarding will lose
            // data
            List<CartItem> cartItems = checkoutService.findAllCartItems();
            model.addAttribute("cartItems", cartItems);
            List<Double> eachCartItemTotal = checkoutService.eachCartItemTotal(cartItems);
            model.addAttribute("CartItemTotal", eachCartItemTotal);
            double cartTotal = checkoutService.cartTotal(eachCartItemTotal);
            // check if newCartTotal is present in session
            Object objCartTotal = sessionObj.getAttribute("newCartTotal");
            if (objCartTotal != null) {
                model.addAttribute("cartTotal", objCartTotal);
            } else {
                model.addAttribute("cartTotal", cartTotal);
            }
            // Dont need to display payment form again as original PaymentMethod object with
            // invalid data is kept
            return "checkout";
        }
        List<CartItem> cartItems = checkoutService.findAllCartItems();
        for (CartItem cartItem : cartItems) {
            checkoutService.createOrderItem(cartItem);
        }
        checkoutService.deleteAllCartItems(cartItems);
        checkoutService.savePaymentMethod(paymentMethod);
        // delete dession obj
        sessionObj.removeAttribute("newCartTotal");
        return "redirect:/checkout/thank-you";
        // return "thank-you";
    }

    @GetMapping("/checkout/thank-you")
    public String orderSuccess() {
        return "thank-you";
    }

    // @GetMapping("/checkout/{id}")
    // public String orderConfirmation(@PathVariable("id") Integer id, Model model)
    // {
    // Optional<Order> inOrder = checkoutService.findOrderById(id);
    // if (inOrder.isPresent()) {
    // Order order = inOrder.get();
    // model.addAttribute("order", order);
    // }
    // return "thank-you";
    // }

}
