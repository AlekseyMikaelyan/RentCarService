package nix.finalproject.carrentalservice.service;

import nix.finalproject.carrentalservice.dto.EngineTypeDTO;
import nix.finalproject.carrentalservice.dto.request.EngineTypeRequestDTO;
import nix.finalproject.carrentalservice.entity.EngineType;
import nix.finalproject.carrentalservice.exceptions.ExceptionMessages;
import nix.finalproject.carrentalservice.exceptions.ServiceException;
import nix.finalproject.carrentalservice.repository.EngineTypeRepository;
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

public class EngineTypeServiceTest {

    @Mock
    private EngineTypeRepository engineTypeRepository;

    private final EngineTypeService engineTypeService;

    public EngineTypeServiceTest() {
        MockitoAnnotations.initMocks(this);
        this.engineTypeService = new EngineTypeService(engineTypeRepository);
    }

    @Test
    public void methodShouldReturnCorrectEngineCapacity() {
        Long id1 = 1L;
        Long id2 = 2L;

        EngineType engineType1 = new EngineType(id1, "Gasoline", "1.2");
        EngineType engineType2 = new EngineType(id2, "Diesel", "1.5");

        given(engineTypeRepository.findById(id1)).willReturn(Optional.of(engineType1));
        given(engineTypeRepository.findById(id2)).willReturn(Optional.of(engineType2));

        EngineTypeDTO engineTypeDTO1 = engineTypeService.findEngineTypeDTOById(id1);
        EngineTypeDTO engineTypeDTO2 = engineTypeService.findEngineTypeDTOById(id2);

        Assert.assertEquals("1.2", engineTypeDTO1.getEngineCapacity());
        Assert.assertEquals("1.5", engineTypeDTO2.getEngineCapacity());

        verify(engineTypeRepository).findById(id1);
        verify(engineTypeRepository).findById(id2);
    }

    @Test(expected = ResponseStatusException.class)
    public void methodShouldThrowException() {
        Long absentId = 20L;
        Long presentId = 1L;

        EngineType engineType = new EngineType(presentId, "Diesel", "1.5");

        given(engineTypeRepository.findById(absentId)).willThrow(ServiceException.entityNotFound(ExceptionMessages.CAN_NOT_FIND_ENGINE));
        given(engineTypeRepository.findById(presentId)).willReturn(Optional.of(engineType));

        EngineTypeDTO engineTypeDTO = engineTypeService.findEngineTypeDTOById(absentId);
        assertThat(engineTypeDTO).isNull();

        verify(engineTypeRepository).findById(absentId);
    }

    @Test
    public void methodShouldReturnAllEngines() {
        List<EngineType> engineTypeList = new ArrayList<>();
        engineTypeList.add(new EngineType(1L, "Gasoline", "1.2"));
        engineTypeList.add(new EngineType(2L, "Gasoline", "1.6"));
        engineTypeList.add(new EngineType(3L, "Diesel", "1.5"));

        given(engineTypeRepository.findAll()).willReturn(engineTypeList);

        List<EngineTypeDTO> engineTypeDTOList = engineTypeService.findAllEngineTypeDTO();

        Assert.assertEquals(engineTypeList.get(0).getCapacity(), engineTypeDTOList.get(0).getEngineCapacity());
        Assert.assertEquals(engineTypeList.get(1).getCapacity(), engineTypeDTOList.get(1).getEngineCapacity());
        Assert.assertEquals(engineTypeList.get(2).getCapacity(), engineTypeDTOList.get(2).getEngineCapacity());
    }

    @Test
    public void methodShouldSaveNewEngineSuccessfully() {
        EngineType engineType = new EngineType(1L, "Diesel", "1.5");

        given(engineTypeRepository.findById(engineType.getId())).willReturn(Optional.empty());
        given(engineTypeRepository.save(engineType)).willAnswer(invocation -> invocation.getArgument(0));

        EngineTypeRequestDTO engineTypeRequestDTO = engineTypeService.createNewEngineType(engineType);

        assertThat(engineTypeRequestDTO).isNotNull();
        assertThat(engineTypeRequestDTO.getCapacity()).isEqualTo(engineType.getCapacity());

        verify(engineTypeRepository).save(any(EngineType.class));
    }

    @Test
    public void methodShouldDeleteEngine() {
        Long id = 1L;

        EngineType engineType = new EngineType(1L, "Diesel", "1.5");

        when(engineTypeRepository.findById(id)).thenReturn(Optional.of(engineType));

        engineTypeService.deleteById(id);

        verify(engineTypeRepository).deleteById(id);
    }
}
