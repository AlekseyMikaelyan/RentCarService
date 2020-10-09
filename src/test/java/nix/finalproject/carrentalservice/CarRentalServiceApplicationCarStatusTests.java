package nix.finalproject.carrentalservice;

import nix.finalproject.carrentalservice.dto.CarStatusResponseDTO;
import nix.finalproject.carrentalservice.dto.request.CarStatusRequestDTO;
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
public class CarRentalServiceApplicationCarStatusTests {

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
    void testCreateNewCarStatus() {
        var status = "Broken";

        ResponseEntity<CarStatusResponseDTO> carStatusResponseEntity = createNewCarStatus(status);

        assertEquals(HttpStatus.CREATED, carStatusResponseEntity.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, carStatusResponseEntity.getHeaders().getContentType());

        CarStatusResponseDTO responseBody = carStatusResponseEntity.getBody();
        assertNotNull(responseBody);
        assertEquals(status, responseBody.getStatus());
        assertNotNull(responseBody.getId());
    }

    @Test
    void testGetCarStatusById() {

        var status = "Broken";

        var carStatus = createNewCarStatus(status).getBody();
        assertNotNull(carStatus);

        Long id = carStatus.getId();

        var statusUrl = baseUrl(id);

        ResponseEntity<CarStatusResponseDTO> carStatusResponseEntity = rest
                .getForEntity(statusUrl, CarStatusResponseDTO.class);
        assertEquals(HttpStatus.OK, carStatusResponseEntity.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, carStatusResponseEntity.getHeaders().getContentType());

        CarStatusResponseDTO responseBody = carStatusResponseEntity.getBody();
        assertNotNull(responseBody);
        assertEquals(status, responseBody.getStatus());
        assertEquals(id, responseBody.getId());

        assertEquals(responseBody, rest.getForEntity(statusUrl, CarStatusResponseDTO.class).getBody());
    }

    @Test
    void testUpdateCarStatus() {

        var status = "Broken";

        var carStatus = createNewCarStatus(status).getBody();
        assertNotNull(carStatus);

        Long id = carStatus.getId();

        var statusUrl = baseUrl(id);

        var updatedStatus = "Available";

        rest.put(statusUrl, new CarStatusRequestDTO(updatedStatus));

        ResponseEntity<CarStatusResponseDTO> carStatusResponseEntity = rest.getForEntity(statusUrl, CarStatusResponseDTO.class);
        assertEquals(HttpStatus.OK, carStatusResponseEntity.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, carStatusResponseEntity.getHeaders().getContentType());

        CarStatusResponseDTO responseBody = carStatusResponseEntity.getBody();
        assertNotNull(responseBody);
        assertEquals(updatedStatus, responseBody.getStatus());
        assertEquals(id, responseBody.getId());
    }

    @Test
    void testDeleteCarStatus() {

        var status = "Available";

        var carStatus = createNewCarStatus(status).getBody();
        assertNotNull(carStatus);

        Long id = carStatus.getId();

        var statusUrl = baseUrl(id);
        var statusUri = URI.create(statusUrl);

        ResponseEntity<CarStatusResponseDTO> carStatusResponseEntity = rest
                .exchange(RequestEntity.delete(statusUri).build(), CarStatusResponseDTO.class);

        assertEquals(HttpStatus.OK, carStatusResponseEntity.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, carStatusResponseEntity.getHeaders().getContentType());

        CarStatusResponseDTO responseBody = carStatusResponseEntity.getBody();
        assertNotNull(responseBody);
        assertEquals(status, responseBody.getStatus());
        assertEquals(id, responseBody.getId());

        assertEquals(HttpStatus.NOT_FOUND, rest.getForEntity(statusUrl, CarStatusResponseDTO.class).getStatusCode());
        assertEquals(HttpStatus.NOT_FOUND, rest
                .exchange(RequestEntity.delete(statusUri).build(), CarStatusResponseDTO.class)
                .getStatusCode());
    }

    @Test
    void testGetNonExistingCarStatus() {
        var statusUrl = baseUrl(20L);

        ResponseEntity<CarStatusResponseDTO> carStatusResponseEntity = rest.getForEntity(statusUrl, CarStatusResponseDTO.class);

        assertEquals(HttpStatus.NOT_FOUND, carStatusResponseEntity.getStatusCode());
    }

    private ResponseEntity<CarStatusResponseDTO> createNewCarStatus(String status) {
        var url = baseUrl();
        var requestBody = new CarStatusRequestDTO();
        requestBody.setStatus(status);

        return rest.postForEntity(url, requestBody, CarStatusResponseDTO.class);
    }

    private String baseUrl() {
        return "http://localhost:" + port + "/car_status";
    }

    private String baseUrl(Long id) {
        return baseUrl() + '/' + id;
    }
}
