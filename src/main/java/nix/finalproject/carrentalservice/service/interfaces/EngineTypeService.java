package nix.finalproject.carrentalservice.service.interfaces;

import nix.finalproject.carrentalservice.dto.EngineTypeResponseDTO;
import nix.finalproject.carrentalservice.dto.request.EngineTypeRequestDTO;

import java.util.Optional;

public interface EngineTypeService {

    EngineTypeResponseDTO create(EngineTypeRequestDTO request);

    Optional<EngineTypeResponseDTO> getById(Long id);

    void update (Long id, EngineTypeRequestDTO request);

    Optional<EngineTypeResponseDTO> deleteById(Long id);

}
