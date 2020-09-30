package nix.finalproject.carrentalservice.service;

import nix.finalproject.carrentalservice.dto.CarStatusDTO;
import nix.finalproject.carrentalservice.dto.request.CarStatusRequestDTO;
import nix.finalproject.carrentalservice.entity.CarStatus;
import nix.finalproject.carrentalservice.exceptions.ExceptionMessages;
import nix.finalproject.carrentalservice.exceptions.ServiceException;
import nix.finalproject.carrentalservice.repository.CarStatusRepository;
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

public class CarStatusServiceTest {

    @Mock
    private CarStatusRepository carStatusRepository;

    private final CarStatusService carStatusService;

    public CarStatusServiceTest() {
        MockitoAnnotations.initMocks(this);
        this.carStatusService = new CarStatusService(carStatusRepository);
    }

    @Test
    public void methodShouldReturnCorrectStatus() {
        Long id1 = 1L;
        Long id2 = 2L;

        CarStatus carStatus1 = new CarStatus(id1, "Available");
        CarStatus carStatus2 = new CarStatus(id2, "Broken");

        given(carStatusRepository.findById(id1)).willReturn(Optional.of(carStatus1));
        given(carStatusRepository.findById(id2)).willReturn(Optional.of(carStatus2));

        CarStatusDTO carStatusDTO1 = carStatusService.findCarStatusDTOById(id1);
        CarStatusDTO carStatusDTO2 = carStatusService.findCarStatusDTOById(id2);

        Assert.assertEquals("Available", carStatusDTO1.getStatus());
        Assert.assertEquals("Broken", carStatusDTO2.getStatus());

        verify(carStatusRepository).findById(id1);
        verify(carStatusRepository).findById(id2);
    }

    @Test(expected = ResponseStatusException.class)
    public void methodShouldThrowException() {
        Long absentId = 20L;
        Long presentId = 1L;

        CarStatus carStatus = new CarStatus(presentId, "Available");

        given(carStatusRepository.findById(absentId)).willThrow(ServiceException.entityNotFound(ExceptionMessages.CAN_NOT_FIND_CAR_STATUS));
        given(carStatusRepository.findById(presentId)).willReturn(Optional.of(carStatus));

        CarStatusDTO carStatusDTO = carStatusService.findCarStatusDTOById(absentId);
        assertThat(carStatusDTO).isNull();

        verify(carStatusRepository).findById(absentId);
    }

    @Test
    public void methodShouldReturnAllStatus() {
        List<CarStatus> carStatusList = new ArrayList<>();
        carStatusList.add(new CarStatus(1L, "Available"));
        carStatusList.add(new CarStatus(2L, "Broken"));
        carStatusList.add(new CarStatus(3L, "Unavailable"));

        given(carStatusRepository.findAll()).willReturn(carStatusList);

        List<CarStatusDTO> carStatusDTOList = carStatusService.findAllCarStatusDTO();

        Assert.assertEquals(carStatusList.get(0).getStatus(), carStatusDTOList.get(0).getStatus());
        Assert.assertEquals(carStatusList.get(1).getStatus(), carStatusDTOList.get(1).getStatus());
        Assert.assertEquals(carStatusList.get(2).getStatus(), carStatusDTOList.get(2).getStatus());
    }

    @Test
    public void methodShouldSaveNewStatusSuccessfully() {
        CarStatus carStatus = new CarStatus(1L, "Available");

        given(carStatusRepository.findById(carStatus.getId())).willReturn(Optional.empty());
        given(carStatusRepository.save(carStatus)).willAnswer(invocation -> invocation.getArgument(0));

        CarStatusRequestDTO carStatusRequestDTO = carStatusService.createNewStatus(carStatus);

        assertThat(carStatusRequestDTO).isNotNull();
        assertThat(carStatusRequestDTO.getStatus()).isEqualTo(carStatus.getStatus());

        verify(carStatusRepository).save(any(CarStatus.class));
    }

    @Test
    public void methodShouldDeleteStatus() {
        Long id = 1L;

        CarStatus carStatus = new CarStatus(id, "Available");

        when(carStatusRepository.findById(id)).thenReturn(Optional.of(carStatus));

        carStatusService.deleteById(id);

        verify(carStatusRepository).deleteById(id);
    }

}
