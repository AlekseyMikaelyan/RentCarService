package nix.finalproject.carrentalservice.controller;

import nix.finalproject.carrentalservice.dto.OrderResponseDTO;
import nix.finalproject.carrentalservice.dto.request.OrderRequestDTO;
import nix.finalproject.carrentalservice.exceptions.ExceptionMessages;
import nix.finalproject.carrentalservice.exceptions.ServiceException;
import nix.finalproject.carrentalservice.service.OrderServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderServiceImpl orderServiceImpl;

    @Autowired
    public OrderController(OrderServiceImpl orderServiceImpl) {
        this.orderServiceImpl = orderServiceImpl;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public OrderResponseDTO createNewOrder(@Valid @RequestBody OrderRequestDTO order) {
        return orderServiceImpl.create(order);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public OrderResponseDTO findById(@PathVariable Long id) {
        return orderServiceImpl.getById(id)
                .orElseThrow(() -> ServiceException.entityNotFound(ExceptionMessages.CAN_NOT_FIND_ORDER));
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/{id}")
    public void updateOrder(@PathVariable Long id, @Valid @RequestBody OrderRequestDTO request) {
        orderServiceImpl.update(id, request);
    }

    @DeleteMapping("/{id}")
    public OrderResponseDTO deleteById(@PathVariable Long id) {
        return orderServiceImpl.deleteById(id)
                .orElseThrow(() -> ServiceException.entityNotFound(ExceptionMessages.CAN_NOT_FIND_ORDER));
    }

    @GetMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<OrderResponseDTO> findAll() {
        return orderServiceImpl.findAll();
    }

}
