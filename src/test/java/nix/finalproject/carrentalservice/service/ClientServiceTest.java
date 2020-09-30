package nix.finalproject.carrentalservice.service;

import nix.finalproject.carrentalservice.dto.ClientDTO;
import nix.finalproject.carrentalservice.dto.request.ClientRequestDTO;
import nix.finalproject.carrentalservice.entity.Client;
import nix.finalproject.carrentalservice.exceptions.ExceptionMessages;
import nix.finalproject.carrentalservice.exceptions.ServiceException;
import nix.finalproject.carrentalservice.repository.ClientRepository;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ClientServiceTest {

    @Mock
    private ClientRepository clientRepository;

    private final ClientService clientService;

    public ClientServiceTest() {
        MockitoAnnotations.initMocks(this);
        this.clientService = new ClientService(clientRepository);
    }

    @Test
    public void methodShouldReturnCorrectBrandName() {
        Long id1 = 1L;
        Long id2 = 2L;

        Client client1 = new Client(id1, "Aleksey", "Alekseev", "+380936435199", "aleks@gmail.com", "password1");
        Client client2 = new Client(id2, "Sergey", "Sergeev", "+380990964311", "ser@gmail.com", "password2");


        given(clientRepository.findById(id1)).willReturn(Optional.of(client1));
        given(clientRepository.findById(id2)).willReturn(Optional.of(client2));

        ClientDTO clientDTO1 = clientService.findClientDTOById(id1);
        ClientDTO clientDTO2 = clientService.findClientDTOById(id2);

        Assert.assertEquals("aleks@gmail.com", clientDTO1.getEmail());
        Assert.assertEquals("ser@gmail.com", clientDTO2.getEmail());

        verify(clientRepository).findById(id1);
        verify(clientRepository).findById(id2);
    }

    @Test(expected = ResponseStatusException.class)
    public void methodShouldThrowException() {
        Long absentId = 20L;
        Long presentId = 1L;

        Client client = new Client(presentId, "Aleksey", "Alekseev", "+380936435199", "aleks@gmail.com", "password1");

        given(clientRepository.findById(absentId)).willThrow(ServiceException.entityNotFound(ExceptionMessages.CAN_NOT_FIND_CLIENT));
        given(clientRepository.findById(presentId)).willReturn(Optional.of(client));

        ClientDTO clientDTO = clientService.findClientDTOById(absentId);
        assertThat(clientDTO).isNull();

        verify(clientRepository).findById(absentId);
    }

    @Test
    public void methodShouldReturnAllBrands() {
        List<Client> clientList = new ArrayList<>();
        Client client1 = new Client(1L, "Aleksey", "Alekseev", "+380936435199", "aleks@gmail.com", "password1");
        Client client2 = new Client(2L, "Sergey", "Sergeev", "+380990964311", "ser@gmail.com", "password2");
        Client client3 = new Client(3L, "Andrey", "Andreev", "+380990964315", "andr@gmail.com", "password3");
        clientList.add(client1);
        clientList.add(client2);
        clientList.add(client3);

        given(clientRepository.findAll()).willReturn(clientList);

        List<ClientDTO> clientDTOList = clientService.findAllClientDTO();

        Assert.assertEquals(clientList.get(0).getEmail(), clientDTOList.get(0).getEmail());
        Assert.assertEquals(clientList.get(1).getEmail(), clientDTOList.get(1).getEmail());
        Assert.assertEquals(clientList.get(2).getEmail(), clientDTOList.get(2).getEmail());
    }

    @Test
    public void methodShouldSaveNewBrandSuccessfully() {
        Client client = new Client(1L, "Aleksey", "Alekseev", "+380936435199", "aleks@gmail.com", "password1");

        given(clientRepository.findById(client.getId())).willReturn(Optional.empty());
        given(clientRepository.save(client)).willAnswer(invocation -> invocation.getArgument(0));

        ClientRequestDTO clientRequestDTO = clientService.createNewClient(client);

        assertThat(clientRequestDTO).isNotNull();
        assertThat(clientRequestDTO.getFirstName()).isEqualTo(client.getFirstName());

        verify(clientRepository).save(any(Client.class));
    }

    @Test
    public void methodShouldDeleteBrand() {
        Long id = 1L;

        Client client = new Client(id, "Aleksey", "Alekseev", "+380936435199", "aleks@gmail.com", "password1");

        when(clientRepository.findById(id)).thenReturn(Optional.of(client));

        clientService.deleteById(id);

        verify(clientRepository).deleteById(id);
    }
}
