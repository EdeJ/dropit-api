package nl.saxofoonleren.dropitapi.repository;

import nl.saxofoonleren.dropitapi.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
