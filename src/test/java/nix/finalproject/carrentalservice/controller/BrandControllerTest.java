package nix.finalproject.carrentalservice.controller;

import nix.finalproject.carrentalservice.dto.BrandDTO;
import nix.finalproject.carrentalservice.dto.request.BrandRequestDTO;
import nix.finalproject.carrentalservice.entity.Brand;
import nix.finalproject.carrentalservice.exceptions.ExceptionMessages;
import nix.finalproject.carrentalservice.exceptions.ServiceException;
import nix.finalproject.carrentalservice.service.BrandService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BrandController.class)
public class BrandControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BrandService brandService;

    @Test
    public void findBrandByIdTest() throws Exception {
        final Long id = 1L;
        BrandDTO brandDTO = new BrandDTO(id, "Renault");

        when(brandService.findBrandDTOById(id)).thenReturn(brandDTO);

        var expectedJson = "{\"id\":" + id + ",\"brandName\":\"Renault\"}";

        mockMvc.perform(get("/brands/" + id))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expectedJson));

        verify(brandService, only()).findBrandDTOById(id);
    }

    @Test
    public void findBrandByAbsentIdTest() throws Exception {
        final Long id = 1L;

        when(brandService.findBrandDTOById(id)).thenThrow(ServiceException.entityNotFound(ExceptionMessages.CAN_NOT_FIND_BRAND));

        mockMvc.perform(get("/brands/" + id))
                .andExpect(status().isNotFound());

        verify(brandService, only()).findBrandDTOById(id);
    }

    @Test
    public void deleteBrandTest() throws Exception {
        Long id = 1L;
        BrandDTO brandDTO = new BrandDTO(id, "Renault");

        when(brandService.findBrandDTOById(id)).thenReturn(brandDTO);
        doNothing().when(brandService).deleteById(id);

        mockMvc.perform(delete("/brands/" + id)).andExpect(status().isOk());

        verify(brandService, only()).deleteById(id);
    }

    @Test
    public void createNewBrandTest() throws Exception {
        Long id = 1L;
        var request = new Brand(id, "Renault");
        var response = new BrandRequestDTO("Renault");

        when(brandService.createNewBrand(request)).thenReturn(response);

        var expectedJson = "{\"brandName\":\"Renault\"}";

        mockMvc.perform(post("/brands")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"id\":" + id + ",\"brandName\":\"Renault\"}"))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expectedJson));

        verify(brandService, only()).createNewBrand(request);
    }

}
