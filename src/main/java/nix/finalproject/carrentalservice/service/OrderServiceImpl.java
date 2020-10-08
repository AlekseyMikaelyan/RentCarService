package nix.finalproject.carrentalservice.service;

import nix.finalproject.carrentalservice.dto.OrderResponseDTO;
import nix.finalproject.carrentalservice.dto.request.OrderRequestDTO;
import nix.finalproject.carrentalservice.entity.*;
import nix.finalproject.carrentalservice.exceptions.ExceptionMessages;
import nix.finalproject.carrentalservice.exceptions.ServiceException;
import nix.finalproject.carrentalservice.repository.CarRepository;
import nix.finalproject.carrentalservice.repository.ClientRepository;
import nix.finalproject.carrentalservice.repository.OrderRepository;
import nix.finalproject.carrentalservice.service.interfaces.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final ClientRepository clientRepository;
    private final CarRepository carRepository;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository, ClientRepository clientRepository, CarRepository carRepository) {
        this.orderRepository = orderRepository;
        this.clientRepository = clientRepository;
        this.carRepository = carRepository;
    }

    public List<OrderResponseDTO> convertOrderListToOrderResponseDTOList(List<Order> orderList) {
        return orderList.stream().map(OrderResponseDTO::fromOrder).collect(Collectors.toList());
    }

    @Override
    public OrderResponseDTO create(OrderRequestDTO request) {

        Client client = clientRepository.findById(request.getClientId()).orElseThrow(() -> ServiceException.entityNotFound(ExceptionMessages.CAN_NOT_FIND_CLIENT));

        Car car = carRepository.findById(request.getCarId()).orElseThrow(() -> ServiceException.entityNotFound(ExceptionMessages.CAN_NOT_FIND_CAR));

        Order order = new Order(client, car, request.getStartOrder(), request.getEndOrder());

        return OrderResponseDTO.fromOrder(orderRepository.save(order));
    }

    @Override
    public Optional<OrderResponseDTO> getById(Long id) {
        return orderRepository.findById(id).map(OrderResponseDTO::fromOrder);
    }

    @Override
    public void update(Long id, OrderRequestDTO request) {
        var order = orderRepository.findById(id).orElseThrow(() -> ServiceException.entityNotFound(ExceptionMessages.CAN_NOT_FIND_ORDER));

        order.setClient(clientRepository.findById(request.getClientId()).orElseThrow(() -> ServiceException.entityNotFound(ExceptionMessages.CAN_NOT_FIND_CLIENT)));
        order.setCar(carRepository.findById(request.getCarId()).orElseThrow(() -> ServiceException.entityNotFound(ExceptionMessages.CAN_NOT_FIND_CAR)));
        order.setStartOrder(request.getStartOrder());
        order.setEndOrder(request.getEndOrder());
        orderRepository.save(order);
    }

    @Override
    public Optional<OrderResponseDTO> deleteById(Long id) {
        var order = orderRepository.findById(id);
        order.ifPresent(orderRepository::delete);
        return order.map(OrderResponseDTO::fromOrder);
    }

    public List<OrderResponseDTO> findAll() {
        return convertOrderListToOrderResponseDTOList(orderRepository.findAll());
    }

}
