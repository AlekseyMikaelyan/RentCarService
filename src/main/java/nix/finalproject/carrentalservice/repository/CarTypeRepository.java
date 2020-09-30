package nix.finalproject.carrentalservice.repository;

import nix.finalproject.carrentalservice.entity.CarType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarTypeRepository extends JpaRepository<CarType, Long> {
}
