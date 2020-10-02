package nix.finalproject.carrentalservice.controller;

import nix.finalproject.carrentalservice.dto.CarStatusDTO;
import nix.finalproject.carrentalservice.dto.CarTypeDTO;
import nix.finalproject.carrentalservice.dto.request.CarStatusRequestDTO;
import nix.finalproject.carrentalservice.dto.request.CarTypeRequestDTO;
import nix.finalproject.carrentalservice.entity.CarStatus;
import nix.finalproject.carrentalservice.entity.CarType;
import nix.finalproject.carrentalservice.exceptions.ExceptionMessages;
import nix.finalproject.carrentalservice.exceptions.ServiceException;
import nix.finalproject.carrentalservice.service.CarStatusService;
import nix.finalproject.carrentalservice.service.CarTypeService;
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

@WebMvcTest(CarTypeController.class)
public class CarTypeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CarTypeService carTypeService;

    @Test
    public void findCarTypeByIdTest() throws Exception {
        final Long id = 1L;
        CarTypeDTO carTypeDTO = new CarTypeDTO(id, "Sedan");

        when(carTypeService.findCarTypeDTOById(id)).thenReturn(carTypeDTO);

        var expectedJson = "{\"id\":" + id + ",\"bodyType\":\"Sedan\"}";

        mockMvc.perform(get("/car_types/" + id))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expectedJson));

        verify(carTypeService, only()).findCarTypeDTOById(id);
    }

    @Test
    public void findCarTypeByAbsentIdTest() throws Exception {
        final Long id = 1L;

        when(carTypeService.findCarTypeDTOById(id)).thenThrow(ServiceException.entityNotFound(ExceptionMessages.CAN_NOT_FIND_CAR_TYPE));

        mockMvc.perform(get("/car_types/" + id))
                .andExpect(status().isNotFound());

        verify(carTypeService, only()).findCarTypeDTOById(id);
    }

    @Test
    public void deleteCarTypeTest() throws Exception {
        Long id = 1L;
        CarTypeDTO carTypeDTO = new CarTypeDTO(id, "Sedan");

        when(carTypeService.findCarTypeDTOById(id)).thenReturn(carTypeDTO);
        doNothing().when(carTypeService).deleteById(id);

        mockMvc.perform(delete("/car_types/" + id)).andExpect(status().isOk());

        verify(carTypeService, only()).deleteById(id);
    }

    @Test
    public void createNewCarTypeTest() throws Exception {
        Long id = 1L;
        var request = new CarType(id, "Sedan");
        var response = new CarTypeRequestDTO("Sedan");

        when(carTypeService.createNewType(request)).thenReturn(response);

        var expectedJson = "{\"bodyType\":\"Sedan\"}";

        mockMvc.perform(post("/car_types")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"id\":" + id + ",\"bodyType\":\"Sedan\"}"))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expectedJson));

        verify(carTypeService, only()).createNewType(request);
    }
}
