package sg.iss.javaspring.ca.checkout.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import sg.iss.javaspring.ca.checkout.model.PaymentMethod;

public interface PaymentMethodRepository extends JpaRepository<PaymentMethod, Integer> {

}
