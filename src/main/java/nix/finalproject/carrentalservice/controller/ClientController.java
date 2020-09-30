package nix.finalproject.carrentalservice.controller;

import nix.finalproject.carrentalservice.dto.ClientDTO;
import nix.finalproject.carrentalservice.dto.request.ClientRequestDTO;
import nix.finalproject.carrentalservice.entity.Client;
import nix.finalproject.carrentalservice.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clients")
public class ClientController {

    private final ClientService clientService;

    @Autowired
    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<ClientDTO> findAll() {
        return clientService.findAllClientDTO();
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ClientDTO findById(@PathVariable("id") Long id) {
        return clientService.findClientDTOById(id);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ClientRequestDTO createNewClient(@RequestBody Client client) {
        return clientService.createNewClient(client);
    }

    @DeleteMapping(value = "/{id}")
    public void deleteById(@PathVariable("id") Long id) {
        clientService.deleteById(id);
    }

}
