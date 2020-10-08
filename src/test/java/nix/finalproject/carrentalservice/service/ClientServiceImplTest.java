package nix.finalproject.carrentalservice.service;

import nix.finalproject.carrentalservice.dto.ClientResponseDTO;
import nix.finalproject.carrentalservice.dto.request.ClientRequestDTO;
import nix.finalproject.carrentalservice.entity.Client;
import nix.finalproject.carrentalservice.exceptions.ExceptionMessages;
import nix.finalproject.carrentalservice.exceptions.ServiceException;
import nix.finalproject.carrentalservice.repository.ClientRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.notNull;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verifyNoMoreInteractions;

public class ClientServiceImplTest {

    @Mock
    private ClientRepository clientRepository;

    private final ClientServiceImpl clientServiceImpl;

    public ClientServiceImplTest() {
        MockitoAnnotations.initMocks(this);
        this.clientServiceImpl = new ClientServiceImpl(clientRepository);
    }

    @Test
    public void getClientByIdTest() {

        Long absentId = 1L;
        Long presentId = 2L;

        Client client = new Client(presentId, "Aleksey", "Alekseev", "+380990964311", "aleks@Gmail.com", "password1");
        when(clientRepository.findById(absentId)).thenReturn(Optional.empty());
        when(clientRepository.findById(presentId)).thenReturn(Optional.of(client));

        Optional<ClientResponseDTO> absentResponse = clientServiceImpl.getById(absentId);

        assertThat(absentResponse).isEmpty();
        verify(clientRepository).findById(absentId);

        Optional<ClientResponseDTO> presentResponse = clientServiceImpl.getById(presentId);

        assertThat(presentResponse).hasValueSatisfying(clientResponse ->
                assertClientMatchesResponse(client, clientResponse));
        verify(clientRepository).findById(presentId);

        verifyNoMoreInteractions(clientRepository);
    }

    @Test()
    public void methodShouldThrowException() {

        Long absentId = 20L;
        Long presentId = 1L;

        Client client = new Client(presentId, "Aleksey", "Alekseev", "+380990964311", "aleks@Gmail.com", "password1");

        given(clientRepository.findById(absentId)).willThrow(ServiceException.entityNotFound(ExceptionMessages.CAN_NOT_FIND_CLIENT));
        given(clientRepository.findById(presentId)).willReturn(Optional.of(client));

        assertThrows(ResponseStatusException.class, () -> clientServiceImpl.getById(absentId));

        verify(clientRepository).findById(absentId);
    }

    @Test
    public void updateClientByIdTest() {

        Long presentId = 1L;
        Long absentId = 2L;

        ClientRequestDTO update = new ClientRequestDTO("Aleksey", "Alekseev", "+380990964311", "aleks@Gmail.com", "password1");

        Client client = new Client(presentId,"Sergey", "Sergeev", "+380936435199", "seg@gmail.com", "password2");

        when(clientRepository.findById(absentId)).thenReturn(Optional.empty());
        when(clientRepository.findById(presentId)).thenReturn(Optional.of(client));
        when(clientRepository.save(same(client))).thenReturn(client);

        assertThatExceptionOfType(ResponseStatusException.class)
                .isThrownBy(() -> clientServiceImpl.update(absentId, update))
                .satisfies(e -> assertThat(e.getStatus()).isSameAs(HttpStatus.NOT_FOUND));

        verify(clientRepository).findById(absentId);

        clientServiceImpl.update(presentId, update);

        assertThat(client.getFirstName()).isEqualTo(update.getFirstName());
        assertThat(client.getLastName()).isEqualTo(update.getLastName());
        assertThat(client.getEmail()).isEqualTo(update.getEmail());
        verify(clientRepository).findById(presentId);
        verify(clientRepository).save(same(client));

        verifyNoMoreInteractions(clientRepository);
    }

