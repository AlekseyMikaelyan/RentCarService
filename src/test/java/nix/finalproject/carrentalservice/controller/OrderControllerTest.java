package nix.finalproject.carrentalservice.controller;

import nix.finalproject.carrentalservice.dto.OrderDTO;
import nix.finalproject.carrentalservice.exceptions.ExceptionMessages;
import nix.finalproject.carrentalservice.exceptions.ServiceException;
import nix.finalproject.carrentalservice.service.OrderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrderController.class)
public class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    @Test
    public void findOrderByIdTest() throws Exception {
        final Long id = 1L;
        OrderDTO orderDTO = new OrderDTO(id, "Aleksey", "Alekseev", "+380990964311", "Renault", "Logan", LocalDate.of(2020, 10, 2), LocalDate.of(2020, 10, 5), "90");

        when(orderService.findOrderDTOById(id)).thenReturn(orderDTO);

        var expectedJson = "{\"id\":" + id + ",\"clientFirstName\":\"Aleksey\",\"clientLastName\":\"Alekseev\",\"clientPhoneNumber\":\"+380990964311\",\"carBrand\":\"Renault\",\"startOrder\":\"2020-10-02\",\"endOrder\":\"2020-10-05\",\"totalPrice\":\"90\"}";

        mockMvc.perform(get("/orders/" + id))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expectedJson));

        verify(orderService, only()).findOrderDTOById(id);
    }

    @Test
    public void findOrderByAbsentIdTest() throws Exception {
        final Long id = 1L;

        when(orderService.findOrderDTOById(id)).thenThrow(ServiceException.entityNotFound(ExceptionMessages.CAN_NOT_FIND_ORDER));

        mockMvc.perform(get("/orders/" + id))
                .andExpect(status().isNotFound());

        verify(orderService, only()).findOrderDTOById(id);
    }

    @Test
    public void deleteOrderTest() throws Exception {
        Long id = 1L;
        OrderDTO orderDTO = new OrderDTO(id, "Aleksey", "Alekseev", "+380990964311", "Renault", "Logan", LocalDate.of(2020, 10, 2), LocalDate.of(2020, 10, 5), "90");

        when(orderService.findOrderDTOById(id)).thenReturn(orderDTO);
        doNothing().when(orderService).deleteById(id);

        mockMvc.perform(delete("/orders/" + id)).andExpect(status().isOk());

        verify(orderService, only()).deleteById(id);
    }

}
