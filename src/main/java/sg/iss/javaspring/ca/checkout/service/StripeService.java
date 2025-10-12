package sg.iss.javaspring.ca.checkout.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Coupon;
import com.stripe.model.CouponCollection;
import com.stripe.model.checkout.Session;
import com.stripe.param.CouponCreateParams;
import com.stripe.param.checkout.SessionCreateParams;

import sg.iss.javaspring.ca.checkout.dto.StripeResponse;
import sg.iss.javaspring.ca.checkout.model.Order;
import sg.iss.javaspring.ca.checkout.model.OrderItem;

@Service
public class StripeService {
    @Value("${stripe.secret-key}")
    private String secretKey;

    // stripe -API
    // -> productName, amount, quantity, currency -> only need these 4 inputs to
    // connect to stripe api payment gateway
    // -> will return sessionId and checkout url

    public StripeResponse payProducts(List<OrderItem> orderItems, Order order) {
        Stripe.apiKey = secretKey;

        if (orderItems == null || orderItems.isEmpty()) {
            return StripeResponse.builder()
                    .status("FAILED")
                    .message("Cart is empty")
                    .build();
        }

        List<SessionCreateParams.LineItem> lineItems = new ArrayList<>();

        for (OrderItem orderItem : orderItems) {
            // get the four required parameters for stripe api
            Long amount = Math.round(orderItem.getUnitPrice() * 100); // need to change double to long for api
            Long quantity = (long) orderItem.getQuantity(); // need to change quantity to long as well

            // Product name
            SessionCreateParams.LineItem.PriceData.ProductData productData = SessionCreateParams.LineItem.PriceData.ProductData
                    .builder()
                    .setName(orderItem.getProductName()).build();

            // Price of item and currency
            SessionCreateParams.LineItem.PriceData priceData = SessionCreateParams.LineItem.PriceData.builder()
                    .setCurrency("sgd")
                    .setUnitAmount(amount)
                    .setProductData(productData)
                    .setTaxBehavior(SessionCreateParams.LineItem.PriceData.TaxBehavior.EXCLUSIVE)
                    .build();
            // Quantity
            SessionCreateParams.LineItem lineItem = SessionCreateParams.LineItem.builder()
                    .setQuantity(quantity)
                    .setPriceData(priceData)
                    .addTaxRate("txr_1SHP8CLE4e5BDmJr8uRGOfvB")
                    .build();

            lineItems.add(lineItem);
        }
        // Create Stripe session
        SessionCreateParams.Builder sessionBuilder = SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .putMetadata("orderId", String.valueOf(order.getId()))
                .setSuccessUrl("http://localhost:8080/checkout/thank-you?session-id={CHECKOUT_SESSION_ID}")
                .setCancelUrl("http://localhost:8080/checkout")
                .addAllLineItem(lineItems)
                .setAutomaticTax(SessionCreateParams.AutomaticTax.builder()
                        .setEnabled(false)
                        .build());

        // Implement discount from discount code to final price //
        if (order.getDiscountTotal() > 0.0) {
            Long discountAmountInCents = Math.round(order.getDiscountTotal() * 100);

            try {
                CouponCreateParams couponParams = CouponCreateParams.builder()
                        .setName("Cart Discount")
                        .setCurrency("sgd")
                        .setAmountOff(discountAmountInCents)
                        .setDuration(CouponCreateParams.Duration.ONCE)
                        .build();

                Coupon coupon = Coupon.create(couponParams);

                sessionBuilder.addDiscount(SessionCreateParams.Discount.builder().setCoupon(coupon.getId()).build());
            } catch (StripeException e) {
                e.printStackTrace();
                return StripeResponse.builder()
                        .status("FAILED")
                        .message("Could not create discount coupon" + e.getMessage())
                        .build();
            }
        }

        // -----Discount cannot be calculated in Stripe with a negative priced product//
        // Create Discount name to be shown on checkout
        // SessionCreateParams.LineItem.PriceData.ProductData discountName =
        // SessionCreateParams.LineItem.PriceData.ProductData
        // .builder()
        // .setName("Discount Applied")
        // .build();

        // SessionCreateParams.LineItem discountApplied =
        // SessionCreateParams.LineItem.builder()
        // .setPriceData(SessionCreateParams.LineItem.PriceData.builder()
        // .setCurrency("sgd")
        // .setUnitAmount(discountAmountInCents)
        // .setProductData(discountName)
        // .setTaxBehavior(SessionCreateParams.LineItem.PriceData.TaxBehavior.INCLUSIVE)
        // .build())
        // .setQuantity(1L)
        // .build();

        // lineItems.add(discountApplied);

        // Once payment is done, if session does not throw exception we will get the
        // sessionId and sessionUrl
        Session session = null;

        try {
            session = Session.create(sessionBuilder.build());
            System.out.println("[STRIPE] sessionURL=" + session.getUrl());
            return StripeResponse.builder()
                    .status("SUCCESS")
                    .message("Payment session created")
                    .sessionId(session.getId())
                    .sessionUrl(session.getUrl())
                    .build();
        } catch (StripeException ex) {
            // log
            ex.printStackTrace();
            return StripeResponse.builder()
                    .status("FAILED")
                    .message("Stripe session creation failed: " + ex.getMessage())
                    .build();
        }
        // To-do
        // 1) need to update paymentProcess to complete after successful checkout
        // 2) need to update paymentProcess to require authentication and denied options
        // too
        // 3) need to update price after discount code use in stripe page
        // 4) need to include tax too

    }

}
