package nix.finalproject.carrentalservice.service;

import nix.finalproject.carrentalservice.dto.ClientDTO;
import nix.finalproject.carrentalservice.dto.request.ClientRequestDTO;
import nix.finalproject.carrentalservice.entity.Client;
import nix.finalproject.carrentalservice.exceptions.ExceptionMessages;
import nix.finalproject.carrentalservice.exceptions.ServiceException;
import nix.finalproject.carrentalservice.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ClientService {

    public final ClientRepository clientRepository;

    @Autowired
    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public ClientDTO convertClientToClientDTO(Client client) {
        return new ClientDTO(
                client.getId(),
                client.getFirstName(),
                client.getLastName(),
                client.getPhoneNumber(),
                client.getEmail()
        );
    }

    public List<ClientDTO> convertClientListToClientDTOList(List<Client> clientList) {
        return clientList.stream().map(this::convertClientToClientDTO).collect(Collectors.toList());
    }

    public List<ClientDTO> findAllClientDTO() {
        return convertClientListToClientDTOList(clientRepository.findAll());
    }

    public ClientDTO findClientDTOById(Long id) {
        Client client = clientRepository.findById(id).orElseThrow(() -> ServiceException.entityNotFound(ExceptionMessages.CAN_NOT_FIND_CLIENT));
        return convertClientToClientDTO(client);
    }

    public ClientRequestDTO convertClientToClientRequestDTO(Client client) {
        return new ClientRequestDTO(client.getFirstName(),
                client.getLastName(),
                client.getPhoneNumber(),
                client.getEmail(),
                client.getPassword());
    }

    public ClientRequestDTO createNewClient(Client client) {
        return convertClientToClientRequestDTO(clientRepository.save(client));
    }

    public void deleteById(Long id) {
        clientRepository.deleteById(id);
    }
}
