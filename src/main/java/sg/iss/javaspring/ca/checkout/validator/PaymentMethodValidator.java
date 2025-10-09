package sg.iss.javaspring.ca.checkout.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.time.YearMonth;

import sg.iss.javaspring.ca.checkout.model.PaymentMethod;

@Component
public class PaymentMethodValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return PaymentMethod.class.isAssignableFrom(clazz);
    }

    // Instances where value is rejected
    // Value is null
    // Current year > Expiry year
    // if current year = expiry year -> check if current month > expiry month
    @Override
    public void validate(Object target, Errors errors) {
        PaymentMethod paymentMethod = (PaymentMethod) target;
        YearMonth now = YearMonth.now();
        Integer mm = paymentMethod.getExpiryMonth();
        Integer yy = paymentMethod.getExpiryYear();
        if (paymentMethod.getExpiryMonth() != null & paymentMethod.getExpiryYear() != null) {
            int year = 0;
            if (yy < 100) {
                year = 2000 + yy;
            }
            YearMonth submittedDate = YearMonth.of(year, mm);
            if (submittedDate.isBefore(now)) {
                errors.rejectValue("expiryYear", "errors.year.expiry", "Card has expired. Use another card.");
            }
        }
    }
}
