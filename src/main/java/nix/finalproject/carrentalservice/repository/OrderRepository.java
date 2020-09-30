package nix.finalproject.carrentalservice.repository;

import nix.finalproject.carrentalservice.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
