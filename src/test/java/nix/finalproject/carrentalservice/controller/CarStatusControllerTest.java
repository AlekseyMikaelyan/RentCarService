package nix.finalproject.carrentalservice.controller;

import nix.finalproject.carrentalservice.dto.CarStatusDTO;
import nix.finalproject.carrentalservice.dto.request.CarStatusRequestDTO;
import nix.finalproject.carrentalservice.entity.CarStatus;
import nix.finalproject.carrentalservice.exceptions.ExceptionMessages;
import nix.finalproject.carrentalservice.exceptions.ServiceException;
import nix.finalproject.carrentalservice.service.CarStatusService;
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

@WebMvcTest(CarStatusController.class)
public class CarStatusControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CarStatusService carStatusService;

    @Test
    public void findCarStatusByIdTest() throws Exception {
        final Long id = 1L;
        CarStatusDTO carStatusDTO = new CarStatusDTO(id, "Available");

        when(carStatusService.findCarStatusDTOById(id)).thenReturn(carStatusDTO);

        var expectedJson = "{\"id\":" + id + ",\"status\":\"Available\"}";

        mockMvc.perform(get("/car_status/" + id))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expectedJson));

        verify(carStatusService, only()).findCarStatusDTOById(id);
    }

    @Test
    public void findCarStatusByAbsentIdTest() throws Exception {
        final Long id = 1L;

        when(carStatusService.findCarStatusDTOById(id)).thenThrow(ServiceException.entityNotFound(ExceptionMessages.CAN_NOT_FIND_CAR_STATUS));

        mockMvc.perform(get("/car_status/" + id))
                .andExpect(status().isNotFound());

        verify(carStatusService, only()).findCarStatusDTOById(id);
    }

    @Test
    public void deleteCarStatusTest() throws Exception {
        Long id = 1L;
        CarStatusDTO carStatusDTO = new CarStatusDTO(id, "Available");

        when(carStatusService.findCarStatusDTOById(id)).thenReturn(carStatusDTO);
        doNothing().when(carStatusService).deleteById(id);

        mockMvc.perform(delete("/car_status/" + id)).andExpect(status().isOk());

        verify(carStatusService, only()).deleteById(id);
    }

    @Test
    public void createNewCarStatusTest() throws Exception {
        Long id = 1L;
        var request = new CarStatus(id, "Available");
        var response = new CarStatusRequestDTO("Available");

        when(carStatusService.createNewStatus(request)).thenReturn(response);

        var expectedJson = "{\"status\":\"Available\"}";

        mockMvc.perform(post("/car_status")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"id\":" + id + ",\"status\":\"Available\"}"))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expectedJson));

        verify(carStatusService, only()).createNewStatus(request);
    }
}
