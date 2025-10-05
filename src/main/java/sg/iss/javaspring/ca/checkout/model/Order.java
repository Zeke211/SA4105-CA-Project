package sg.iss.javaspring.ca.checkout.model;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    // private String orderStatus;
    // private String paymentStatus;
    // private String promoCodes;
    // private String fulfilmentStatus;
    // private LocalDateTime createdAt;
    // private double subTotal;
    // private double taxTotal;
    // private double discountTotal;
    // private double grandTotal;

    @OneToMany(mappedBy = "order")
    private List<OrderItem> orderItems;

    public Order() {
    }

}
