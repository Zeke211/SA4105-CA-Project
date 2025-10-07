package sg.iss.javaspring.ca.checkout.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class DiscountCode {
    @Id
    private String code;
    private double discountPercent;

    public DiscountCode(String code, double discountPercent) {
        this.code = code;
        this.discountPercent = discountPercent;
    }
}
