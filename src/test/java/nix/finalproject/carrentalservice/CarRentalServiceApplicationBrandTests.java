package nix.finalproject.carrentalservice;

import nix.finalproject.carrentalservice.dto.request.BrandRequestDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("integration-test")
class CarRentalServiceApplicationBrandTests {

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
    void testCreateNewBrand() {
        var brandName = "Renault";

        ResponseEntity<BrandRequestDTO> brandResponseEntity = createNewBrand(brandName);

        assertEquals(HttpStatus.CREATED, brandResponseEntity.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, brandResponseEntity.getHeaders().getContentType());

        BrandRequestDTO responseBody = brandResponseEntity.getBody();
        assertNotNull(responseBody);
        assertEquals(brandName, responseBody.getBrandName());
    }

    private ResponseEntity<BrandRequestDTO> createNewBrand(String brandName) {
        var url = baseUrl();
        var requestBody = new BrandRequestDTO();
        requestBody.setBrandName(brandName);

        return rest.postForEntity(url, requestBody, BrandRequestDTO.class);
    }

    private String baseUrl() {
        return "http://localhost:" + port + "/brands";
    }

    private String baseUrl(Long id) {
        return baseUrl() + '/' + id;
    }

}
