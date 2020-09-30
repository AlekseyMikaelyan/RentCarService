package nix.finalproject.carrentalservice.repository;

import nix.finalproject.carrentalservice.entity.CarStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarStatusRepository extends JpaRepository<CarStatus, Long> {
}
