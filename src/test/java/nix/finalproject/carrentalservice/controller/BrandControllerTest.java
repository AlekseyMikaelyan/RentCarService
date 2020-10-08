package nix.finalproject.carrentalservice.controller;

import nix.finalproject.carrentalservice.dto.BrandResponseDTO;
import nix.finalproject.carrentalservice.dto.request.BrandRequestDTO;
import nix.finalproject.carrentalservice.service.BrandServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Optional;

import static org.mockito.Mockito.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class BrandControllerTest {

    private MockMvc mockMvc;

    private BrandServiceImpl brandService;

    @BeforeEach
    void setUp() {
        brandService = mock(BrandServiceImpl.class);
        mockMvc = MockMvcBuilders
                .standaloneSetup(new BrandController(brandService))
                .build();
    }

    @Test
    public void getBrandByIdTest() throws Exception {

        Long id = 1L;
        var response = new BrandResponseDTO(id, "Renault");

        when(brandService.getById(id)).thenReturn(Optional.of(response));

        var expectedJson = "{\"id\":" + id + ",\"brandName\":\"Renault\"}";

        mockMvc.perform(get("/brands/" + id))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expectedJson));

        verify(brandService, only()).getById(id);
    }

    @Test
    public void getBrandByAbsentIdTest() throws Exception {
        final Long id = 1L;

        when(brandService.getById(id)).thenReturn(Optional.empty());

        mockMvc.perform(get("/brands/" + id))
                .andExpect(status().isNotFound());

        verify(brandService, only()).getById(id);
    }

    @Test
    public void deleteBrandTest() throws Exception {

        Long id = 1L;
        var response = new BrandResponseDTO(id, "Renault");
        var expectedJson = "{\"id\":" + id + ",\"brandName\":\"Renault\"}";

        when(brandService.deleteById(id))
                .thenReturn(Optional.of(response))
                .thenReturn(Optional.empty());

        mockMvc.perform(delete("/brands/" + id))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedJson));

        mockMvc.perform(delete("/brands/" + id))
                .andExpect(status().isNotFound());

        verify(brandService, times(2)).deleteById(id);
        verifyNoMoreInteractions(brandService);
    }

    @Test
    public void createNewBrandTest() throws Exception {

        var request = new BrandRequestDTO("Renault");
        Long id = 1L;
        var response = new BrandResponseDTO(id, "Renault");

        when(brandService.create(request)).thenReturn(response);

        var expectedJson = "{\"id\":" + id + ",\"brandName\":\"Renault\"}";

        mockMvc.perform(post("/brands")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"brandName\":\"Renault\"}"))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expectedJson));

        verify(brandService, only()).create(request);
    }

    @Test
    public void updateBrandTest() throws Exception {
        var request = new BrandRequestDTO("Renault");
        Long id = 1L;

        mockMvc.perform(put("/brands/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"brandName\":\"Renault\"}"))
                .andExpect(status().isNoContent());

        verify(brandService, only()).update(id, request);
    }
}
