package nix.finalproject.carrentalservice.integrationtests;

import nix.finalproject.carrentalservice.dto.OrderResponseDTO;
import nix.finalproject.carrentalservice.dto.request.OrderRequestDTO;
import nix.finalproject.carrentalservice.entity.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.net.URI;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("integration-test")
public class CarRentalServiceApplicationOrderTests {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate rest;

    @Test
    void contextLoads() {
        assertNotNull(rest);
        assertNotEquals(0, port);
    }

    @Test
    void testCreateOrder() {

        Brand brand = new Brand(1L, "Renault");
        EngineType engineType = new EngineType(8L, "Diesel", "1.5");
        CarType carType = new CarType(2L, "Hatchback");
        String model = "Megan";
        String transmission = "Automatic";
        int yearOfManufacture = 2016;
        CarStatus carStatus = new CarStatus(2L, "Available");
        String price = "35 euro";
        Client client = new Client(1L, "Aleksey", "Alekseev", "+380936435199", "aleks@gmail.com", "password1");
        Car car = new Car(1L, brand, engineType, carType, model, transmission, yearOfManufacture, carStatus, price);

        ResponseEntity<OrderResponseDTO> orderResponseEntity = createNewOrder(client, car, LocalDate.of(2020,10,8), LocalDate.of(2020, 10, 10));

        assertEquals(HttpStatus.CREATED, orderResponseEntity.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, orderResponseEntity.getHeaders().getContentType());

        OrderResponseDTO responseBody = orderResponseEntity.getBody();
        assertNotNull(responseBody);
        assertEquals(client.getLastName(), responseBody.getClientLastName());
        assertEquals(client.getFirstName(), responseBody.getClientFirstName());
        assertEquals(client.getPhoneNumber(), responseBody.getClientPhoneNumber());
        assertEquals(car.getModel(), responseBody.getCarModel());
        assertNotNull(responseBody.getId());
    }

    @Test
    void testGetOrderById() {

        Brand brand = new Brand(1L, "Renault");
        EngineType engineType = new EngineType(8L, "Diesel", "1.5");
        CarType carType = new CarType(2L, "Hatchback");
        String model = "Megan";
        String transmission = "Automatic";
        int yearOfManufacture = 2016;
        CarStatus carStatus = new CarStatus(2L, "Available");
        String price = "35 euro";
        Client client = new Client(1L, "Aleksey", "Alekseev", "+380936435199", "aleks@gmail.com", "password1");
        Car car = new Car(1L, brand, engineType, carType, model, transmission, yearOfManufacture, carStatus, price);

        var order = createNewOrder(client, car, LocalDate.of(2020, 10, 8), LocalDate.of(2020, 10, 10)).getBody();
        assertNotNull(order);

        Long id = order.getId();

        var orderUrl = baseUrl(id);

        ResponseEntity<OrderResponseDTO> orderResponseEntity = rest
                .getForEntity(orderUrl, OrderResponseDTO.class);
        assertEquals(HttpStatus.OK, orderResponseEntity.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, orderResponseEntity.getHeaders().getContentType());

        OrderResponseDTO responseBody = orderResponseEntity.getBody();
        assertNotNull(responseBody);
        assertEquals(client.getLastName(), responseBody.getClientLastName());
        assertEquals(client.getFirstName(), responseBody.getClientFirstName());
        assertEquals(client.getPhoneNumber(), responseBody.getClientPhoneNumber());
        assertEquals(car.getModel(), responseBody.getCarModel());
        assertNotNull(responseBody.getId());

        assertEquals(responseBody, rest.getForEntity(orderUrl, OrderResponseDTO.class).getBody());
    }

