package nix.finalproject.carrentalservice.repository;

import nix.finalproject.carrentalservice.entity.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CarRepository extends JpaRepository<Car, Long> {

    @Query("select c from Car c where c.brand.brandName = :brandName")
    List<Car> findAllByBrand(@Param("brandName") String brandName);

    @Query("select c from Car c where c.engineType.type = :type")
    List<Car> findAllByEngine(@Param("type") String type);

    @Query("select c from Car c where c.carStatus.status = :status")
    List<Car> findAllByStatus(@Param("status") String status);

}
