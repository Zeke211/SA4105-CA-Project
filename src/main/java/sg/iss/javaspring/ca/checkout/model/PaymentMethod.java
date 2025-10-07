package sg.iss.javaspring.ca.checkout.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class PaymentMethod {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private Integer expiryMonth;
    private Integer expiryYear;
    private String cardBrand;
    private String cardHolderName;
    private Integer lastFourDigits;
    private boolean isDefault;
}
