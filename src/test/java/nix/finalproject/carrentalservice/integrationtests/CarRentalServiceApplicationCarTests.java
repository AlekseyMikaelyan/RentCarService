package nix.finalproject.carrentalservice.integrationtests;

import nix.finalproject.carrentalservice.dto.CarResponseDTO;
import nix.finalproject.carrentalservice.dto.request.CarRequestDTO;
import nix.finalproject.carrentalservice.entity.Brand;
import nix.finalproject.carrentalservice.entity.CarStatus;
import nix.finalproject.carrentalservice.entity.CarType;
import nix.finalproject.carrentalservice.entity.EngineType;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("integration-test")
public class CarRentalServiceApplicationCarTests {

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
    void testCreateCar() {

        Brand brand = new Brand(1L, "Renault");
        EngineType engineType = new EngineType(8L, "Diesel", "1.5");
        CarType carType = new CarType(2L, "Hatchback");
        String model = "Megan";
        String transmission = "Automatic";
        int yearOfManufacture = 2016;
        CarStatus carStatus = new CarStatus(2L, "Available");
        String price = "35 euro";

        ResponseEntity<CarResponseDTO> carResponseEntity = createNewCar(brand, engineType, carType, model, transmission, yearOfManufacture, carStatus, price);

        assertEquals(HttpStatus.CREATED, carResponseEntity.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, carResponseEntity.getHeaders().getContentType());

        CarResponseDTO responseBody = carResponseEntity.getBody();
        assertNotNull(responseBody);
        assertEquals(brand.getBrandName(), responseBody.getBrand());
        assertEquals(engineType.getType(), responseBody.getEngineType());
        assertEquals(carType.getBodyType(), responseBody.getCarType());
        assertEquals(model, responseBody.getModel());
        assertEquals(transmission, responseBody.getTransmission());
        assertEquals(price, responseBody.getPrice());
        assertNotNull(responseBody.getId());
    }

    @Test
    void testGetCarById() {

        Brand brand = new Brand(1L, "Renault");
        EngineType engineType = new EngineType(8L, "Diesel", "1.5");
        CarType carType = new CarType(2L, "Hatchback");
        String model = "Megan";
        String transmission = "Automatic";
        int yearOfManufacture = 2016;
        CarStatus carStatus = new CarStatus(2L, "Available");
        String price = "35 euro";

        var car = createNewCar(brand, engineType, carType, model, transmission, yearOfManufacture, carStatus, price).getBody();
        assertNotNull(car);

        Long id = car.getId();

        var carUrl = baseUrl(id);

        ResponseEntity<CarResponseDTO> carResponseEntity = rest
                .getForEntity(carUrl, CarResponseDTO.class);
        assertEquals(HttpStatus.OK, carResponseEntity.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, carResponseEntity.getHeaders().getContentType());

        CarResponseDTO responseBody = carResponseEntity.getBody();
        assertNotNull(responseBody);
        assertEquals(brand.getBrandName(), responseBody.getBrand());
        assertEquals(engineType.getType(), responseBody.getEngineType());
        assertEquals(carType.getBodyType(), responseBody.getCarType());
        assertEquals(model, responseBody.getModel());
        assertEquals(transmission, responseBody.getTransmission());
        assertEquals(price, responseBody.getPrice());
        assertNotNull(responseBody.getId());

        assertEquals(responseBody, rest.getForEntity(carUrl, CarResponseDTO.class).getBody());
    }

