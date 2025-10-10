package sg.iss.javaspring.ca.checkout.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import sg.iss.javaspring.ca.checkout.model.Shipment;

public interface ShipmentRepository extends JpaRepository<Shipment, Integer> {

}
