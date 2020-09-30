package nix.finalproject.carrentalservice.service;

import nix.finalproject.carrentalservice.dto.OrderDTO;
import nix.finalproject.carrentalservice.dto.request.OrderRequestDTO;
import nix.finalproject.carrentalservice.entity.Car;
import nix.finalproject.carrentalservice.entity.Client;
import nix.finalproject.carrentalservice.entity.Order;
import nix.finalproject.carrentalservice.exceptions.ExceptionMessages;
import nix.finalproject.carrentalservice.exceptions.ServiceException;
import nix.finalproject.carrentalservice.repository.CarRepository;
import nix.finalproject.carrentalservice.repository.ClientRepository;
import nix.finalproject.carrentalservice.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final ClientRepository clientRepository;
    private final CarRepository carRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository, ClientRepository clientRepository, CarRepository carRepository) {
        this.orderRepository = orderRepository;
        this.clientRepository = clientRepository;
        this.carRepository = carRepository;
    }

    public OrderDTO convertOrderToOrderDTO(Order order) {
        return new OrderDTO(
                order.getId(),
                order.getClient().getFirstName(),
                order.getClient().getLastName(),
                order.getClient().getPhoneNumber(),
                order.getCar().getBrand().getBrandName(),
                order.getCar().getModel(),
                order.getStartOrder(),
                order.getEndOrder(),
                createATotalPrice(order.getCar(), findHowManyDaysClientOrderedACar(order.getStartOrder(), order.getEndOrder()))
        );
    }

    public List<OrderDTO> convertOrderListToOrderDTOList(List<Order> orderList) {
        return orderList.stream().map(this::convertOrderToOrderDTO).collect(Collectors.toList());
    }

    public int findHowManyDaysClientOrderedACar(LocalDate startOrder, LocalDate endOrder) {
        int orderDays;

        Period period = Period.between(startOrder, endOrder);
        orderDays = period.getDays();

        return orderDays;
    }

    public String createATotalPrice(Car car, int orderDays) {
        String priceForDay = car.getPrice();

        String [] array = priceForDay.split(" ");
        String countOfMoneyString = array[0];

        int countOfMoneyInt = Integer.parseInt(countOfMoneyString);

        int totalPriceInt = countOfMoneyInt * orderDays;

        String totalPrice = Integer.toString(totalPriceInt);

        return totalPrice + " " + array[1];
    }

    public List<OrderDTO> findAllOrderDTO() {
        return convertOrderListToOrderDTOList(orderRepository.findAll());
    }

    public OrderDTO findOrderDTOById(Long id) {
        Order order = orderRepository.findById(id).orElseThrow(() -> ServiceException.entityNotFound(ExceptionMessages.CAN_NOT_FIND_ORDER));
        return convertOrderToOrderDTO(order);
    }

    public OrderRequestDTO convertOrderToOrderRequestDTO(Order order) {
        return new OrderRequestDTO(order.getClient().getId(),
                order.getCar().getId(),
                order.getStartOrder(),
                order.getEndOrder());
    }

    public OrderRequestDTO createNewOrder(OrderRequestDTO orderRequestDTO) {

        Client client = clientRepository.findById(orderRequestDTO.getClientId()).orElseThrow(() -> ServiceException.entityNotFound(ExceptionMessages.CAN_NOT_FIND_CLIENT));

        Car car = carRepository.findById(orderRequestDTO.getCarId()).orElseThrow(() -> ServiceException.entityNotFound(ExceptionMessages.CAN_NOT_FIND_CAR));

        Order order = new Order(client, car, orderRequestDTO.getStartOrder(), orderRequestDTO.getEndOrder());

        return convertOrderToOrderRequestDTO(orderRepository.save(order));
    }

    public void updateOrder(Long id, OrderRequestDTO orderRequestDTO) {

        Order order = orderRepository.findById(id).orElseThrow(() -> ServiceException.entityNotFound(ExceptionMessages.CAN_NOT_FIND_ORDER));
        order.setClient(clientRepository.findById(orderRequestDTO.getClientId()).orElseThrow(() -> ServiceException.entityNotFound(ExceptionMessages.CAN_NOT_FIND_CLIENT)));
        order.setCar(carRepository.findById(orderRequestDTO.getCarId()).orElseThrow(() -> ServiceException.entityNotFound(ExceptionMessages.CAN_NOT_FIND_CAR)));
        order.setStartOrder(orderRequestDTO.getStartOrder());
        order.setEndOrder(orderRequestDTO.getEndOrder());

        convertOrderToOrderDTO(orderRepository.save(order));

    }

    public void deleteById(Long id) {
        orderRepository.deleteById(id);
    }

}
