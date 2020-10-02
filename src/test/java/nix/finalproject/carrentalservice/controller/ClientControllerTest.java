package nix.finalproject.carrentalservice.controller;

import nix.finalproject.carrentalservice.dto.ClientDTO;
import nix.finalproject.carrentalservice.dto.request.ClientRequestDTO;
import nix.finalproject.carrentalservice.entity.Client;
import nix.finalproject.carrentalservice.exceptions.ExceptionMessages;
import nix.finalproject.carrentalservice.exceptions.ServiceException;
import nix.finalproject.carrentalservice.service.ClientService;
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

@WebMvcTest(ClientController.class)
public class ClientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ClientService clientService;

    @Test
    public void findClientByIdTest() throws Exception {
        final Long id = 1L;
        ClientDTO clientDTO = new ClientDTO(id, "Aleksey", "Alekseev", "+380990964311", "aleks@gmail.com");

        when(clientService.findClientDTOById(id)).thenReturn(clientDTO);

        var expectedJson = "{\"id\":" + id + ",\"firstName\":\"Aleksey\",\"lastName\":\"Alekseev\",\"phoneNumber\":\"+380990964311\",\"email\":\"aleks@gmail.com\"}";

        mockMvc.perform(get("/clients/" + id))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expectedJson));

        verify(clientService, only()).findClientDTOById(id);
    }

    @Test
    public void findClientByAbsentIdTest() throws Exception {
        final Long id = 1L;

        when(clientService.findClientDTOById(id)).thenThrow(ServiceException.entityNotFound(ExceptionMessages.CAN_NOT_FIND_CLIENT));

        mockMvc.perform(get("/clients/" + id))
                .andExpect(status().isNotFound());

        verify(clientService, only()).findClientDTOById(id);
    }

    @Test
    public void deleteClientTest() throws Exception {
        Long id = 1L;
        ClientDTO clientDTO = new ClientDTO(id, "Aleksey", "Alekseev", "+380990964311", "aleks@gmail.com");

        when(clientService.findClientDTOById(id)).thenReturn(clientDTO);
        doNothing().when(clientService).deleteById(id);

        mockMvc.perform(delete("/clients/" + id)).andExpect(status().isOk());

        verify(clientService, only()).deleteById(id);
    }

    @Test
    public void createNewClientTest() throws Exception {
        Long id = 1L;
        var request = new Client(id, "Aleksey", "Alekseev", "+380990964311", "aleks@gmail.com", "password1");
        var response = new ClientRequestDTO("Aleksey", "Alekseev", "+380990964311", "aleks@gmail.com", "password1");

        when(clientService.createNewClient(request)).thenReturn(response);

        var expectedJson = "{\"firstName\":\"Aleksey\",\"lastName\":\"Alekseev\",\"phoneNumber\":\"+380990964311\",\"email\":\"aleks@gmail.com\",\"password\":\"password1\"}";

        mockMvc.perform(post("/clients")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"id\":" + id + ",\"firstName\":\"Aleksey\",\"lastName\":\"Alekseev\",\"phoneNumber\":\"+380990964311\",\"email\":\"aleks@gmail.com\",\"password\":\"password1\"}"))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expectedJson));

        verify(clientService, only()).createNewClient(request);
    }
}
