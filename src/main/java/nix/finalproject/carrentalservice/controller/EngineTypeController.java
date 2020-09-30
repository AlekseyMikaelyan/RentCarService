package nix.finalproject.carrentalservice.controller;

import nix.finalproject.carrentalservice.dto.EngineTypeDTO;
import nix.finalproject.carrentalservice.dto.request.EngineTypeRequestDTO;
import nix.finalproject.carrentalservice.entity.EngineType;
import nix.finalproject.carrentalservice.service.EngineTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/engine_types")
public class EngineTypeController {

    private final EngineTypeService engineTypeService;

    @Autowired
    public EngineTypeController(EngineTypeService engineTypeService) {
        this.engineTypeService = engineTypeService;
    }

    @GetMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<EngineTypeDTO> findAll() {
        return engineTypeService.findAllEngineTypeDTO();
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public EngineTypeDTO findById(@PathVariable("id") Long id) {
        return engineTypeService.findEngineTypeDTOById(id);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public EngineTypeRequestDTO createNewEngineType(@RequestBody EngineType engineType) {
        return engineTypeService.createNewEngineType(engineType);
    }

    @DeleteMapping(value = "/{id}")
    public void deleteById(@PathVariable("id") Long id) {
        engineTypeService.deleteById(id);
    }

}
