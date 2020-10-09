package nix.finalproject.carrentalservice;

import nix.finalproject.carrentalservice.dto.CarTypeResponseDTO;
import nix.finalproject.carrentalservice.dto.request.CarTypeRequestDTO;
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
public class CarRentalServiceApplicationCarTypeTests {

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
    void testCreateNewCarType() {
        var bodyType = "Sedan";

        ResponseEntity<CarTypeResponseDTO> carTypeResponseEntity = createNewCarType(bodyType);

        assertEquals(HttpStatus.CREATED, carTypeResponseEntity.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, carTypeResponseEntity.getHeaders().getContentType());

        CarTypeResponseDTO responseBody = carTypeResponseEntity.getBody();
        assertNotNull(responseBody);
        assertEquals(bodyType, responseBody.getBodyType());
        assertNotNull(responseBody.getId());
    }

    @Test
    void testGetCarTypeById() {

        var bodyType = "Sedan";

        var carType = createNewCarType(bodyType).getBody();
        assertNotNull(carType);

        Long id = carType.getId();

        var typeUrl = baseUrl(id);

        ResponseEntity<CarTypeResponseDTO> carTypeResponseEntity = rest
                .getForEntity(typeUrl, CarTypeResponseDTO.class);
        assertEquals(HttpStatus.OK, carTypeResponseEntity.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, carTypeResponseEntity.getHeaders().getContentType());

        CarTypeResponseDTO responseBody = carTypeResponseEntity.getBody();
        assertNotNull(responseBody);
        assertEquals(bodyType, responseBody.getBodyType());
        assertEquals(id, responseBody.getId());

        assertEquals(responseBody, rest.getForEntity(typeUrl, CarTypeResponseDTO.class).getBody());
    }

    @Test
    void testUpdateCarType() {

        var bodyType = "Sedan";

        var carType = createNewCarType(bodyType).getBody();
        assertNotNull(carType);

        Long id = carType.getId();

        var typeUrl = baseUrl(id);

        var updatedType = "Minivan";

        rest.put(typeUrl, new CarTypeRequestDTO(updatedType));

        ResponseEntity<CarTypeResponseDTO> carTypeResponseEntity = rest.getForEntity(typeUrl, CarTypeResponseDTO.class);
        assertEquals(HttpStatus.OK, carTypeResponseEntity.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, carTypeResponseEntity.getHeaders().getContentType());

        CarTypeResponseDTO responseBody = carTypeResponseEntity.getBody();
        assertNotNull(responseBody);
        assertEquals(updatedType, responseBody.getBodyType());
        assertEquals(id, responseBody.getId());
    }

    @Test
    void testDeleteCarType() {

        var type = "Sedan";

        var carType = createNewCarType(type).getBody();
        assertNotNull(carType);

        Long id = carType.getId();

        var typeUrl = baseUrl(id);
        var typeUri = URI.create(typeUrl);

        ResponseEntity<CarTypeResponseDTO> carTypeResponseEntity = rest
                .exchange(RequestEntity.delete(typeUri).build(), CarTypeResponseDTO.class);

        assertEquals(HttpStatus.OK, carTypeResponseEntity.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, carTypeResponseEntity.getHeaders().getContentType());

        CarTypeResponseDTO responseBody = carTypeResponseEntity.getBody();
        assertNotNull(responseBody);
        assertEquals(type, responseBody.getBodyType());
        assertEquals(id, responseBody.getId());

        assertEquals(HttpStatus.NOT_FOUND, rest.getForEntity(typeUrl, CarTypeResponseDTO.class).getStatusCode());
        assertEquals(HttpStatus.NOT_FOUND, rest
                .exchange(RequestEntity.delete(typeUri).build(), CarTypeResponseDTO.class)
                .getStatusCode());
    }

    @Test
    void testGetNonExistingCarType() {
        var typeUrl = baseUrl(20L);

        ResponseEntity<CarTypeResponseDTO> carTypeResponseEntity = rest.getForEntity(typeUrl, CarTypeResponseDTO.class);

        assertEquals(HttpStatus.NOT_FOUND, carTypeResponseEntity.getStatusCode());
    }

    private ResponseEntity<CarTypeResponseDTO> createNewCarType(String bodyType) {
        var url = baseUrl();
        var requestBody = new CarTypeRequestDTO();
        requestBody.setBodyType(bodyType);

        return rest.postForEntity(url, requestBody, CarTypeResponseDTO.class);
    }

    private String baseUrl() {
        return "http://localhost:" + port + "/car_types";
    }

    private String baseUrl(Long id) {
        return baseUrl() + '/' + id;
    }
}
