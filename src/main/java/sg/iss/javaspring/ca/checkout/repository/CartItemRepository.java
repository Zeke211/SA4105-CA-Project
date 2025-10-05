package sg.iss.javaspring.ca.checkout.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import sg.iss.javaspring.ca.checkout.model.CartItem;

public interface CartItemRepository extends JpaRepository<CartItem, Integer> {

}
