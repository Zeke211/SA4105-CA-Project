package sg.iss.javaspring.ca.checkout.model;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class CheckoutDTO {
    // PaymentMethod attributes
    @NotNull(message = "Month is required. Format: 1-12")
    @Min(1)
    @Max(12)
    private Integer expiryMonth;
    @NotNull(message = "Year is required")
    @Min(24)
    @Max(99)
    private Integer expiryYear;
    @NotNull(message = "Card Number is required")
    @Size(min = 16, max = 16)
    @Pattern(regexp = "\\d{16}", message = "Card number must contain 16 digits") // ensures that all 16 are digits (0-9)
    private String cardNumber;
    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 32)
    private String cardHolderName;

    // Shipment attributes
    @NotNull(message = "Delivery option is required")
    private String serviceLevel; // standard or express

}
