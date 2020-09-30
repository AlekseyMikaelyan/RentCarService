package nix.finalproject.carrentalservice.controller;

import nix.finalproject.carrentalservice.dto.OrderDTO;
import nix.finalproject.carrentalservice.dto.request.OrderRequestDTO;
import nix.finalproject.carrentalservice.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<OrderDTO> findAll() {
        return orderService.findAllOrderDTO();
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public OrderDTO findById(@PathVariable("id") Long id) {
        return orderService.findOrderDTOById(id);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public OrderRequestDTO createNewOrder(@RequestBody OrderRequestDTO order) {
        return orderService.createNewOrder(order);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/{id}")
    public void updateOrder(@PathVariable Long id, @RequestBody OrderRequestDTO orderRequestDTO) {
        orderService.updateOrder(id, orderRequestDTO);
    }

    @DeleteMapping(value = "/{id}")
    public void deleteById(@PathVariable("id") Long id) {
        orderService.deleteById(id);
    }
}
