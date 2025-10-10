package sg.iss.javaspring.ca.checkout.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Shipment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String courierName;
    private String seviceLevel;
    private String shipmentCode;
    private LocalDateTime deliveryEstimate;
    private LocalDateTime createdAt;
    private String shipmentMethod;

    @OneToOne
    @JoinColumn(name = "orderId")
    private Order orders;

}
