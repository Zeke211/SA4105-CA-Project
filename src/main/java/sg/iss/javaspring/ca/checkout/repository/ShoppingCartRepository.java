package sg.iss.javaspring.ca.checkout.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import sg.iss.javaspring.ca.checkout.model.ShoppingCart;

public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, Integer> {

}
