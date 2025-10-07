package sg.iss.javaspring.ca.checkout;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import sg.iss.javaspring.ca.checkout.model.CartItem;
import sg.iss.javaspring.ca.checkout.model.DiscountCode;
import sg.iss.javaspring.ca.checkout.model.Product;
import sg.iss.javaspring.ca.checkout.repository.CartItemRepository;
import sg.iss.javaspring.ca.checkout.repository.DiscountCodeRepository;
import sg.iss.javaspring.ca.checkout.repository.ProductRepository;

@SpringBootTest
public class TestDataCreation {
    @Autowired
    ProductRepository prepo;
    @Autowired
    CartItemRepository cartItemRepository;
    @Autowired
    DiscountCodeRepository discountCodeRepository;

    @Test
    void conTextLoad() {
        // Product data creation
        Product p1 = new Product("Bob Biscuits", 2.00);
        Product p2 = new Product("Tom Biscuits", 3.00);
        Product p3 = new Product("Joe Biscuits", 1.00);
        Product p4 = new Product("Foe Biscuits", 6.00);
        Product p5 = new Product("toe Biscuits", 4.00);

        prepo.save(p1);
        prepo.save(p2);
        prepo.save(p3);
        prepo.save(p4);
        prepo.save(p5);

        // Cart Item data creation
        CartItem c1 = new CartItem(3.0, 3);
        c1.setProduct(prepo.findById(2).get());
        cartItemRepository.save(c1);

        CartItem c2 = new CartItem(1.0, 4);
        c2.setProduct(prepo.findById(3).get());
        cartItemRepository.save(c2);

        CartItem c3 = new CartItem(6.0, 2);
        c3.setProduct(prepo.findById(4).get());
        cartItemRepository.save(c3);

        // Discount Code data creation
        DiscountCode d1 = new DiscountCode("HELLO", 0.2);
        DiscountCode d2 = new DiscountCode("WELCOME", 0.3);
        DiscountCode d3 = new DiscountCode("GOODBYE", 0.4);

        discountCodeRepository.save(d1);
        discountCodeRepository.save(d2);
        discountCodeRepository.save(d3);
    }

}
