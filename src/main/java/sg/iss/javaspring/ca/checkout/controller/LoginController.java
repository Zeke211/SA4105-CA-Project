package sg.iss.javaspring.ca.checkout.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;
import sg.iss.javaspring.ca.checkout.model.Customer;
import sg.iss.javaspring.ca.checkout.service.CustomerService;

@Controller
public class LoginController {

    @Autowired
    CustomerService customerService;

    @GetMapping("/login")
    public String viewLoginPage() {
        return "login";
    }

    @GetMapping("/user/create")
    public String viewCreateUser(Model model) {
        // display create user form
        model.addAttribute("user", new Customer());
        return "create-user";
    }

    @PostMapping("/user/create/submit")
    public String createUser(@ModelAttribute("user") Customer newCustomer) {
        customerService.saveCustomer(newCustomer);
        return "redirect:/login";
    }

    // not implemented yet
    @PostMapping("/login")
    public String login(Customer customer, HttpSession sessionObj) {
        sessionObj.setAttribute("username", customer.getUsername());
        return "redirect:/cart";
    }

    // not implemented yet
    @PostMapping("/logout")
    public String logout(HttpSession sessionObj) {
        sessionObj.removeAttribute("username");
        return "redirect:/login";
    }

}
