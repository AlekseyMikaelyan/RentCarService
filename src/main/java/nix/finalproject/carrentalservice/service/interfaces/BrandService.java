package nix.finalproject.carrentalservice.service.interfaces;

import nix.finalproject.carrentalservice.dto.BrandResponseDTO;
import nix.finalproject.carrentalservice.dto.request.BrandRequestDTO;

import java.util.Optional;

public interface BrandService {

    BrandResponseDTO create(BrandRequestDTO request);

    Optional<BrandResponseDTO> getById(Long id);

    void update (Long id, BrandRequestDTO request);

    Optional<BrandResponseDTO> deleteById(Long id);

}
