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
import sg.iss.javaspring.ca.checkout.model.OrderItem;
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

        // Display cart total && set as session attribute for subTotal in Order
        double cartTotal = checkoutService.cartTotal(eachCartItemTotal);
        sessionObj.setAttribute("subTotal", cartTotal);
        // check if newCartTotal is present in session
        // display taxed amount
        // calcualte taxTotal
        // display taxTotal
        Object newCartTotal = sessionObj.getAttribute("newCartTotal");
        double tax = 0.0;
        double grandTotal = 0.0;
        double discount = 0.0;
        if (newCartTotal != null) {
            // Display new total after discount
            model.addAttribute("cartTotal", newCartTotal);
            grandTotal = checkoutService.calculateTaxTotal((double) newCartTotal);
            tax = grandTotal - (double) newCartTotal;
            // set discount as session attribute for discountTotal in Order
            discount = cartTotal - (double) newCartTotal;
            sessionObj.setAttribute("discountTotal", discount);
        } else {
            model.addAttribute("cartTotal", cartTotal);
            grandTotal = checkoutService.calculateTaxTotal((double) cartTotal);
            tax = grandTotal - (double) cartTotal;
        }
        // Display GST amount && set as sessionObj for taxTotal in Order
        model.addAttribute("tax", tax);
        sessionObj.setAttribute("taxTotal", tax);
        // Display grandTotal && set as sessionObj for grandTotal in Order
        model.addAttribute("grandTotal", grandTotal);
        sessionObj.setAttribute("grandTotal", grandTotal);
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
            // save String code from discount code into session for Order
            sessionObj.setAttribute("code", discountCode.getCode());
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
            Object newCartTotal = sessionObj.getAttribute("newCartTotal");
            double tax = 0.0;
            double grandTotal = 0.0;
            if (newCartTotal != null) {
                model.addAttribute("cartTotal", newCartTotal);
                grandTotal = checkoutService.calculateTaxTotal((double) newCartTotal);
                tax = grandTotal - (double) newCartTotal;
            } else {
                model.addAttribute("cartTotal", cartTotal);
                grandTotal = checkoutService.calculateTaxTotal((double) cartTotal);
                tax = grandTotal - (double) cartTotal;
            }
            // Display GST amount && grandTotal again
            model.addAttribute("tax", tax);
            model.addAttribute("grandTotal", grandTotal);
            // Dont need to display payment form again as original PaymentMethod object with
            // invalid data is kept
            return "checkout";
        }
        // create new order
        Order newOrder = checkoutService.createNewOrder();
        // save new empty order to persist it
        checkoutService.saveOrder(newOrder);
        // create new List to store all OrderItems
        List<OrderItem> newOrderItems = new LinkedList<OrderItem>();
        // copy cartItems to orderItems
        // add newly converted orderItems to newOrderItems list
        // set Order object to each OrderItem
        List<CartItem> cartItems = checkoutService.findAllCartItems();
        for (CartItem cartItem : cartItems) {
            OrderItem orderItem = checkoutService.createOrderItem(cartItem, newOrder);
            newOrderItems.add(orderItem);
        }
        // Retrieve code from session. If null just enter empty String
        Object codeObj = sessionObj.getAttribute("code");
        Object subTotalObj = sessionObj.getAttribute("subTotal");
        Object discountTotalObj = sessionObj.getAttribute("discountTotal");
        Object taxObj = sessionObj.getAttribute("taxTotal");
        Object grandTotalObj = sessionObj.getAttribute("grandTotal");
        String code = "";
        double subTotal = 0, discountTotal = 0, taxTotal = 0, grandTotal = 0;
        if (codeObj != null && subTotalObj != null && discountTotalObj != null && taxObj != null
                && grandTotalObj != null) {
            code = (String) codeObj;
            subTotal = (double) subTotalObj;
            discountTotal = (double) discountTotalObj;
            taxTotal = (double) taxObj;
            grandTotal = (double) grandTotalObj;
        }
        // set and save all attributes for new order
        checkoutService.setNewOrderAttributes(newOrder, newOrderItems, subTotal, taxTotal, discountTotal, grandTotal,
                code);
        checkoutService.saveOrder(newOrder);
        // delete existing cart items
        checkoutService.deleteAllCartItems(cartItems);
        // save payment method
        checkoutService.savePaymentMethod(paymentMethod);
        // delete session obj
        sessionObj.removeAttribute("newCartTotal");
        sessionObj.removeAttribute("code");
        sessionObj.removeAttribute("subTotal");
        sessionObj.removeAttribute("discountTotal");
        sessionObj.removeAttribute("taxTotal");
        sessionObj.removeAttribute("grandTotal");
        // create new shipment record

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
