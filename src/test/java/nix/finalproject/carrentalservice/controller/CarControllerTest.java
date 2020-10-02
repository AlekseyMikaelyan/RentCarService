package nix.finalproject.carrentalservice.controller;

import nix.finalproject.carrentalservice.dto.CarDTO;
import nix.finalproject.carrentalservice.exceptions.ExceptionMessages;
import nix.finalproject.carrentalservice.exceptions.ServiceException;
import nix.finalproject.carrentalservice.service.CarService;
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

@WebMvcTest(CarController.class)
public class CarControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CarService carService;

    @Test
    public void findCarByIdTest() throws Exception {
        final Long id = 1L;
        CarDTO carDTO = new CarDTO("Renault", "Logan", "Diesel", "1.5", "Sedan", "Manual", 2017, "Available", "30 euro");

        when(carService.findCarDTOById(id)).thenReturn(carDTO);

        var expectedJson = "{\"brand\":\"Renault\",\"model\":\"Logan\",\"engineType\":\"Diesel\",\"engineCapacity\":\"1.5\",\"carType\":\"Sedan\",\"transmission\":\"Manual\",\"yearOfManufacture\":" + 2017 + ",\"carStatus\":\"Available\",\"price\":\"30 euro\"}";

        mockMvc.perform(get("/cars/" + id))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expectedJson));

        verify(carService, only()).findCarDTOById(id);
    }

    @Test
    public void findCarByAbsentIdTest() throws Exception {
        final Long id = 1L;

        when(carService.findCarDTOById(id)).thenThrow(ServiceException.entityNotFound(ExceptionMessages.CAN_NOT_FIND_CAR));

        mockMvc.perform(get("/cars/" + id))
                .andExpect(status().isNotFound());

        verify(carService, only()).findCarDTOById(id);
    }

    @Test
    public void deleteCarTest() throws Exception {
        Long id = 1L;
        CarDTO carDTO = new CarDTO("Renault", "Logan", "Diesel", "1.5", "Sedan", "Manual", 2017, "Available", "30 euro");

        when(carService.findCarDTOById(id)).thenReturn(carDTO);
        doNothing().when(carService).deleteById(id);

        mockMvc.perform(delete("/cars/" + id)).andExpect(status().isOk());

        verify(carService, only()).deleteById(id);
    }

}
