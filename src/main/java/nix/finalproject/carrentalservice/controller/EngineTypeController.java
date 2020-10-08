package nix.finalproject.carrentalservice.controller;

import nix.finalproject.carrentalservice.dto.EngineTypeResponseDTO;
import nix.finalproject.carrentalservice.dto.request.EngineTypeRequestDTO;
import nix.finalproject.carrentalservice.exceptions.ExceptionMessages;
import nix.finalproject.carrentalservice.exceptions.ServiceException;
import nix.finalproject.carrentalservice.service.EngineTypeServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/engine_types")
public class EngineTypeController {

    private final EngineTypeServiceImpl engineTypeServiceImpl;

    @Autowired
    public EngineTypeController(EngineTypeServiceImpl engineTypeServiceImpl) {
        this.engineTypeServiceImpl = engineTypeServiceImpl;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public EngineTypeResponseDTO createNewEngineType(@Valid @RequestBody EngineTypeRequestDTO engineType) {
        return engineTypeServiceImpl.create(engineType);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public EngineTypeResponseDTO findById(@PathVariable Long id) {
        return engineTypeServiceImpl.getById(id)
                .orElseThrow(() -> ServiceException.entityNotFound(ExceptionMessages.CAN_NOT_FIND_ENGINE));
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/{id}")
    public void updateEngine(@PathVariable Long id, @Valid @RequestBody EngineTypeRequestDTO request) {
        engineTypeServiceImpl.update(id, request);
    }

    @DeleteMapping("/{id}")
    public EngineTypeResponseDTO deleteById(@PathVariable Long id) {
        return engineTypeServiceImpl.deleteById(id)
                .orElseThrow(() -> ServiceException.entityNotFound(ExceptionMessages.CAN_NOT_FIND_ENGINE));
    }

    @GetMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<EngineTypeResponseDTO> findAll() {
        return engineTypeServiceImpl.findAll();
    }

}
