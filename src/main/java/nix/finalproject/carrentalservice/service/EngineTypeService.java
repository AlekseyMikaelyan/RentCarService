package nix.finalproject.carrentalservice.service;

import nix.finalproject.carrentalservice.dto.EngineTypeDTO;
import nix.finalproject.carrentalservice.dto.request.EngineTypeRequestDTO;
import nix.finalproject.carrentalservice.entity.EngineType;
import nix.finalproject.carrentalservice.exceptions.ExceptionMessages;
import nix.finalproject.carrentalservice.exceptions.ServiceException;
import nix.finalproject.carrentalservice.repository.EngineTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class EngineTypeService {

    private final EngineTypeRepository engineTypeRepository;

    @Autowired
    public EngineTypeService(EngineTypeRepository engineTypeRepository) {
        this.engineTypeRepository = engineTypeRepository;
    }

    public EngineTypeDTO convertEngineTypeToEngineTypeDTO(EngineType engineType) {
        return new EngineTypeDTO(engineType.getId(), engineType.getType(), engineType.getCapacity());
    }

    public List<EngineTypeDTO> convertEngineTypeListToEngineTypeDTOList(List<EngineType> engineTypeList) {
        return engineTypeList.stream().map(this::convertEngineTypeToEngineTypeDTO).collect(Collectors.toList());
    }

    public List<EngineTypeDTO> findAllEngineTypeDTO() {
        return convertEngineTypeListToEngineTypeDTOList(engineTypeRepository.findAll());
    }

    public EngineTypeDTO findEngineTypeDTOById(Long id) {
        EngineType engineType = engineTypeRepository.findById(id).orElseThrow(() -> ServiceException.entityNotFound(ExceptionMessages.CAN_NOT_FIND_ENGINE));
        return convertEngineTypeToEngineTypeDTO(engineType);
    }

    public EngineTypeRequestDTO convertEngineTypeToEngineTypeRequestDTO(EngineType engineType) {
        return new EngineTypeRequestDTO(engineType.getType(), engineType.getCapacity());
    }

    public EngineTypeRequestDTO createNewEngineType(EngineType engineType) {
        return convertEngineTypeToEngineTypeRequestDTO(engineTypeRepository.save(engineType));
    }

    public void deleteById(Long id) {
        engineTypeRepository.deleteById(id);
    }
}

