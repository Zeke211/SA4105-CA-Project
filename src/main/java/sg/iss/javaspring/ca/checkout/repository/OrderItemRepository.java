package sg.iss.javaspring.ca.checkout.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import sg.iss.javaspring.ca.checkout.model.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Integer> {

}
