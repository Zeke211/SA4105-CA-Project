package sg.iss.javaspring.ca.checkout.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String productName;
    private double unitPrice;
    // private double discount;
    // private double tax;
    private int quantity;
    private double itemTotal;

    @ManyToOne
    private Product product;

    @ManyToOne
    @JoinColumn(name = "orderId")
    private Order orders;

    public OrderItem() {

    }

    public OrderItem(String productName, double unitPrice, double discount, double tax, double itemTotal) {
        this.productName = productName;
        this.unitPrice = unitPrice;
        // this.discount=discount;
        // this.tax=tax;
        this.itemTotal = itemTotal;
    }
}