    @Test
    void testUpdateCar() {

        Brand brand = new Brand(1L, "Renault");
        EngineType engineType = new EngineType(8L, "Diesel", "1.5");
        CarType carType = new CarType(2L, "Hatchback");
        String model = "Megan";
        String transmission = "Automatic";
        int yearOfManufacture = 2016;
        CarStatus carStatus = new CarStatus(2L, "Available");
        String price = "35 euro";

        var car = createNewCar(brand, engineType, carType, model, transmission, yearOfManufacture, carStatus, price).getBody();
        assertNotNull(car);

        Long id = car.getId();

        var carUrl = baseUrl(id);

        var updatedTransmission = "Manual";
        var updatedPrice = "30 euro";

        rest.put(carUrl, new CarRequestDTO(brand.getId(), engineType.getId(), carType.getId(), model, updatedTransmission, yearOfManufacture, carStatus.getId(), updatedPrice));

        ResponseEntity<CarResponseDTO> carResponseEntity = rest.getForEntity(carUrl, CarResponseDTO.class);
        assertEquals(HttpStatus.OK, carResponseEntity.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, carResponseEntity.getHeaders().getContentType());

        CarResponseDTO responseBody = carResponseEntity.getBody();
        assertNotNull(responseBody);
        assertEquals(updatedTransmission, responseBody.getTransmission());
        assertEquals(updatedPrice, responseBody.getPrice());
        assertEquals(id, responseBody.getId());
    }

    @Test
    void testDeleteCar() {

        Brand brand = new Brand(1L, "Renault");
        EngineType engineType = new EngineType(8L, "Diesel", "1.5");
        CarType carType = new CarType(2L, "Hatchback");
        String model = "Megan";
        String transmission = "Automatic";
        int yearOfManufacture = 2016;
        CarStatus carStatus = new CarStatus(2L, "Available");
        String price = "35 euro";

        var car = createNewCar(brand, engineType, carType, model, transmission, yearOfManufacture, carStatus, price).getBody();
        assertNotNull(car);

        Long id = car.getId();

        var carUrl = baseUrl(id);
        var carUri = URI.create(carUrl);

        ResponseEntity<CarResponseDTO> carResponseEntity = rest
                .exchange(RequestEntity.delete(carUri).build(), CarResponseDTO.class);

        assertEquals(HttpStatus.OK, carResponseEntity.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, carResponseEntity.getHeaders().getContentType());

        CarResponseDTO responseBody = carResponseEntity.getBody();
        assertNotNull(responseBody);
        assertEquals(brand.getBrandName(), responseBody.getBrand());
        assertEquals(engineType.getType(), responseBody.getEngineType());
        assertEquals(carType.getBodyType(), responseBody.getCarType());
        assertEquals(model, responseBody.getModel());
        assertEquals(transmission, responseBody.getTransmission());
        assertEquals(price, responseBody.getPrice());
        assertNotNull(responseBody.getId());

        assertEquals(HttpStatus.NOT_FOUND, rest.getForEntity(carUrl, CarResponseDTO.class).getStatusCode());
        assertEquals(HttpStatus.NOT_FOUND, rest
                .exchange(RequestEntity.delete(carUri).build(), CarResponseDTO.class)
                .getStatusCode());
    }

    @Test
    void testGetNonExistingCar() {
        var carUrl = baseUrl(20L);

        ResponseEntity<CarResponseDTO> carResponseEntity = rest.getForEntity(carUrl, CarResponseDTO.class);

        assertEquals(HttpStatus.NOT_FOUND, carResponseEntity.getStatusCode());
    }

    private ResponseEntity<CarResponseDTO> createNewCar(Brand brand, EngineType engineType, CarType carType, String model, String transmission, int yearOfManufacture, CarStatus carStatus, String price) {
        var url = baseUrl();
        var requestBody = new CarRequestDTO();
        requestBody.setBrandId(brand.getId());
        requestBody.setEngineTypeId(engineType.getId());
        requestBody.setCarTypeId(carType.getId());
        requestBody.setModel(model);
        requestBody.setTransmission(transmission);
        requestBody.setYearOfManufacture(yearOfManufacture);
        requestBody.setCarStatusId(carStatus.getId());
        requestBody.setPrice(price);

        return rest.postForEntity(url, requestBody, CarResponseDTO.class);
    }

    private String baseUrl() {
        return "http://localhost:" + port + "/cars";
    }

    private String baseUrl(Long id) {
        return baseUrl() + '/' + id;
    }
}
