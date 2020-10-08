package nix.finalproject.carrentalservice.service.interfaces;

import nix.finalproject.carrentalservice.dto.ClientResponseDTO;
import nix.finalproject.carrentalservice.dto.request.ClientRequestDTO;

import java.util.Optional;

public interface ClientService {

    ClientResponseDTO create(ClientRequestDTO request);

    Optional<ClientResponseDTO> getById(Long id);

    void update (Long id, ClientRequestDTO request);

    Optional<ClientResponseDTO> deleteById(Long id);

}
