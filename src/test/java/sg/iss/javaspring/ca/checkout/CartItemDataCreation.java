package sg.iss.javaspring.ca.checkout;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import sg.iss.javaspring.ca.checkout.model.CartItem;
import sg.iss.javaspring.ca.checkout.repository.CartItemRepository;
import sg.iss.javaspring.ca.checkout.repository.ProductRepository;

@SpringBootTest
public class CartItemDataCreation {
    @Autowired
    CartItemRepository cartItemRepository;
    @Autowired
    ProductRepository productRepository;

    @Test
    void conTextLoad() {
        CartItem c1 = new CartItem(3.0, 3);
        c1.setProduct(productRepository.findById(2).get());
        cartItemRepository.save(c1);

        CartItem c2 = new CartItem(1.0, 4);
        c2.setProduct(productRepository.findById(3).get());
        cartItemRepository.save(c2);

        CartItem c3 = new CartItem(6.0, 2);
        c3.setProduct(productRepository.findById(4).get());
        cartItemRepository.save(c3);
    }

}
