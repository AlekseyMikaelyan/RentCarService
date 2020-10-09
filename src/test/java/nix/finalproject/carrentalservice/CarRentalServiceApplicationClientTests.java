package nix.finalproject.carrentalservice;

import nix.finalproject.carrentalservice.dto.ClientResponseDTO;
import nix.finalproject.carrentalservice.dto.request.ClientRequestDTO;
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
public class CarRentalServiceApplicationClientTests {

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
    void testCreateClient() {

        var firstName = "Sergey";
        var lastName = "Sergeev";
        var phoneNumber = "+380990964311";
        var email = "serg@gmail.com";
        var password = "password1";

        ResponseEntity<ClientResponseDTO> clientResponseEntity = createNewClient(firstName, lastName, phoneNumber, email, password);

        assertEquals(HttpStatus.CREATED, clientResponseEntity.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, clientResponseEntity.getHeaders().getContentType());

        ClientResponseDTO responseBody = clientResponseEntity.getBody();
        assertNotNull(responseBody);
        assertEquals(firstName, responseBody.getFirstName());
        assertEquals(lastName, responseBody.getLastName());
        assertEquals(phoneNumber, responseBody.getPhoneNumber());
        assertEquals(email, responseBody.getEmail());
        assertNotNull(responseBody.getId());
    }

    @Test
    void testGetClientById() {

        var firstName = "Sergey";
        var lastName = "Sergeev";
        var phoneNumber = "+380990964311";
        var email = "serg@gmail.com";
        var password = "password1";

        var client = createNewClient(firstName, lastName, phoneNumber, email, password).getBody();
        assertNotNull(client);

        Long id = client.getId();

        var clientUrl = baseUrl(id);

        ResponseEntity<ClientResponseDTO> clientResponseEntity = rest
                .getForEntity(clientUrl, ClientResponseDTO.class);
        assertEquals(HttpStatus.OK, clientResponseEntity.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, clientResponseEntity.getHeaders().getContentType());

        ClientResponseDTO responseBody = clientResponseEntity.getBody();
        assertNotNull(responseBody);
        assertEquals(firstName, responseBody.getFirstName());
        assertEquals(lastName, responseBody.getLastName());
        assertEquals(phoneNumber, responseBody.getPhoneNumber());
        assertEquals(email, responseBody.getEmail());
        assertEquals(id, responseBody.getId());

        assertEquals(responseBody, rest.getForEntity(clientUrl, ClientResponseDTO.class).getBody());
    }

    @Test
    void testUpdateClient() {

        var firstName = "Sergey";
        var lastName = "Sergeev";
        var phoneNumber = "+380990964311";
        var email = "serg@gmail.com";
        var password = "password1";

        var client = createNewClient(firstName,lastName,phoneNumber,email,password).getBody();
        assertNotNull(client);

        Long id = client.getId();

        var clientUrl = baseUrl(id);

        var updatedFirstName = "Aleks";
        var updatedLastName = "Alekseev";
        var updatedPhoneNumber = "+380936435199";
        var updatedEmail = "aleks@gmail.com";
        var updatedPassword = "password2";

        rest.put(clientUrl, new ClientRequestDTO(updatedFirstName,updatedLastName,updatedPhoneNumber,updatedEmail,updatedPassword));

        ResponseEntity<ClientResponseDTO> clientResponseEntity = rest.getForEntity(clientUrl, ClientResponseDTO.class);
        assertEquals(HttpStatus.OK, clientResponseEntity.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, clientResponseEntity.getHeaders().getContentType());

        ClientResponseDTO responseBody = clientResponseEntity.getBody();
        assertNotNull(responseBody);
        assertEquals(updatedFirstName, responseBody.getFirstName());
        assertEquals(updatedLastName, responseBody.getLastName());
        assertEquals(updatedEmail, responseBody.getEmail());
        assertEquals(updatedPhoneNumber, responseBody.getPhoneNumber());
        assertEquals(id, responseBody.getId());
    }

    @Test
    void testDeleteClient() {

        var firstName = "Sergey";
        var lastName = "Sergeev";
        var phoneNumber = "+380990964311";
        var email = "serg@gmail.com";
        var password = "password1";

        var client = createNewClient(firstName,lastName,phoneNumber,email,password).getBody();
        assertNotNull(client);

        Long id = client.getId();

        var clientUrl = baseUrl(id);
        var clientUri = URI.create(clientUrl);

        ResponseEntity<ClientResponseDTO> clientResponseEntity = rest
                .exchange(RequestEntity.delete(clientUri).build(), ClientResponseDTO.class);

        assertEquals(HttpStatus.OK, clientResponseEntity.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, clientResponseEntity.getHeaders().getContentType());

        ClientResponseDTO responseBody = clientResponseEntity.getBody();
        assertNotNull(responseBody);
        assertEquals(firstName, responseBody.getFirstName());
        assertEquals(lastName, responseBody.getLastName());
        assertEquals(phoneNumber, responseBody.getPhoneNumber());
        assertEquals(email, responseBody.getEmail());
        assertEquals(id, responseBody.getId());

        assertEquals(HttpStatus.NOT_FOUND, rest.getForEntity(clientUrl, ClientResponseDTO.class).getStatusCode());
        assertEquals(HttpStatus.NOT_FOUND, rest
                .exchange(RequestEntity.delete(clientUri).build(), ClientResponseDTO.class)
                .getStatusCode());
    }

    @Test
    void testGetNonExistingClient() {
        var clientUrl = baseUrl(20L);

        ResponseEntity<ClientResponseDTO> clientResponseEntity = rest.getForEntity(clientUrl, ClientResponseDTO.class);

        assertEquals(HttpStatus.NOT_FOUND, clientResponseEntity.getStatusCode());
    }

    private ResponseEntity<ClientResponseDTO> createNewClient(String firstName, String lastName, String phoneNumber, String email, String password) {
        var url = baseUrl();
        var requestBody = new ClientRequestDTO();
        requestBody.setFirstName(firstName);
        requestBody.setLastName(lastName);
        requestBody.setPhoneNumber(phoneNumber);
        requestBody.setEmail(email);
        requestBody.setPassword(password);

        return rest.postForEntity(url, requestBody, ClientResponseDTO.class);
    }

    private String baseUrl() {
        return "http://localhost:" + port + "/clients";
    }

    private String baseUrl(Long id) {
        return baseUrl() + '/' + id;
    }
}
