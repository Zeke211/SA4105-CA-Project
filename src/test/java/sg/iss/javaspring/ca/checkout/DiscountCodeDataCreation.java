package sg.iss.javaspring.ca.checkout;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import sg.iss.javaspring.ca.checkout.model.DiscountCode;
import sg.iss.javaspring.ca.checkout.repository.DiscountCodeRepository;

@SpringBootTest
public class DiscountCodeDataCreation {
    @Autowired
    DiscountCodeRepository discountCodeRepository;

    @Test
    void conTextLoad() {
        DiscountCode d1 = new DiscountCode("HELLO", 20.0);
        DiscountCode d2 = new DiscountCode("WELCOME", 30.0);
        DiscountCode d3 = new DiscountCode("WELCOME", 40.0);

        discountCodeRepository.save(d1);
        discountCodeRepository.save(d2);
        discountCodeRepository.save(d3);
    }
}
