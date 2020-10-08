package nix.finalproject.carrentalservice.service.interfaces;

import nix.finalproject.carrentalservice.dto.CarTypeResponseDTO;
import nix.finalproject.carrentalservice.dto.request.CarTypeRequestDTO;

import java.util.Optional;

public interface CarTypeService {

    CarTypeResponseDTO create(CarTypeRequestDTO request);

    Optional<CarTypeResponseDTO> getById(Long id);

    void update (Long id, CarTypeRequestDTO request);

    Optional<CarTypeResponseDTO> deleteById(Long id);

}