    @Test
    public void methodShouldReturnCorrectEmail() {
        List<Client> clientList = new ArrayList<>();
        Client client1 = new Client(1L, "Aleksey", "Alekseev", "+380936435199", "aleks@gmail.com", "password1");
        Client client2 = new Client(2L, "Sergey", "Sergeev", "+380990964311", "ser@gmail.com", "password2");
        Client client3 = new Client(3L, "Andrey", "Andreev", "+380990964315", "andr@gmail.com", "password3");
        clientList.add(client1);
        clientList.add(client2);
        clientList.add(client3);

        given(clientRepository.findAll()).willReturn(clientList);

        List<ClientResponseDTO> clientResponseDTOList = clientServiceImpl.findAll();

        assertEquals(clientList.get(0).getEmail(), clientResponseDTOList.get(0).getEmail());
        assertEquals(clientList.get(1).getEmail(), clientResponseDTOList.get(1).getEmail());
        assertEquals(clientList.get(2).getEmail(), clientResponseDTOList.get(2).getEmail());
    }

    @Test
    public void createNewClientTest() {

        ClientRequestDTO request = new ClientRequestDTO("Aleksey", "Alekseev", "+380990964311", "aleks@Gmail.com", "password1");
        Long clientId = 1L;

        when(clientRepository.save(notNull())).thenAnswer(invocation -> {
            Client entity = invocation.getArgument(0);
            assertThat(entity.getId()).isNull();
            assertThat(entity.getFirstName()).isEqualTo(request.getFirstName());
            assertThat(entity.getLastName()).isEqualTo(request.getLastName());
            assertThat(entity.getPhoneNumber()).isEqualTo(request.getPhoneNumber());
            assertThat(entity.getEmail()).isEqualTo(request.getEmail());
            assertThat(entity.getPassword()).isEqualTo(request.getPassword());
            entity.setId(clientId);
            return entity;
        });

        ClientResponseDTO response = clientServiceImpl.create(request);

        assertThat(response.getId()).isEqualTo(clientId);
        assertThat(response.getFirstName()).isEqualTo(request.getFirstName());
        assertThat(response.getLastName()).isEqualTo(request.getLastName());
        assertThat(response.getPhoneNumber()).isEqualTo(request.getPhoneNumber());
        assertThat(response.getEmail()).isEqualTo(request.getEmail());
        verify(clientRepository, only()).save(notNull());
    }

    @Test
    public void deleteClientTest() {

        Long absentId = 20L;
        Long presentId = 1L;
        Client client = new Client(presentId, "Andrey", "Andreev", "+380990964315", "andr@gmail.com", "password3");

        when(clientRepository.findById(absentId)).thenReturn(Optional.empty());
        when(clientRepository.findById(presentId)).thenReturn(Optional.of(client));

        Optional<ClientResponseDTO> absentResponse = clientServiceImpl.deleteById(absentId);

        assertThat(absentResponse).isEmpty();
        verify(clientRepository).findById(absentId);

        Optional<ClientResponseDTO> presentResponse = clientServiceImpl.deleteById(presentId);

        assertThat(presentResponse).hasValueSatisfying(clientResponse ->
                assertClientMatchesResponse(client, clientResponse));
        verify(clientRepository).findById(presentId);
        verify(clientRepository).delete(client);

        verifyNoMoreInteractions(clientRepository);
    }

    private static void assertClientMatchesResponse(Client client, ClientResponseDTO clientResponseDTO) {
        assertThat(clientResponseDTO.getId()).isEqualTo(client.getId());
        assertThat(clientResponseDTO.getFirstName()).isEqualTo(client.getFirstName());
        assertThat(clientResponseDTO.getLastName()).isEqualTo(client.getLastName());
        assertThat(clientResponseDTO.getPhoneNumber()).isEqualTo(client.getPhoneNumber());
        assertThat(clientResponseDTO.getEmail()).isEqualTo(client.getEmail());
    }
}
