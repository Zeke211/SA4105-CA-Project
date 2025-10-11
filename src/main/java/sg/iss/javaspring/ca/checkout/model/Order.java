package sg.iss.javaspring.ca.checkout.model;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String orderStatus;
    private String paymentStatus;
    private String promoCodes;
    private String fulfilmentStatus;
    private LocalDateTime createdAt;
    private double subTotal;
    private double taxTotal;
    private double discountTotal;
    private double grandTotal;

    @OneToMany(mappedBy = "orders")
    private List<OrderItem> orderItems;

    @ManyToOne
    @JoinColumn(name = "customerId")
    private Customer customer;

    @OneToOne(mappedBy = "orders")
    private Shipment shipment;

}
