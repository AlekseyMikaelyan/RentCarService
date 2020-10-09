package nix.finalproject.carrentalservice.integrationtests;

import nix.finalproject.carrentalservice.dto.BrandResponseDTO;
import nix.finalproject.carrentalservice.dto.request.BrandRequestDTO;
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

        ResponseEntity<BrandResponseDTO> brandResponseEntity = createNewBrand(brandName);

        assertEquals(HttpStatus.CREATED, brandResponseEntity.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, brandResponseEntity.getHeaders().getContentType());

        BrandResponseDTO responseBody = brandResponseEntity.getBody();
        assertNotNull(responseBody);
        assertEquals(brandName, responseBody.getBrandName());
        assertNotNull(responseBody.getId());
    }

    @Test
    void testGetBrandById() {

        var brandName = "Renault";

        var brand = createNewBrand(brandName).getBody();
        assertNotNull(brand);

        Long id = brand.getId();

        var brandUrl = baseUrl(id);

        ResponseEntity<BrandResponseDTO> brandResponseEntity = rest
                .getForEntity(brandUrl, BrandResponseDTO.class);
        assertEquals(HttpStatus.OK, brandResponseEntity.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, brandResponseEntity.getHeaders().getContentType());

        BrandResponseDTO responseBody = brandResponseEntity.getBody();
        assertNotNull(responseBody);
        assertEquals(brandName, responseBody.getBrandName());
        assertEquals(id, responseBody.getId());

        assertEquals(responseBody, rest.getForEntity(brandUrl, BrandResponseDTO.class).getBody());
    }

    @Test
    void testUpdateBrand() {

        var brandName = "Renault";

        var brand = createNewBrand(brandName).getBody();
        assertNotNull(brand);

        Long id = brand.getId();

        var brandUrl = baseUrl(id);

        var updatedBrandName = "Peugeot";

        rest.put(brandUrl, new BrandRequestDTO(updatedBrandName));

        ResponseEntity<BrandResponseDTO> brandResponseEntity = rest.getForEntity(brandUrl, BrandResponseDTO.class);
        assertEquals(HttpStatus.OK, brandResponseEntity.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, brandResponseEntity.getHeaders().getContentType());

        BrandResponseDTO responseBody = brandResponseEntity.getBody();
        assertNotNull(responseBody);
        assertEquals(updatedBrandName, responseBody.getBrandName());
        assertEquals(id, responseBody.getId());
    }

    @Test
    void testDeleteBrand() {

        var brandName = "Renault";

        var brand = createNewBrand(brandName).getBody();
        assertNotNull(brand);

        Long id = brand.getId();

        var brandUrl = baseUrl(id);
        var brandUri = URI.create(brandUrl);

        ResponseEntity<BrandResponseDTO> brandResponseEntity = rest
                .exchange(RequestEntity.delete(brandUri).build(), BrandResponseDTO.class);

        assertEquals(HttpStatus.OK, brandResponseEntity.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, brandResponseEntity.getHeaders().getContentType());

        BrandResponseDTO responseBody = brandResponseEntity.getBody();
        assertNotNull(responseBody);
        assertEquals(brandName, responseBody.getBrandName());
        assertEquals(id, responseBody.getId());

        assertEquals(HttpStatus.NOT_FOUND, rest.getForEntity(brandUrl, BrandResponseDTO.class).getStatusCode());
        assertEquals(HttpStatus.NOT_FOUND, rest
                .exchange(RequestEntity.delete(brandUri).build(), BrandResponseDTO.class)
                .getStatusCode());
    }

    @Test
    void testGetNonExistingBrand() {
        var brandUrl = baseUrl(20L);

        ResponseEntity<BrandResponseDTO> brandResponseEntity = rest.getForEntity(brandUrl, BrandResponseDTO.class);

        assertEquals(HttpStatus.NOT_FOUND, brandResponseEntity.getStatusCode());
    }

    private ResponseEntity<BrandResponseDTO> createNewBrand(String brandName) {
        var url = baseUrl();
        var requestBody = new BrandRequestDTO();
        requestBody.setBrandName(brandName);

        return rest.postForEntity(url, requestBody, BrandResponseDTO.class);
    }

    private String baseUrl() {
        return "http://localhost:" + port + "/brands";
    }

    private String baseUrl(Long id) {
        return baseUrl() + '/' + id;
    }
}
