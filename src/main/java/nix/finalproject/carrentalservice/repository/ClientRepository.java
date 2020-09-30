package nix.finalproject.carrentalservice.repository;

import nix.finalproject.carrentalservice.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client, Long> {
}
