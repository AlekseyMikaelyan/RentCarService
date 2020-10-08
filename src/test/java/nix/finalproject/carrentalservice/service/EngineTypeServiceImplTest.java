package nix.finalproject.carrentalservice.service;

import nix.finalproject.carrentalservice.dto.EngineTypeResponseDTO;
import nix.finalproject.carrentalservice.dto.request.EngineTypeRequestDTO;
import nix.finalproject.carrentalservice.entity.EngineType;
import nix.finalproject.carrentalservice.exceptions.ExceptionMessages;
import nix.finalproject.carrentalservice.exceptions.ServiceException;
import nix.finalproject.carrentalservice.repository.EngineTypeRepository;
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

public class EngineTypeServiceImplTest {

    @Mock
    private EngineTypeRepository engineTypeRepository;

    private final EngineTypeServiceImpl engineTypeServiceImpl;

    public EngineTypeServiceImplTest() {
        MockitoAnnotations.initMocks(this);
        this.engineTypeServiceImpl = new EngineTypeServiceImpl(engineTypeRepository);
    }

    @Test
    public void getEngineTypeByIdTest() {

        Long absentId = 1L;
        Long presentId = 2L;

        EngineType engineType = new EngineType(presentId, "Diesel", "1.5");
        when(engineTypeRepository.findById(absentId)).thenReturn(Optional.empty());
        when(engineTypeRepository.findById(presentId)).thenReturn(Optional.of(engineType));

        Optional<EngineTypeResponseDTO> absentResponse = engineTypeServiceImpl.getById(absentId);

        assertThat(absentResponse).isEmpty();
        verify(engineTypeRepository).findById(absentId);

        Optional<EngineTypeResponseDTO> presentResponse = engineTypeServiceImpl.getById(presentId);

        assertThat(presentResponse).hasValueSatisfying(engineResponse ->
                assertEngineTypeMatchesResponse(engineType, engineResponse));
        verify(engineTypeRepository).findById(presentId);

        verifyNoMoreInteractions(engineTypeRepository);
    }

    @Test()
    public void methodShouldThrowException() {

        Long absentId = 20L;
        Long presentId = 1L;

        EngineType engineType = new EngineType(presentId, "Diesel", "1.5");

        given(engineTypeRepository.findById(absentId)).willThrow(ServiceException.entityNotFound(ExceptionMessages.CAN_NOT_FIND_ENGINE));
        given(engineTypeRepository.findById(presentId)).willReturn(Optional.of(engineType));

        assertThrows(ResponseStatusException.class, () -> engineTypeServiceImpl.getById(absentId));

        verify(engineTypeRepository).findById(absentId);
    }

    @Test
    public void updateEngineTypeByIdTest() {

        Long presentId = 1L;
        Long absentId = 2L;

        EngineTypeRequestDTO update = new EngineTypeRequestDTO("Diesel", "1.5");

        EngineType engineType = new EngineType(presentId,"Gasoline", "1.4");

        when(engineTypeRepository.findById(absentId)).thenReturn(Optional.empty());
        when(engineTypeRepository.findById(presentId)).thenReturn(Optional.of(engineType));
        when(engineTypeRepository.save(same(engineType))).thenReturn(engineType);

        assertThatExceptionOfType(ResponseStatusException.class)
                .isThrownBy(() -> engineTypeServiceImpl.update(absentId, update))
                .satisfies(e -> assertThat(e.getStatus()).isSameAs(HttpStatus.NOT_FOUND));

        verify(engineTypeRepository).findById(absentId);

        engineTypeServiceImpl.update(presentId, update);

        assertThat(engineType.getType()).isEqualTo(update.getType());
        assertThat(engineType.getCapacity()).isEqualTo(update.getCapacity());
        verify(engineTypeRepository).findById(presentId);
        verify(engineTypeRepository).save(same(engineType));

        verifyNoMoreInteractions(engineTypeRepository);
    }

    @Test
    public void methodShouldReturnCorrectCapacity() {

        List<EngineType> engineTypeList = new ArrayList<>();
        engineTypeList.add(new EngineType(1L, "Gasoline", "1.2"));
        engineTypeList.add(new EngineType(2L, "Gasoline", "1.6"));
        engineTypeList.add(new EngineType(3L, "Diesel", "1.5"));

        given(engineTypeRepository.findAll()).willReturn(engineTypeList);

        List<EngineTypeResponseDTO> engineTypeResponseDTOList = engineTypeServiceImpl.findAll();

        assertEquals(engineTypeList.get(0).getCapacity(), engineTypeResponseDTOList.get(0).getEngineCapacity());
        assertEquals(engineTypeList.get(1).getCapacity(), engineTypeResponseDTOList.get(1).getEngineCapacity());
        assertEquals(engineTypeList.get(2).getCapacity(), engineTypeResponseDTOList.get(2).getEngineCapacity());
    }

    @Test
    public void createNewEngineTest() {

        EngineTypeRequestDTO request = new EngineTypeRequestDTO("Diesel", "1.5");
        Long engineId = 1L;

        when(engineTypeRepository.save(notNull())).thenAnswer(invocation -> {
            EngineType entity = invocation.getArgument(0);
            assertThat(entity.getId()).isNull();
            assertThat(entity.getType()).isEqualTo(request.getType());
            assertThat(entity.getCapacity()).isEqualTo(request.getCapacity());
            entity.setId(engineId);
            return entity;
        });

        EngineTypeResponseDTO response = engineTypeServiceImpl.create(request);

        assertThat(response.getId()).isEqualTo(engineId);
        assertThat(response.getType()).isEqualTo(request.getType());
        assertThat(response.getEngineCapacity()).isEqualTo(request.getCapacity());
        verify(engineTypeRepository, only()).save(notNull());
    }

    @Test
    public void deleteEngineTest() {

        Long absentId = 20L;
        Long presentId = 1L;
        EngineType engineType = new EngineType(presentId, "Diesel", "Available");

        when(engineTypeRepository.findById(absentId)).thenReturn(Optional.empty());
        when(engineTypeRepository.findById(presentId)).thenReturn(Optional.of(engineType));

        Optional<EngineTypeResponseDTO> absentResponse = engineTypeServiceImpl.deleteById(absentId);

        assertThat(absentResponse).isEmpty();
        verify(engineTypeRepository).findById(absentId);

        Optional<EngineTypeResponseDTO> presentResponse = engineTypeServiceImpl.deleteById(presentId);

        assertThat(presentResponse).hasValueSatisfying(engineResponse ->
                assertEngineTypeMatchesResponse(engineType, engineResponse));
        verify(engineTypeRepository).findById(presentId);
        verify(engineTypeRepository).delete(engineType);

        verifyNoMoreInteractions(engineTypeRepository);
    }

    private static void assertEngineTypeMatchesResponse(EngineType engineType, EngineTypeResponseDTO engineTypeResponseDTO) {
        assertThat(engineTypeResponseDTO.getId()).isEqualTo(engineType.getId());
        assertThat(engineTypeResponseDTO.getType()).isEqualTo(engineType.getType());
        assertThat(engineTypeResponseDTO.getEngineCapacity()).isEqualTo(engineType.getCapacity());
    }
}