    @Test
    void testUpdateOrder() {

        Brand brand = new Brand(1L, "Renault");
        EngineType engineType = new EngineType(8L, "Diesel", "1.5");
        CarType carType = new CarType(2L, "Hatchback");
        String model = "Megan";
        String transmission = "Automatic";
        int yearOfManufacture = 2016;
        CarStatus carStatus = new CarStatus(2L, "Available");
        String price = "35 euro";
        Client client = new Client(1L, "Aleksey", "Alekseev", "+380936435199", "aleks@gmail.com", "password1");
        Car car = new Car(1L, brand, engineType, carType, model, transmission, yearOfManufacture, carStatus, price);

        var order = createNewOrder(client, car, LocalDate.of(2020, 10, 8), LocalDate.of(2020, 10, 10)).getBody();
        assertNotNull(order);

        Long id = order.getId();

        var orderUrl = baseUrl(id);

        var updatedStartOrder = LocalDate.of(2020,10,9);
        var updatedEndOrder = LocalDate.of(2020,10,11);

        rest.put(orderUrl, new OrderRequestDTO(client.getId(), car.getId(), updatedStartOrder, updatedEndOrder));

        ResponseEntity<OrderResponseDTO> orderResponseEntity = rest.getForEntity(orderUrl, OrderResponseDTO.class);
        assertEquals(HttpStatus.OK, orderResponseEntity.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, orderResponseEntity.getHeaders().getContentType());

        OrderResponseDTO responseBody = orderResponseEntity.getBody();
        assertNotNull(responseBody);
        assertEquals(updatedStartOrder, responseBody.getStartOrder());
        assertEquals(updatedEndOrder, responseBody.getEndOrder());
        assertEquals(id, responseBody.getId());
    }

    @Test
    void testDeleteOrder() {

        Brand brand = new Brand(1L, "Renault");
        EngineType engineType = new EngineType(8L, "Diesel", "1.5");
        CarType carType = new CarType(2L, "Hatchback");
        String model = "Megan";
        String transmission = "Automatic";
        int yearOfManufacture = 2016;
        CarStatus carStatus = new CarStatus(2L, "Available");
        String price = "35 euro";
        Client client = new Client(1L, "Aleksey", "Alekseev", "+380936435199", "aleks@gmail.com", "password1");
        Car car = new Car(1L, brand, engineType, carType, model, transmission, yearOfManufacture, carStatus, price);

        var order = createNewOrder(client, car, LocalDate.of(2020, 10, 8), LocalDate.of(2020, 10, 10)).getBody();
        assertNotNull(order);

        Long id = order.getId();

        var orderUrl = baseUrl(id);
        var orderUri = URI.create(orderUrl);

        ResponseEntity<OrderResponseDTO> orderResponseEntity = rest
                .exchange(RequestEntity.delete(orderUri).build(), OrderResponseDTO.class);

        assertEquals(HttpStatus.OK, orderResponseEntity.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, orderResponseEntity.getHeaders().getContentType());

        OrderResponseDTO responseBody = orderResponseEntity.getBody();
        assertNotNull(responseBody);
        assertNotNull(responseBody);
        assertEquals(client.getLastName(), responseBody.getClientLastName());
        assertEquals(client.getFirstName(), responseBody.getClientFirstName());
        assertEquals(client.getPhoneNumber(), responseBody.getClientPhoneNumber());
        assertEquals(car.getModel(), responseBody.getCarModel());
        assertNotNull(responseBody.getId());

        assertEquals(HttpStatus.NOT_FOUND, rest.getForEntity(orderUrl, OrderResponseDTO.class).getStatusCode());
        assertEquals(HttpStatus.NOT_FOUND, rest
                .exchange(RequestEntity.delete(orderUri).build(), OrderResponseDTO.class)
                .getStatusCode());
    }

    @Test
    void testGetNonExistingOrder() {
        var orderUrl = baseUrl(20L);

        ResponseEntity<OrderResponseDTO> orderResponseEntity = rest.getForEntity(orderUrl, OrderResponseDTO.class);

        assertEquals(HttpStatus.NOT_FOUND, orderResponseEntity.getStatusCode());
    }

    private ResponseEntity<OrderResponseDTO> createNewOrder(Client client, Car car, LocalDate startOrder, LocalDate endOrder) {
        var url = baseUrl();
        var requestBody = new OrderRequestDTO();
        requestBody.setClientId(client.getId());
        requestBody.setCarId(car.getId());
        requestBody.setStartOrder(startOrder);
        requestBody.setEndOrder(endOrder);

        return rest.postForEntity(url, requestBody, OrderResponseDTO.class);
    }

    private String baseUrl() {
        return "http://localhost:" + port + "/orders";
    }

    private String baseUrl(Long id) {
        return baseUrl() + '/' + id;
    }
}
