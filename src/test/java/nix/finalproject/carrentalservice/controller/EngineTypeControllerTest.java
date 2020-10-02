package nix.finalproject.carrentalservice.controller;

import nix.finalproject.carrentalservice.dto.EngineTypeDTO;
import nix.finalproject.carrentalservice.dto.request.EngineTypeRequestDTO;
import nix.finalproject.carrentalservice.entity.EngineType;
import nix.finalproject.carrentalservice.exceptions.ExceptionMessages;
import nix.finalproject.carrentalservice.exceptions.ServiceException;
import nix.finalproject.carrentalservice.service.EngineTypeService;
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

@WebMvcTest(EngineTypeController.class)
public class EngineTypeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EngineTypeService engineTypeService;

    @Test
    public void findEngineTypeByIdTest() throws Exception {
        final Long id = 1L;
        EngineTypeDTO engineTypeDTO = new EngineTypeDTO(id, "Diesel", "1.5");

        when(engineTypeService.findEngineTypeDTOById(id)).thenReturn(engineTypeDTO);

        var expectedJson = "{\"id\":" + id + ",\"type\":\"Diesel\",\"engineCapacity\":\"1.5\"}";

        mockMvc.perform(get("/engine_types/" + id))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expectedJson));

        verify(engineTypeService, only()).findEngineTypeDTOById(id);
    }

    @Test
    public void findEngineTypeByAbsentIdTest() throws Exception {
        final Long id = 1L;

        when(engineTypeService.findEngineTypeDTOById(id)).thenThrow(ServiceException.entityNotFound(ExceptionMessages.CAN_NOT_FIND_ENGINE));

        mockMvc.perform(get("/engine_types/" + id))
                .andExpect(status().isNotFound());

        verify(engineTypeService, only()).findEngineTypeDTOById(id);
    }

    @Test
    public void deleteEngineTypeTest() throws Exception {
        Long id = 1L;
        EngineTypeDTO engineTypeDTO = new EngineTypeDTO(id, "Diesel", "1.5");

        when(engineTypeService.findEngineTypeDTOById(id)).thenReturn(engineTypeDTO);
        doNothing().when(engineTypeService).deleteById(id);

        mockMvc.perform(delete("/engine_types/" + id)).andExpect(status().isOk());

        verify(engineTypeService, only()).deleteById(id);
    }

    @Test
    public void createNewCarTypeTest() throws Exception {
        Long id = 1L;
        var request = new EngineType(id, "Diesel", "1.5");
        var response = new EngineTypeRequestDTO("Diesel", "1.5");

        when(engineTypeService.createNewEngineType(request)).thenReturn(response);

        var expectedJson = "{\"type\":\"Diesel\",\"capacity\":\"1.5\"}";

        mockMvc.perform(post("/engine_types")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"id\":" + id + ",\"type\":\"Diesel\",\"capacity\":\"1.5\"}"))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expectedJson));

        verify(engineTypeService, only()).createNewEngineType(request);
    }
}
