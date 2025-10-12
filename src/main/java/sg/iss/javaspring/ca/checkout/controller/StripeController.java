// package sg.iss.javaspring.ca.checkout.controller;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.beans.factory.annotation.Value;
// import org.springframework.http.HttpStatus;
// import org.springframework.http.ResponseEntity;
// import org.springframework.stereotype.Controller;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RestController;

// import com.stripe.Stripe;
// import com.stripe.net.StripeResponse;
// import com.stripe.service.CheckoutService;

// import sg.iss.javaspring.ca.checkout.service.StripeService;
// import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.RequestBody;

// @RestController
// @RequestMapping("/product/v1")
// public class StripeController {
// @Autowired
// private CheckoutService checkoutService;
// @Autowired
// private StripeService stripeService;

// public StripeController(StripeService stripeService) {
// this.stripeService = stripeService;
// }

// @PostMapping("/checkout/payment")
// public ResponseEntity<StripeResponse> payProducts() {
// StripeResponse stripeResponse = stripeService.payProducts(orderItem, order);
// return ResponseEntity
// .status(HttpStatus.OK)
// .body(stripeResponse);
// }
// }
