package nix.finalproject.carrentalservice.controller;

import nix.finalproject.carrentalservice.dto.ClientResponseDTO;
import nix.finalproject.carrentalservice.dto.request.ClientRequestDTO;
import nix.finalproject.carrentalservice.exceptions.ExceptionMessages;
import nix.finalproject.carrentalservice.exceptions.ServiceException;
import nix.finalproject.carrentalservice.service.ClientServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/clients")
public class ClientController {

    private final ClientServiceImpl clientServiceImpl;

    @Autowired
    public ClientController(ClientServiceImpl clientServiceImpl) {
        this.clientServiceImpl = clientServiceImpl;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ClientResponseDTO createNewClient(@Valid @RequestBody ClientRequestDTO client) {
        return clientServiceImpl.create(client);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ClientResponseDTO findById(@PathVariable Long id) {
        return clientServiceImpl.getById(id)
                .orElseThrow(() -> ServiceException.entityNotFound(ExceptionMessages.CAN_NOT_FIND_CLIENT));
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/{id}")
    public void updateClient(@PathVariable Long id, @Valid @RequestBody ClientRequestDTO request) {
        clientServiceImpl.update(id, request);
    }

    @DeleteMapping("/{id}")
    public ClientResponseDTO deleteById(@PathVariable Long id) {
        return clientServiceImpl.deleteById(id)
                .orElseThrow(() -> ServiceException.entityNotFound(ExceptionMessages.CAN_NOT_FIND_CLIENT));
    }

    @GetMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<ClientResponseDTO> findAll() {
        return clientServiceImpl.findAll();
    }

}
