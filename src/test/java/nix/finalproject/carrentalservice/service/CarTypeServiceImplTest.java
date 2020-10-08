package nix.finalproject.carrentalservice.service;

import nix.finalproject.carrentalservice.dto.CarTypeResponseDTO;
import nix.finalproject.carrentalservice.dto.request.CarTypeRequestDTO;
import nix.finalproject.carrentalservice.entity.CarType;
import nix.finalproject.carrentalservice.exceptions.ExceptionMessages;
import nix.finalproject.carrentalservice.exceptions.ServiceException;
import nix.finalproject.carrentalservice.repository.CarTypeRepository;
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

public class CarTypeServiceImplTest {

    @Mock
    private CarTypeRepository carTypeRepository;

    private final CarTypeServiceImpl carTypeServiceImpl;

    public CarTypeServiceImplTest() {
        MockitoAnnotations.initMocks(this);
        this.carTypeServiceImpl = new CarTypeServiceImpl(carTypeRepository);
    }

    @Test
    public void getCarTypeByIdTest() {

        Long absentId = 1L;
        Long presentId = 2L;

        CarType carType = new CarType(presentId, "Sedan");
        when(carTypeRepository.findById(absentId)).thenReturn(Optional.empty());
        when(carTypeRepository.findById(presentId)).thenReturn(Optional.of(carType));

        Optional<CarTypeResponseDTO> absentResponse = carTypeServiceImpl.getById(absentId);

        assertThat(absentResponse).isEmpty();
        verify(carTypeRepository).findById(absentId);

        Optional<CarTypeResponseDTO> presentResponse = carTypeServiceImpl.getById(presentId);

        assertThat(presentResponse).hasValueSatisfying(carTypeResponse ->
                assertCarTypeMatchesResponse(carType, carTypeResponse));
        verify(carTypeRepository).findById(presentId);

        verifyNoMoreInteractions(carTypeRepository);
    }

    @Test()
    public void methodShouldThrowException() {

        Long absentId = 20L;
        Long presentId = 1L;

        CarType carType = new CarType(presentId, "Sedan");

        given(carTypeRepository.findById(absentId)).willThrow(ServiceException.entityNotFound(ExceptionMessages.CAN_NOT_FIND_CAR_TYPE));
        given(carTypeRepository.findById(presentId)).willReturn(Optional.of(carType));

        assertThrows(ResponseStatusException.class, () -> carTypeServiceImpl.getById(absentId));

        verify(carTypeRepository).findById(absentId);
    }

    @Test
    public void updateCarTypeByIdTest() {

        Long presentId = 1L;
        Long absentId = 2L;

        CarTypeRequestDTO update = new CarTypeRequestDTO("Sedan");

        CarType carType = new CarType (presentId, "SUV");

        when(carTypeRepository.findById(absentId)).thenReturn(Optional.empty());
        when(carTypeRepository.findById(presentId)).thenReturn(Optional.of(carType));
        when(carTypeRepository.save(same(carType))).thenReturn(carType);

        assertThatExceptionOfType(ResponseStatusException.class)
                .isThrownBy(() -> carTypeServiceImpl.update(absentId, update))
                .satisfies(e -> assertThat(e.getStatus()).isSameAs(HttpStatus.NOT_FOUND));

        verify(carTypeRepository).findById(absentId);

        carTypeServiceImpl.update(presentId, update);

        assertThat(carType.getBodyType()).isEqualTo(update.getBodyType());
        verify(carTypeRepository).findById(presentId);
        verify(carTypeRepository).save(same(carType));

        verifyNoMoreInteractions(carTypeRepository);
    }

    @Test
    public void methodShouldReturnAllTypes() {

        List<CarType> carTypeList = new ArrayList<>();
        carTypeList.add(new CarType(1L, "Sedan"));
        carTypeList.add(new CarType(2L, "SUV"));
        carTypeList.add(new CarType(3L, "Minivan"));


        given(carTypeRepository.findAll()).willReturn(carTypeList);

        List<CarTypeResponseDTO> carTypeResponseDTOList = carTypeServiceImpl.findAll();

        assertEquals(carTypeList.get(0).getBodyType(), carTypeResponseDTOList.get(0).getBodyType());
        assertEquals(carTypeList.get(1).getBodyType(), carTypeResponseDTOList.get(1).getBodyType());
        assertEquals(carTypeList.get(2).getBodyType(), carTypeResponseDTOList.get(2).getBodyType());
    }

    @Test
    public void createNewCarTypeTest() {

        CarTypeRequestDTO request = new CarTypeRequestDTO("Sedan");
        Long carTypeId = 1L;

        when(carTypeRepository.save(notNull())).thenAnswer(invocation -> {
            CarType entity = invocation.getArgument(0);
            assertThat(entity.getId()).isNull();
            assertThat(entity.getBodyType()).isEqualTo(request.getBodyType());
            entity.setId(carTypeId);
            return entity;
        });

        CarTypeResponseDTO response = carTypeServiceImpl.create(request);

        assertThat(response.getId()).isEqualTo(carTypeId);
        assertThat(response.getBodyType()).isEqualTo(request.getBodyType());
        verify(carTypeRepository, only()).save(notNull());
    }

    @Test
    public void deleteCarTypeTest() {

        Long absentId = 20L;
        Long presentId = 1L;
        CarType carType = new CarType(presentId, "Sedan");

        when(carTypeRepository.findById(absentId)).thenReturn(Optional.empty());
        when(carTypeRepository.findById(presentId)).thenReturn(Optional.of(carType));

        Optional<CarTypeResponseDTO> absentResponse = carTypeServiceImpl.deleteById(absentId);

        assertThat(absentResponse).isEmpty();
        verify(carTypeRepository).findById(absentId);

        Optional<CarTypeResponseDTO> presentResponse = carTypeServiceImpl.deleteById(presentId);

        assertThat(presentResponse).hasValueSatisfying(carTypeResponse ->
                assertCarTypeMatchesResponse(carType, carTypeResponse));
        verify(carTypeRepository).findById(presentId);
        verify(carTypeRepository).delete(carType);

        verifyNoMoreInteractions(carTypeRepository);
    }

    private static void assertCarTypeMatchesResponse(CarType carType, CarTypeResponseDTO carTypeResponseDTO) {
        assertThat(carTypeResponseDTO.getId()).isEqualTo(carType.getId());
        assertThat(carTypeResponseDTO.getBodyType()).isEqualTo(carType.getBodyType());
    }
}
