package nix.finalproject.carrentalservice.controller;

import nix.finalproject.carrentalservice.dto.ClientResponseDTO;
import nix.finalproject.carrentalservice.dto.request.ClientRequestDTO;
import nix.finalproject.carrentalservice.service.ClientServiceImpl;
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

public class ClientControllerTest {

    private MockMvc mockMvc;

    private ClientServiceImpl clientService;

    @BeforeEach
    void setUp() {
        clientService = mock(ClientServiceImpl.class);
        mockMvc = MockMvcBuilders
                .standaloneSetup(new ClientController(clientService))
                .build();
    }

    @Test
    public void getClientByIdTest() throws Exception {

        Long id = 1L;
        var response = new ClientResponseDTO(id, "Aleksey", "Alekseev", "+380990964311", "aleks@gmail.com");

        when(clientService.getById(id)).thenReturn(Optional.of(response));

        var expectedJson = "{\"id\":" + id + ",\"firstName\":\"Aleksey\",\"lastName\":\"Alekseev\",\"phoneNumber\":\"+380990964311\",\"email\":\"aleks@gmail.com\"}";

        mockMvc.perform(get("/clients/" + id))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expectedJson));

        verify(clientService, only()).getById(id);
    }

    @Test
    public void getClientByAbsentIdTest() throws Exception {
        final Long id = 1L;

        when(clientService.getById(id)).thenReturn(Optional.empty());

        mockMvc.perform(get("/clients/" + id))
                .andExpect(status().isNotFound());

        verify(clientService, only()).getById(id);
    }

    @Test
    public void deleteClientTest() throws Exception {

        Long id = 1L;
        var response = new ClientResponseDTO(id, "Aleksey", "Alekseev", "+380990964311", "aleks@gmail.com");
        var expectedJson = "{\"id\":" + id + ",\"firstName\":\"Aleksey\",\"lastName\":\"Alekseev\",\"phoneNumber\":\"+380990964311\",\"email\":\"aleks@gmail.com\"}";

        when(clientService.deleteById(id))
                .thenReturn(Optional.of(response))
                .thenReturn(Optional.empty());

        mockMvc.perform(delete("/clients/" + id))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedJson));

        mockMvc.perform(delete("/clients/" + id))
                .andExpect(status().isNotFound());

        verify(clientService, times(2)).deleteById(id);
        verifyNoMoreInteractions(clientService);
    }

    @Test
    public void createNewClientTest() throws Exception {

        var request = new ClientRequestDTO("Aleksey", "Alekseev", "+380990964311", "aleks@gmail.com", "password1");
        Long id = 1L;
        var response = new ClientResponseDTO(id, "Aleksey", "Alekseev", "+380990964311", "aleks@gmail.com");

        when(clientService.create(request)).thenReturn(response);

        var expectedJson = "{\"id\":" + id + ",\"firstName\":\"Aleksey\",\"lastName\":\"Alekseev\",\"phoneNumber\":\"+380990964311\",\"email\":\"aleks@gmail.com\"}";

        mockMvc.perform(post("/clients")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"firstName\":\"Aleksey\",\"lastName\":\"Alekseev\",\"phoneNumber\":\"+380990964311\",\"email\":\"aleks@gmail.com\",\"password\":\"password1\"}"))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expectedJson));

        verify(clientService, only()).create(request);
    }

    @Test
    public void updateClientTest() throws Exception {
        var request = new ClientRequestDTO("Aleksey", "Alekseev", "+380990964311", "aleks@gmail.com", "password1");
        Long id = 1L;

        mockMvc.perform(put("/clients/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"firstName\":\"Aleksey\",\"lastName\":\"Alekseev\",\"phoneNumber\":\"+380990964311\",\"email\":\"aleks@gmail.com\",\"password\":\"password1\"}"))
                .andExpect(status().isNoContent());

        verify(clientService, only()).update(id, request);
    }
}
