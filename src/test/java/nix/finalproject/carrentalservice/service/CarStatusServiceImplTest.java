package nix.finalproject.carrentalservice.service;

import nix.finalproject.carrentalservice.dto.CarStatusResponseDTO;
import nix.finalproject.carrentalservice.dto.request.CarStatusRequestDTO;
import nix.finalproject.carrentalservice.entity.CarStatus;
import nix.finalproject.carrentalservice.exceptions.ExceptionMessages;
import nix.finalproject.carrentalservice.exceptions.ServiceException;
import nix.finalproject.carrentalservice.repository.CarStatusRepository;
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

public class CarStatusServiceImplTest {

    @Mock
    private CarStatusRepository carStatusRepository;

    private final CarStatusServiceImpl carStatusServiceImpl;

    public CarStatusServiceImplTest() {
        MockitoAnnotations.initMocks(this);
        this.carStatusServiceImpl = new CarStatusServiceImpl(carStatusRepository);
    }

    @Test
    public void getCarStatusByIdTest() {

        Long absentId = 1L;
        Long presentId = 2L;

        CarStatus carStatus = new CarStatus(presentId, "Available");
        when(carStatusRepository.findById(absentId)).thenReturn(Optional.empty());
        when(carStatusRepository.findById(presentId)).thenReturn(Optional.of(carStatus));

        Optional<CarStatusResponseDTO> absentResponse = carStatusServiceImpl.getById(absentId);

        assertThat(absentResponse).isEmpty();
        verify(carStatusRepository).findById(absentId);

        Optional<CarStatusResponseDTO> presentResponse = carStatusServiceImpl.getById(presentId);

        assertThat(presentResponse).hasValueSatisfying(carStatusResponse ->
                assertCarStatusMatchesResponse(carStatus, carStatusResponse));
        verify(carStatusRepository).findById(presentId);

        verifyNoMoreInteractions(carStatusRepository);
    }

    @Test()
    public void methodShouldThrowException() {

        Long absentId = 20L;
        Long presentId = 1L;

        CarStatus carStatus = new CarStatus(presentId, "Available");

        given(carStatusRepository.findById(absentId)).willThrow(ServiceException.entityNotFound(ExceptionMessages.CAN_NOT_FIND_CAR_STATUS));
        given(carStatusRepository.findById(presentId)).willReturn(Optional.of(carStatus));

        assertThrows(ResponseStatusException.class, () -> carStatusServiceImpl.getById(absentId));

        verify(carStatusRepository).findById(absentId);
    }

    @Test
    public void updateCarStatusByIdTest() {

        Long presentId = 1L;
        Long absentId = 2L;

        CarStatusRequestDTO update = new CarStatusRequestDTO("Available");

        CarStatus carStatus = new CarStatus(presentId, "Broken");

        when(carStatusRepository.findById(absentId)).thenReturn(Optional.empty());
        when(carStatusRepository.findById(presentId)).thenReturn(Optional.of(carStatus));
        when(carStatusRepository.save(same(carStatus))).thenReturn(carStatus);

        assertThatExceptionOfType(ResponseStatusException.class)
                .isThrownBy(() -> carStatusServiceImpl.update(absentId, update))
                .satisfies(e -> assertThat(e.getStatus()).isSameAs(HttpStatus.NOT_FOUND));

        verify(carStatusRepository).findById(absentId);

        carStatusServiceImpl.update(presentId, update);

        assertThat(carStatus.getStatus()).isEqualTo(update.getStatus());
        verify(carStatusRepository).findById(presentId);
        verify(carStatusRepository).save(same(carStatus));

        verifyNoMoreInteractions(carStatusRepository);
    }

    @Test
    public void methodShouldReturnAllStatuses() {

        List<CarStatus> carStatusList = new ArrayList<>();
        carStatusList.add(new CarStatus(1L, "Available"));
        carStatusList.add(new CarStatus(2L, "Broken"));
        carStatusList.add(new CarStatus(3L, "Unavailable"));

        given(carStatusRepository.findAll()).willReturn(carStatusList);

        List<CarStatusResponseDTO> carStatusResponseDTOList = carStatusServiceImpl.findAll();

        assertEquals(carStatusList.get(0).getStatus(), carStatusResponseDTOList.get(0).getStatus());
        assertEquals(carStatusList.get(1).getStatus(), carStatusResponseDTOList.get(1).getStatus());
        assertEquals(carStatusList.get(2).getStatus(), carStatusResponseDTOList.get(2).getStatus());
    }

    @Test
    public void createNewCarStatusTest() {

        CarStatusRequestDTO request = new CarStatusRequestDTO("Available");
        Long carStatusId = 1L;

        when(carStatusRepository.save(notNull())).thenAnswer(invocation -> {
            CarStatus entity = invocation.getArgument(0);
            assertThat(entity.getId()).isNull();
            assertThat(entity.getStatus()).isEqualTo(request.getStatus());
            entity.setId(carStatusId);
            return entity;
        });

        CarStatusResponseDTO response = carStatusServiceImpl.create(request);

        assertThat(response.getId()).isEqualTo(carStatusId);
        assertThat(response.getStatus()).isEqualTo(request.getStatus());
        verify(carStatusRepository, only()).save(notNull());
    }

    @Test
    public void deleteCarStatusTest() {

        Long absentId = 20L;
        Long presentId = 1L;
        CarStatus carStatus = new CarStatus(presentId, "Available");

        when(carStatusRepository.findById(absentId)).thenReturn(Optional.empty());
        when(carStatusRepository.findById(presentId)).thenReturn(Optional.of(carStatus));

        Optional<CarStatusResponseDTO> absentResponse = carStatusServiceImpl.deleteById(absentId);

        assertThat(absentResponse).isEmpty();
        verify(carStatusRepository).findById(absentId);

        Optional<CarStatusResponseDTO> presentResponse = carStatusServiceImpl.deleteById(presentId);

        assertThat(presentResponse).hasValueSatisfying(carStatusResponse ->
                assertCarStatusMatchesResponse(carStatus, carStatusResponse));
        verify(carStatusRepository).findById(presentId);
        verify(carStatusRepository).delete(carStatus);

        verifyNoMoreInteractions(carStatusRepository);
    }

    private static void assertCarStatusMatchesResponse(CarStatus carStatus, CarStatusResponseDTO carStatusResponseDTO) {
        assertThat(carStatusResponseDTO.getId()).isEqualTo(carStatus.getId());
        assertThat(carStatusResponseDTO.getStatus()).isEqualTo(carStatus.getStatus());
    }
}
