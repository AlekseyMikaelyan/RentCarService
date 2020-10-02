package nix.finalproject.carrentalservice.service;

import nix.finalproject.carrentalservice.dto.OrderDTO;
import nix.finalproject.carrentalservice.entity.*;
import nix.finalproject.carrentalservice.exceptions.ExceptionMessages;
import nix.finalproject.carrentalservice.exceptions.ServiceException;
import nix.finalproject.carrentalservice.repository.CarRepository;
import nix.finalproject.carrentalservice.repository.ClientRepository;
import nix.finalproject.carrentalservice.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private CarRepository carRepository;

    private final OrderService orderService;

    public OrderServiceTest() {
        MockitoAnnotations.initMocks(this);
        this.orderService = new OrderService(orderRepository, clientRepository, carRepository);
    }

    @Test
    public void methodShouldReturnCorrectCarModelFromOrder() {
        Long id1 = 1L;
        Long id2 = 2L;

        Client client1 = new Client(id1, "Aleksey", "Alekseev", "+380936435199", "aleks@gmail.com", "password1");
        Client client2 = new Client(id1, "Sergey", "Sergeev", "+380990964311", "ser@gmail.com", "password2");

        Brand brand1 = new Brand(id1, "Renault");
        Brand brand2 = new Brand(id2, "Peugeot");

        EngineType engineType1 = new EngineType(id1, "Diesel", "1.5");
        EngineType engineType2 = new EngineType(id2, "Gasoline", "1.4");

        CarType carType1 = new CarType(id1, "Hatchback");
        CarType carType2 = new CarType(id2, "Sedan");

        CarStatus carStatus1 = new CarStatus(id1, "Available");
        CarStatus carStatus2 = new CarStatus(id2, "Broken");

        Car car1 = new Car(id1, brand1, engineType1, carType1, "Kangoo", "Manual", 2006, carStatus1, "20 euro");
        Car car2 = new Car(id2, brand2, engineType2, carType2, "206", "Manual", 2006, carStatus2, "25 euro");

        Order order1 = new Order(id1, client1, car1, LocalDate.of(2020, 9, 29), LocalDate.of(2020,10,1));
        Order order2 = new Order(id2, client2, car2, LocalDate.of(2020,10,2), LocalDate.of(2020,10,5));

        given(orderRepository.findById(id1)).willReturn(Optional.of(order1));
        given(orderRepository.findById(id2)).willReturn(Optional.of(order2));

        OrderDTO orderDTO1 = orderService.findOrderDTOById(id1);
        OrderDTO orderDTO2 = orderService.findOrderDTOById(id2);

        assertEquals("Kangoo", orderDTO1.getCarModel());
        assertEquals("206", orderDTO2.getCarModel());

        verify(orderRepository).findById(id1);
        verify(orderRepository).findById(id2);
    }

    @Test()
    public void methodShouldThrowException() {
        Long absentId = 20L;
        Long presentId = 1L;

        Client client = new Client(presentId, "Aleksey", "Alekseev", "+380936435199", "aleks@gmail.com", "password1");
        Brand brand = new Brand(presentId, "Audi");
        EngineType engineType = new EngineType(presentId, "Diesel", "1.5");
        CarType carType = new CarType(presentId, "Hatchback");
        CarStatus carStatus = new CarStatus(presentId, "Available");
        Car car = new Car(brand, engineType, carType, "A1", "Automatic", 2017, carStatus, "40 euro");

        Order order = new Order(presentId, client, car, LocalDate.of(2020,9,29), LocalDate.of(2020,10,1));

        given(orderRepository.findById(absentId)).willThrow(ServiceException.entityNotFound(ExceptionMessages.CAN_NOT_FIND_ORDER));
        given(orderRepository.findById(presentId)).willReturn(Optional.of(order));

        assertThrows(ResponseStatusException.class, () -> orderService.findOrderDTOById(absentId));

        verify(orderRepository).findById(absentId);
    }

    @Test
    public void methodShouldReturnAllOrders() {
        List<Order> orderList = new ArrayList<>();

        Long id1 = 1L;
        Long id2 = 2L;
        Long id3 = 3L;

        Client client1 = new Client(1L, "Aleksey", "Alekseev", "+380936435199", "aleks@gmail.com", "password1");
        Client client2 = new Client(2L, "Sergey", "Sergeev", "+380990964311", "ser@gmail.com", "password2");
        Client client3 = new Client(3L, "Andrey", "Andreev", "+380990964315", "andr@gmail.com", "password3");

        Brand brand1 = new Brand(id1, "Renault");
        Brand brand2 = new Brand(id2, "Peugeot");
        Brand brand3 = new Brand(id3, "Audi");

        EngineType engineType1 = new EngineType(id1, "Diesel", "1.5");
        EngineType engineType2 = new EngineType(id2, "Gasoline", "1.4");
        EngineType engineType3 = new EngineType(id3, "Diesel", "2.0");

        CarType carType1 = new CarType(id1, "Hatchback");
        CarType carType2 = new CarType(id2, "Sedan");
        CarType carType3 = new CarType(id3, "SUV");

        CarStatus carStatus1 = new CarStatus(id1, "Available");
        CarStatus carStatus2 = new CarStatus(id2, "Broken");
        CarStatus carStatus3 = new CarStatus(id3, "Unavailable");

        Car car1 = new Car(id1, brand1, engineType1, carType1, "Kangoo", "Manual", 2006, carStatus1, "20 euro");
        Car car2 = new Car(id2, brand2, engineType2, carType2, "206", "Manual", 2006, carStatus2, "25 euro");
        Car car3 = new Car(id3, brand3, engineType3, carType3, "A1", "Automatic", 2017, carStatus3, "40 euro");

        orderList.add(new Order(id1, client1, car1, LocalDate.of(2020,9,29), LocalDate.of(2020,10,1)));
        orderList.add(new Order(id2, client2, car2, LocalDate.of(2020,10,2), LocalDate.of(2020,10,5)));
        orderList.add(new Order(id3, client3, car3, LocalDate.of(2020,10,7), LocalDate.of(2020,10,10)));

        given(orderRepository.findAll()).willReturn(orderList);

        List<OrderDTO> orderDTOList = orderService.findAllOrderDTO();

        assertEquals(orderList.get(0).getEndOrder(), orderDTOList.get(0).getEndOrder());
        assertEquals(orderList.get(1).getEndOrder(), orderDTOList.get(1).getEndOrder());
        assertEquals(orderList.get(2).getEndOrder(), orderDTOList.get(2).getEndOrder());
    }

    @Test
    public void methodShouldDeleteCar() {
        Long presentId = 1L;
        Client client = new Client(presentId, "Aleksey", "Alekseev", "+380936435199", "aleks@gmail.com", "password1");
        Brand brand = new Brand(presentId, "Audi");
        EngineType engineType = new EngineType(presentId, "Diesel", "1.5");
        CarType carType = new CarType(presentId, "Hatchback");
        CarStatus carStatus = new CarStatus(presentId, "Available");
        Car car = new Car(brand, engineType, carType, "A1", "Automatic", 2017, carStatus, "40 euro");

        Order order = new Order(presentId, client, car, LocalDate.of(2020,9,29), LocalDate.of(2020,10,1));

        when(orderRepository.findById(presentId)).thenReturn(Optional.of(order));

        orderService.deleteById(presentId);

        verify(orderRepository).deleteById(presentId);
    }
}
