package sg.iss.javaspring.ca.checkout.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    // private String firstName;
    // private String lastName;
    // private String phoneNumber;
    // private String email;
    // private String address;
    // private String country;
    // private int postalCode;
    // private String password;
    // private String providerCustomerId;

    @OneToOne(mappedBy = "customer")
    private ShoppingCart shoppingCart;
}
