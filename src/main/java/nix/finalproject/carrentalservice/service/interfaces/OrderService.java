package nix.finalproject.carrentalservice.service.interfaces;

import nix.finalproject.carrentalservice.dto.OrderResponseDTO;
import nix.finalproject.carrentalservice.dto.request.OrderRequestDTO;

import java.util.Optional;

public interface OrderService {

    OrderResponseDTO create(OrderRequestDTO request);

    Optional<OrderResponseDTO> getById(Long id);

    void update (Long id, OrderRequestDTO request);

    Optional<OrderResponseDTO> deleteById(Long id);

}
