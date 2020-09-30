package nix.finalproject.carrentalservice.repository;

import nix.finalproject.carrentalservice.entity.Brand;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BrandRepository extends JpaRepository<Brand, Long> {
}
