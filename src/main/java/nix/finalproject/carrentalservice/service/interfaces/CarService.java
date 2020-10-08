package nix.finalproject.carrentalservice.service.interfaces;

import nix.finalproject.carrentalservice.dto.CarResponseDTO;
import nix.finalproject.carrentalservice.dto.request.CarRequestDTO;

import java.util.Optional;

public interface CarService {

    CarResponseDTO create(CarRequestDTO request);

    Optional<CarResponseDTO> getById(Long id);

    void update (Long id, CarRequestDTO request);

    Optional<CarResponseDTO> deleteById(Long id);

}
