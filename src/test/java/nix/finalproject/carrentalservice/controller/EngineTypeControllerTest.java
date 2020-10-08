package nix.finalproject.carrentalservice.controller;

import nix.finalproject.carrentalservice.dto.EngineTypeResponseDTO;
import nix.finalproject.carrentalservice.dto.request.EngineTypeRequestDTO;
import nix.finalproject.carrentalservice.service.EngineTypeServiceImpl;
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

public class EngineTypeControllerTest {

    private MockMvc mockMvc;

    private EngineTypeServiceImpl engineTypeService;

    @BeforeEach
    void setUp() {
        engineTypeService = mock(EngineTypeServiceImpl.class);
        mockMvc = MockMvcBuilders
                .standaloneSetup(new EngineTypeController(engineTypeService))
                .build();
    }

    @Test
    public void getEngineByIdTest() throws Exception {

        Long id = 1L;
        var response = new EngineTypeResponseDTO(id,"Diesel", "1.5");

        when(engineTypeService.getById(id)).thenReturn(Optional.of(response));

        var expectedJson = "{\"id\":" + id + ",\"type\":\"Diesel\",\"engineCapacity\":\"1.5\"}";

        mockMvc.perform(get("/engine_types/" + id))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expectedJson));

        verify(engineTypeService, only()).getById(id);
    }

    @Test
    public void getEngineByAbsentIdTest() throws Exception {
        final Long id = 1L;

        when(engineTypeService.getById(id)).thenReturn(Optional.empty());

        mockMvc.perform(get("/engine_types/" + id))
                .andExpect(status().isNotFound());

        verify(engineTypeService, only()).getById(id);
    }

    @Test
    public void deleteEngineTest() throws Exception {

        Long id = 1L;
        var response = new EngineTypeResponseDTO(id,"Diesel", "1.5");
        var expectedJson = "{\"id\":" + id + ",\"type\":\"Diesel\",\"engineCapacity\":\"1.5\"}";

        when(engineTypeService.deleteById(id))
                .thenReturn(Optional.of(response))
                .thenReturn(Optional.empty());

        mockMvc.perform(delete("/engine_types/" + id))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedJson));

        mockMvc.perform(delete("/engine_types/" + id))
                .andExpect(status().isNotFound());

        verify(engineTypeService, times(2)).deleteById(id);
        verifyNoMoreInteractions(engineTypeService);
    }

    @Test
    public void createNewEngineTest() throws Exception {

        var request = new EngineTypeRequestDTO("Diesel","1.5");
        Long id = 1L;
        var response = new EngineTypeResponseDTO(id, "Diesel", "1.5");

        when(engineTypeService.create(request)).thenReturn(response);

        var expectedJson = "{\"id\":" + id + ",\"type\":\"Diesel\",\"engineCapacity\":\"1.5\"}";

        mockMvc.perform(post("/engine_types")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"type\":\"Diesel\",\"capacity\":\"1.5\"}"))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expectedJson));

        verify(engineTypeService, only()).create(request);
    }

    @Test
    public void updateEngineTest() throws Exception {
        var request = new EngineTypeRequestDTO("Diesel", "1.5");
        Long id = 1L;

        mockMvc.perform(put("/engine_types/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"type\":\"Diesel\",\"capacity\":\"1.5\"}"))
                .andExpect(status().isNoContent());

        verify(engineTypeService, only()).update(id, request);
    }
}
