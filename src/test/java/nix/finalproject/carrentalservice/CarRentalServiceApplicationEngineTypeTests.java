package nix.finalproject.carrentalservice;

import nix.finalproject.carrentalservice.dto.EngineTypeResponseDTO;
import nix.finalproject.carrentalservice.dto.request.EngineTypeRequestDTO;
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
public class CarRentalServiceApplicationEngineTypeTests {

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
    void testCreateNewEngine() {
        var type = "Diesel";
        var capacity = "1.5";

        ResponseEntity<EngineTypeResponseDTO> engineTypeResponseEntity = createNewEngineType(type, capacity);

        assertEquals(HttpStatus.CREATED, engineTypeResponseEntity.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, engineTypeResponseEntity.getHeaders().getContentType());

        EngineTypeResponseDTO responseBody = engineTypeResponseEntity.getBody();
        assertNotNull(responseBody);
        assertEquals(type, responseBody.getType());
        assertEquals(capacity, responseBody.getEngineCapacity());
        assertNotNull(responseBody.getId());
    }

    @Test
    void testGetEngineById() {

        var type = "Diesel";
        var capacity = "1.5";

        var engine = createNewEngineType(type, capacity).getBody();
        assertNotNull(engine);

        Long id = engine.getId();

        var engineUrl = baseUrl(id);

        ResponseEntity<EngineTypeResponseDTO> engineTypeResponseEntity = rest
                .getForEntity(engineUrl, EngineTypeResponseDTO.class);
        assertEquals(HttpStatus.OK, engineTypeResponseEntity.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, engineTypeResponseEntity.getHeaders().getContentType());

        EngineTypeResponseDTO responseBody = engineTypeResponseEntity.getBody();
        assertNotNull(responseBody);
        assertEquals(type, responseBody.getType());
        assertEquals(capacity, responseBody.getEngineCapacity());
        assertEquals(id, responseBody.getId());

        assertEquals(responseBody, rest.getForEntity(engineUrl, EngineTypeResponseDTO.class).getBody());
    }

    @Test
    void testUpdateEngine() {

        var type = "Diesel";
        var capacity = "1.5";

        var engine = createNewEngineType(type, capacity).getBody();
        assertNotNull(engine);

        Long id = engine.getId();

        var engineUrl = baseUrl(id);

        var updatedType = "Gasoline";
        var updatedCapacity = "1.6";

        rest.put(engineUrl, new EngineTypeRequestDTO(updatedType, updatedCapacity));

        ResponseEntity<EngineTypeResponseDTO> engineTypeResponseEntity = rest.getForEntity(engineUrl, EngineTypeResponseDTO.class);
        assertEquals(HttpStatus.OK, engineTypeResponseEntity.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, engineTypeResponseEntity.getHeaders().getContentType());

        EngineTypeResponseDTO responseBody = engineTypeResponseEntity.getBody();
        assertNotNull(responseBody);
        assertEquals(updatedType, responseBody.getType());
        assertEquals(updatedCapacity, responseBody.getEngineCapacity());
        assertEquals(id, responseBody.getId());
    }

    @Test
    void testDeleteEngine() {

        var type = "Diesel";
        var capacity = "1.5";

        var engine = createNewEngineType(type, capacity).getBody();
        assertNotNull(engine);

        Long id = engine.getId();

        var engineUrl = baseUrl(id);
        var engineUri = URI.create(engineUrl);

        ResponseEntity<EngineTypeResponseDTO> engineTypeResponseEntity = rest
                .exchange(RequestEntity.delete(engineUri).build(), EngineTypeResponseDTO.class);

        assertEquals(HttpStatus.OK, engineTypeResponseEntity.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, engineTypeResponseEntity.getHeaders().getContentType());

        EngineTypeResponseDTO responseBody = engineTypeResponseEntity.getBody();
        assertNotNull(responseBody);
        assertEquals(type, responseBody.getType());
        assertEquals(capacity, responseBody.getEngineCapacity());
        assertEquals(id, responseBody.getId());

        assertEquals(HttpStatus.NOT_FOUND, rest.getForEntity(engineUrl, EngineTypeResponseDTO.class).getStatusCode());
        assertEquals(HttpStatus.NOT_FOUND, rest
                .exchange(RequestEntity.delete(engineUri).build(), EngineTypeResponseDTO.class)
                .getStatusCode());
    }

    @Test
    void testGetNonExistingEngine() {
        var engineUrl = baseUrl(20L);

        ResponseEntity<EngineTypeResponseDTO> engineTypeResponseEntity = rest.getForEntity(engineUrl, EngineTypeResponseDTO.class);

        assertEquals(HttpStatus.NOT_FOUND, engineTypeResponseEntity.getStatusCode());
    }

    private ResponseEntity<EngineTypeResponseDTO> createNewEngineType(String type, String capacity) {
        var url = baseUrl();
        var requestBody = new EngineTypeRequestDTO();
        requestBody.setType(type);
        requestBody.setCapacity(capacity);

        return rest.postForEntity(url, requestBody, EngineTypeResponseDTO.class);
    }

    private String baseUrl() {
        return "http://localhost:" + port + "/engine_types";
    }

    private String baseUrl(Long id) {
        return baseUrl() + '/' + id;
    }
}
