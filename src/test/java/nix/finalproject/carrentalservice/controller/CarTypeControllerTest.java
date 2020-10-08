package nix.finalproject.carrentalservice.controller;

import nix.finalproject.carrentalservice.dto.CarTypeResponseDTO;
import nix.finalproject.carrentalservice.dto.request.CarTypeRequestDTO;
import nix.finalproject.carrentalservice.service.CarTypeServiceImpl;
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

public class CarTypeControllerTest {

    private MockMvc mockMvc;

    private CarTypeServiceImpl carTypeService;

    @BeforeEach
    void setUp() {
        carTypeService = mock(CarTypeServiceImpl.class);
        mockMvc = MockMvcBuilders
                .standaloneSetup(new CarTypeController(carTypeService))
                .build();
    }

    @Test
    public void getCarTypeByIdTest() throws Exception {

        Long id = 1L;
        var response = new CarTypeResponseDTO(id, "Sedan");

        when(carTypeService.getById(id)).thenReturn(Optional.of(response));

        var expectedJson = "{\"id\":" + id + ",\"bodyType\":\"Sedan\"}";

        mockMvc.perform(get("/car_types/" + id))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expectedJson));

        verify(carTypeService, only()).getById(id);
    }

    @Test
    public void getCarTypesByAbsentIdTest() throws Exception {
        final Long id = 1L;

        when(carTypeService.getById(id)).thenReturn(Optional.empty());

        mockMvc.perform(get("/car_types/" + id))
                .andExpect(status().isNotFound());

        verify(carTypeService, only()).getById(id);
    }

    @Test
    public void deleteCarTypeTest() throws Exception {

        Long id = 1L;
        var response = new CarTypeResponseDTO(id, "Sedan");
        var expectedJson = "{\"id\":" + id + ",\"bodyType\":\"Sedan\"}";

        when(carTypeService.deleteById(id))
                .thenReturn(Optional.of(response))
                .thenReturn(Optional.empty());

        mockMvc.perform(delete("/car_types/" + id))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedJson));

        mockMvc.perform(delete("/car_types/" + id))
                .andExpect(status().isNotFound());

        verify(carTypeService, times(2)).deleteById(id);
        verifyNoMoreInteractions(carTypeService);
    }

    @Test
    public void createNewCarTypeTest() throws Exception {

        var request = new CarTypeRequestDTO("Sedan");
        Long id = 1L;
        var response = new CarTypeResponseDTO(id, "Sedan");

        when(carTypeService.create(request)).thenReturn(response);

        var expectedJson = "{\"id\":" + id + ",\"bodyType\":\"Sedan\"}";

        mockMvc.perform(post("/car_types")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"bodyType\":\"Sedan\"}"))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expectedJson));

        verify(carTypeService, only()).create(request);
    }

    @Test
    public void updateCarTypeTest() throws Exception {
        var request = new CarTypeRequestDTO("Sedan");
        Long id = 1L;

        mockMvc.perform(put("/car_types/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"bodyType\":\"Sedan\"}"))
                .andExpect(status().isNoContent());

        verify(carTypeService, only()).update(id, request);
    }
}
