package nix.finalproject.carrentalservice.service;

import nix.finalproject.carrentalservice.dto.ClientResponseDTO;
import nix.finalproject.carrentalservice.dto.request.ClientRequestDTO;
import nix.finalproject.carrentalservice.entity.Client;
import nix.finalproject.carrentalservice.exceptions.ExceptionMessages;
import nix.finalproject.carrentalservice.exceptions.ServiceException;
import nix.finalproject.carrentalservice.repository.ClientRepository;
import nix.finalproject.carrentalservice.service.interfaces.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class ClientServiceImpl implements ClientService {

    public final ClientRepository clientRepository;

    @Autowired
    public ClientServiceImpl(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public List<ClientResponseDTO> convertClientListToClientResponseDTOList(List<Client> clientList) {
        return clientList.stream().map(ClientResponseDTO::fromClient).collect(Collectors.toList());
    }

    @Override
    public ClientResponseDTO create(ClientRequestDTO request) {
        var client = new Client(request.getFirstName(), request.getLastName(), request.getPhoneNumber(), request.getEmail(), request.getPassword());
        return ClientResponseDTO.fromClient(clientRepository.save(client));
    }

    @Override
    public Optional<ClientResponseDTO> getById(Long id) {
        return clientRepository.findById(id).map(ClientResponseDTO::fromClient);
    }

    @Override
    public void update(Long id, ClientRequestDTO request) {
        var client = clientRepository.findById(id).orElseThrow(() -> ServiceException.entityNotFound(ExceptionMessages.CAN_NOT_FIND_CLIENT));
        client.setFirstName(request.getFirstName());
        client.setLastName(request.getLastName());
        client.setPhoneNumber(request.getPhoneNumber());
        client.setEmail(request.getEmail());
        client.setPassword(request.getPassword());
        clientRepository.save(client);
    }

    @Override
    public Optional<ClientResponseDTO> deleteById(Long id) {
        var client = clientRepository.findById(id);
        client.ifPresent(clientRepository::delete);
        return client.map(ClientResponseDTO::fromClient);
    }

    public List<ClientResponseDTO> findAll() {
        return convertClientListToClientResponseDTOList(clientRepository.findAll());
    }
}
