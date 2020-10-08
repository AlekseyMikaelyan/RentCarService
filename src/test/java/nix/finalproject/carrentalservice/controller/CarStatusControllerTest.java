package nix.finalproject.carrentalservice.controller;

import nix.finalproject.carrentalservice.dto.CarStatusResponseDTO;
import nix.finalproject.carrentalservice.dto.request.CarStatusRequestDTO;
import nix.finalproject.carrentalservice.service.CarStatusServiceImpl;
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

public class CarStatusControllerTest {

    private MockMvc mockMvc;

    private CarStatusServiceImpl carStatusService;

    @BeforeEach
    void setUp() {
        carStatusService = mock(CarStatusServiceImpl.class);
        mockMvc = MockMvcBuilders
                .standaloneSetup(new CarStatusController(carStatusService))
                .build();
    }

    @Test
    public void getCarStatusByIdTest() throws Exception {

        Long id = 1L;
        var response = new CarStatusResponseDTO(id, "Available");

        when(carStatusService.getById(id)).thenReturn(Optional.of(response));

        var expectedJson = "{\"id\":" + id + ",\"status\":\"Available\"}";

        mockMvc.perform(get("/car_status/" + id))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expectedJson));

        verify(carStatusService, only()).getById(id);
    }

    @Test
    public void getCarStatusByAbsentIdTest() throws Exception {
        final Long id = 1L;

        when(carStatusService.getById(id)).thenReturn(Optional.empty());

        mockMvc.perform(get("/car_status/" + id))
                .andExpect(status().isNotFound());

        verify(carStatusService, only()).getById(id);
    }

    @Test
    public void deleteCarStatusTest() throws Exception {

        Long id = 1L;
        var response = new CarStatusResponseDTO(id, "Available");
        var expectedJson = "{\"id\":" + id + ",\"status\":\"Available\"}";

        when(carStatusService.deleteById(id))
                .thenReturn(Optional.of(response))
                .thenReturn(Optional.empty());

        mockMvc.perform(delete("/car_status/" + id))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedJson));

        mockMvc.perform(delete("/car_status/" + id))
                .andExpect(status().isNotFound());

        verify(carStatusService, times(2)).deleteById(id);
        verifyNoMoreInteractions(carStatusService);
    }

    @Test
    public void createNewCarStatusTest() throws Exception {

        var request = new CarStatusRequestDTO("Available");
        Long id = 1L;
        var response = new CarStatusResponseDTO(id, "Available");

        when(carStatusService.create(request)).thenReturn(response);

        var expectedJson = "{\"id\":" + id + ",\"status\":\"Available\"}";

        mockMvc.perform(post("/car_status")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"status\":\"Available\"}"))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expectedJson));

        verify(carStatusService, only()).create(request);
    }

    @Test
    public void updateCarStatusTest() throws Exception {
        var request = new CarStatusRequestDTO("Available");
        Long id = 1L;

        mockMvc.perform(put("/car_status/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"status\":\"Available\"}"))
                .andExpect(status().isNoContent());

        verify(carStatusService, only()).update(id, request);
    }
}
