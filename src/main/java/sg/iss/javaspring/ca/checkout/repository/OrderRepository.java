package sg.iss.javaspring.ca.checkout.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import sg.iss.javaspring.ca.checkout.model.Order;

public interface OrderRepository extends JpaRepository<Order, Integer> {

}
