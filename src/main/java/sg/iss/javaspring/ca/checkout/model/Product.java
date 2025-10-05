package sg.iss.javaspring.ca.checkout.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.GenerationType;

@Entity
@Getter
@Setter
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String description;
    private String name;
    private double unitPrice;
    private int stock;
    private String category;
    private String brand;
    private String tags;
    private String handle;
    private String imageURL;

    public Product() {
    }

    public Product(String description, String name, double unitPrice, int stock, String category, String brand,
            String tags, String handle, String imageURL) {
        this.description = description;
        this.name = name;
        this.unitPrice = unitPrice;
        this.stock = stock;
        this.category = category;
        this.brand = brand;
        this.tags = tags;
        this.handle = handle;
        this.imageURL = imageURL;
    }

}
