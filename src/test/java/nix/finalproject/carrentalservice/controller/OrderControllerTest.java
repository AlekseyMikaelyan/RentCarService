package nix.finalproject.carrentalservice.controller;

import nix.finalproject.carrentalservice.dto.OrderResponseDTO;
import nix.finalproject.carrentalservice.dto.request.OrderRequestDTO;
import nix.finalproject.carrentalservice.entity.*;
import nix.finalproject.carrentalservice.service.OrderServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class OrderControllerTest {

    private MockMvc mockMvc;

    private OrderServiceImpl orderService;

    @BeforeEach
    void setUp() {
        orderService = mock(OrderServiceImpl.class);
        mockMvc = MockMvcBuilders
                .standaloneSetup(new OrderController(orderService))
                .build();
    }

    @Test
    public void getOrderByIdTest() throws Exception {

        Long id = 1L;
        var response = new OrderResponseDTO(id, "Aleksey", "Alekseev", "+380990964311", "Renault", "Logan", LocalDate.of(2020, 10, 2), LocalDate.of(2020, 10, 5), "90");

        when(orderService.getById(id)).thenReturn(Optional.of(response));

        mockMvc.perform(get("/orders/" + id))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(orderService, only()).getById(id);
    }

    @Test
    public void getOrderByAbsentIdTest() throws Exception {
        final Long id = 1L;

        when(orderService.getById(id)).thenReturn(Optional.empty());

        mockMvc.perform(get("/orders/" + id))
                .andExpect(status().isNotFound());

        verify(orderService, only()).getById(id);
    }

    @Test
    public void deleteOrderTest() throws Exception {

        Long id = 1L;
        var response = new OrderResponseDTO(id, "Aleksey", "Alekseev", "+380990964311", "Renault", "Logan", LocalDate.of(2020, 10, 2), LocalDate.of(2020, 10, 5), "90");

        when(orderService.deleteById(id))
                .thenReturn(Optional.of(response))
                .thenReturn(Optional.empty());

        mockMvc.perform(delete("/orders/" + id))
                .andExpect(status().isOk());

        mockMvc.perform(delete("/orders/" + id))
                .andExpect(status().isNotFound());

        verify(orderService, times(2)).deleteById(id);
        verifyNoMoreInteractions(orderService);
    }

    @Test
    public void createOrderTest() throws Exception {
        Long id = 1L;

        Brand brand  = new Brand(id, "Renault");
        EngineType engineType = new EngineType(id, "Diesel", "1.5");
        CarType carType = new CarType(id, "Sedan");
        CarStatus carStatus = new CarStatus(id, "Available");
        Car car = new Car(id, brand, engineType, carType, "Logan", "Manual", 2017, carStatus, "25 euro");
        Client client = new Client(id, "Aleksey", "Alekseev", "+380936435199", "aleks@gmail.com", "password1");

        var request = new OrderRequestDTO(client.getId(), car.getId(), LocalDate.of(2020, 10, 2), LocalDate.of(2020, 10, 5));

        var response = new OrderResponseDTO(id, "Aleksey", "Alekseev", "+380936435199", "Renault", "Logan", LocalDate.of(2020, 10, 2), LocalDate.of(2020, 10, 5), "75");

        when(orderService.create(request)).thenReturn(response);

        mockMvc.perform(post("/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"clientId\":" + id + ",\"carId\":" + id + ",\"startOrder\":\"2020-10-02\",\"endOrder\":\"2020-10-05\"}"))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(orderService, only()).create(request);
    }

    @Test
    public void updateOrderTest() throws Exception {
        Long id = 1L;

        Brand brand  = new Brand(id, "Renault");
        EngineType engineType = new EngineType(id, "Diesel", "1.5");
        CarType carType = new CarType(id, "Sedan");
        CarStatus carStatus = new CarStatus(id, "Available");
        Car car = new Car(id, brand, engineType, carType, "Logan", "Manual", 2017, carStatus, "25 euro");
        Client client = new Client(id, "Aleksey", "Alekseev", "+380936435199", "aleks@gmail.com", "password1");

        var request = new OrderRequestDTO(client.getId(), car.getId(), LocalDate.of(2020, 10, 2), LocalDate.of(2020, 10, 5));

        mockMvc.perform(put("/orders/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"clientId\":" + id + ",\"carId\":" + id + ",\"startOrder\":\"2020-10-02\",\"endOrder\":\"2020-10-05\"}"))
                .andExpect(status().isNoContent());

        verify(orderService, only()).update(id, request);
    }
}
