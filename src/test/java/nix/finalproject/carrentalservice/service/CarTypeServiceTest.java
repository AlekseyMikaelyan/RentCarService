package nix.finalproject.carrentalservice.service;

import nix.finalproject.carrentalservice.dto.CarTypeDTO;
import nix.finalproject.carrentalservice.dto.request.CarTypeRequestDTO;
import nix.finalproject.carrentalservice.entity.CarType;
import nix.finalproject.carrentalservice.exceptions.ExceptionMessages;
import nix.finalproject.carrentalservice.exceptions.ServiceException;
import nix.finalproject.carrentalservice.repository.CarTypeRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CarTypeServiceTest {

    @Mock
    private CarTypeRepository carTypeRepository;

    private final CarTypeService carTypeService;

    public CarTypeServiceTest() {
        MockitoAnnotations.initMocks(this);
        this.carTypeService = new CarTypeService(carTypeRepository);
    }

    @Test
    public void methodShouldReturnCorrectBodyType() {
        Long id1 = 1L;
        Long id2 = 2L;

        CarType carType1 = new CarType(id1, "Sedan");
        CarType carType2 = new CarType(id2, "SUV");

        given(carTypeRepository.findById(id1)).willReturn(Optional.of(carType1));
        given(carTypeRepository.findById(id2)).willReturn(Optional.of(carType2));

        CarTypeDTO carTypeDTO1 = carTypeService.findCarTypeDTOById(id1);
        CarTypeDTO carTypeDTO2 = carTypeService.findCarTypeDTOById(id2);

        assertEquals("Sedan", carTypeDTO1.getBodyType());
        assertEquals("SUV", carTypeDTO2.getBodyType());

        verify(carTypeRepository).findById(id1);
        verify(carTypeRepository).findById(id2);
    }

    @Test()
    public void methodShouldThrowException() {
        Long absentId = 20L;
        Long presentId = 1L;

        CarType carType = new CarType(presentId, "Sedan");

        given(carTypeRepository.findById(absentId)).willThrow(ServiceException.entityNotFound(ExceptionMessages.CAN_NOT_FIND_CAR_TYPE));
        given(carTypeRepository.findById(presentId)).willReturn(Optional.of(carType));

        assertThrows(ResponseStatusException.class, () -> carTypeService.findCarTypeDTOById(absentId));

        verify(carTypeRepository).findById(absentId);
    }

    @Test
    public void methodShouldReturnAllCarTypes() {
        List<CarType> carTypeList = new ArrayList<>();
        carTypeList.add(new CarType(1L, "Sedan"));
        carTypeList.add(new CarType(2L, "SUV"));
        carTypeList.add(new CarType(3L, "Minivan"));


        given(carTypeRepository.findAll()).willReturn(carTypeList);

        List<CarTypeDTO> carTypeDTOList = carTypeService.findAllCarTypeDTO();

        assertEquals(carTypeList.get(0).getBodyType(), carTypeDTOList.get(0).getBodyType());
        assertEquals(carTypeList.get(1).getBodyType(), carTypeDTOList.get(1).getBodyType());
        assertEquals(carTypeList.get(2).getBodyType(), carTypeDTOList.get(2).getBodyType());
    }

    @Test
    public void methodShouldSaveNewCarTypeSuccessfully() {
        CarType carType = new CarType(1L, "Sedan");

        given(carTypeRepository.findById(carType.getId())).willReturn(Optional.empty());
        given(carTypeRepository.save(carType)).willAnswer(invocation -> invocation.getArgument(0));

        CarTypeRequestDTO carTypeRequestDTO = carTypeService.createNewType(carType);

        assertThat(carTypeRequestDTO).isNotNull();
        assertThat(carTypeRequestDTO.getBodyType()).isEqualTo(carType.getBodyType());

        verify(carTypeRepository).save(any(CarType.class));
    }

    @Test
    public void methodShouldDeleteCarType() {
        Long id = 1L;

        CarType carType = new CarType(id, "Sedan");

        when(carTypeRepository.findById(id)).thenReturn(Optional.of(carType));

        carTypeService.deleteById(id);

        verify(carTypeRepository).deleteById(id);
    }
}
