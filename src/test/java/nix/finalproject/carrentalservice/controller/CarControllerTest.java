package nix.finalproject.carrentalservice.controller;

import nix.finalproject.carrentalservice.dto.CarResponseDTO;
import nix.finalproject.carrentalservice.dto.request.CarRequestDTO;
import nix.finalproject.carrentalservice.entity.Brand;
import nix.finalproject.carrentalservice.entity.CarStatus;
import nix.finalproject.carrentalservice.entity.CarType;
import nix.finalproject.carrentalservice.entity.EngineType;
import nix.finalproject.carrentalservice.service.CarServiceImpl;
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

public class CarControllerTest {

    private MockMvc mockMvc;

    private CarServiceImpl carService;

    @BeforeEach
    void setUp() {
        carService = mock(CarServiceImpl.class);
        mockMvc = MockMvcBuilders
                .standaloneSetup(new CarController(carService))
                .build();
    }

    @Test
    public void getCarByIdTest() throws Exception {

        Long id = 1L;
        var response = new CarResponseDTO(id, "Renault", "Logan", "Diesel", "1.5", "Sedan", "Manual", 2017, "Available", "30 euro");

        when(carService.getById(id)).thenReturn(Optional.of(response));

        var expectedJson = "{\"id\":" + id + ",\"brand\":\"Renault\",\"model\":\"Logan\",\"engineType\":\"Diesel\",\"engineCapacity\":\"1.5\",\"carType\":\"Sedan\",\"transmission\":\"Manual\",\"yearOfManufacture\":" + 2017 + ",\"carStatus\":\"Available\",\"price\":\"30 euro\"}";

        mockMvc.perform(get("/cars/" + id))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expectedJson));

        verify(carService, only()).getById(id);
    }

    @Test
    public void getCarByAbsentIdTest() throws Exception {
        final Long id = 1L;

        when(carService.getById(id)).thenReturn(Optional.empty());

        mockMvc.perform(get("/cars/" + id))
                .andExpect(status().isNotFound());

        verify(carService, only()).getById(id);
    }

    @Test
    public void deleteCarTest() throws Exception {

        Long id = 1L;
        var response = new CarResponseDTO(id, "Renault", "Logan", "Diesel", "1.5", "Sedan", "Manual", 2017, "Available", "30 euro");
        var expectedJson = "{\"id\":" + id + ",\"brand\":\"Renault\",\"model\":\"Logan\",\"engineType\":\"Diesel\",\"engineCapacity\":\"1.5\",\"carType\":\"Sedan\",\"transmission\":\"Manual\",\"yearOfManufacture\":" + 2017 + ",\"carStatus\":\"Available\",\"price\":\"30 euro\"}";

        when(carService.deleteById(id))
                .thenReturn(Optional.of(response))
                .thenReturn(Optional.empty());

        mockMvc.perform(delete("/cars/" + id))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedJson));

        mockMvc.perform(delete("/cars/" + id))
                .andExpect(status().isNotFound());

        verify(carService, times(2)).deleteById(id);
        verifyNoMoreInteractions(carService);
    }

    @Test
    public void createNewCarTest() throws Exception {
        Long id = 1L;

        Brand brand  = new Brand(id, "Renault");

        EngineType engineType = new EngineType(id, "Diesel", "1.5");

        CarType carType = new CarType(id, "Sedan");

        CarStatus carStatus = new CarStatus(id, "Available");

        var request = new CarRequestDTO(brand.getId(), engineType.getId(), carType.getId(), "Logan", "Manual", 2017, carStatus.getId(), "30 euro");

        var response = new CarResponseDTO(id, "Renault", "Logan", "Diesel", "1.5", "Sedan", "Manual", 2017, "Available", "30 euro");

        when(carService.create(request)).thenReturn(response);

        var expectedJson = "{\"id\":" + id + ",\"brand\":\"Renault\",\"model\":\"Logan\",\"engineType\":\"Diesel\",\"engineCapacity\":\"1.5\",\"carType\":\"Sedan\",\"transmission\":\"Manual\",\"yearOfManufacture\":" + 2017 + ",\"carStatus\":\"Available\",\"price\":\"30 euro\"}";

        mockMvc.perform(post("/cars")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"brandId\":" + id + ",\"engineTypeId\":" + id + ",\"carTypeId\":" + id + ",\"model\":\"Logan\",\"transmission\":\"Manual\",\"yearOfManufacture\":" + 2017 + ",\"carStatusId\":" + id + ",\"price\":\"30 euro\"}"))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expectedJson));

        verify(carService, only()).create(request);
    }

    @Test
    public void updateCarTest() throws Exception {
        Long id = 1L;

        Brand brand  = new Brand(id, "Renault");

        EngineType engineType = new EngineType(id, "Diesel", "1.5");

        CarType carType = new CarType(id, "Sedan");

        CarStatus carStatus = new CarStatus(id, "Available");

        var request = new CarRequestDTO(brand.getId(), engineType.getId(), carType.getId(), "Logan", "Manual", 2017, carStatus.getId(), "30 euro");

        mockMvc.perform(put("/cars/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"brandId\":" + id + ",\"engineTypeId\":" + id + ",\"carTypeId\":" + id + ",\"model\":\"Logan\",\"transmission\":\"Manual\",\"yearOfManufacture\":" + 2017 + ",\"carStatusId\":" + id + ",\"price\":\"30 euro\"}"))
                .andExpect(status().isNoContent());

        verify(carService, only()).update(id, request);
    }
}
