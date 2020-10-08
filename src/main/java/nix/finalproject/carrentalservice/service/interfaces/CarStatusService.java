package nix.finalproject.carrentalservice.service.interfaces;

import nix.finalproject.carrentalservice.dto.CarStatusResponseDTO;
import nix.finalproject.carrentalservice.dto.request.CarStatusRequestDTO;

import java.util.Optional;

public interface CarStatusService {

    CarStatusResponseDTO create(CarStatusRequestDTO request);

    Optional<CarStatusResponseDTO> getById(Long id);

    void update (Long id, CarStatusRequestDTO request);

    Optional<CarStatusResponseDTO> deleteById(Long id);

}
