package nix.finalproject.carrentalservice.service;

import nix.finalproject.carrentalservice.dto.EngineTypeResponseDTO;
import nix.finalproject.carrentalservice.dto.request.EngineTypeRequestDTO;
import nix.finalproject.carrentalservice.entity.EngineType;
import nix.finalproject.carrentalservice.exceptions.ExceptionMessages;
import nix.finalproject.carrentalservice.exceptions.ServiceException;
import nix.finalproject.carrentalservice.repository.EngineTypeRepository;
import nix.finalproject.carrentalservice.service.interfaces.EngineTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class EngineTypeServiceImpl implements EngineTypeService {

    private final EngineTypeRepository engineTypeRepository;

    @Autowired
    public EngineTypeServiceImpl(EngineTypeRepository engineTypeRepository) {
        this.engineTypeRepository = engineTypeRepository;
    }

    public List<EngineTypeResponseDTO> convertEngineTypeListToEngineTypeResponseDTOList(List<EngineType> engineTypeList) {
        return engineTypeList.stream().map(EngineTypeResponseDTO::fromEngineType).collect(Collectors.toList());
    }

    @Override
    public EngineTypeResponseDTO create(EngineTypeRequestDTO request) {
        var engineType = new EngineType(request.getType(), request.getCapacity());
        return EngineTypeResponseDTO.fromEngineType(engineTypeRepository.save(engineType));
    }

    @Override
    public Optional<EngineTypeResponseDTO> getById(Long id) {
        return engineTypeRepository.findById(id).map(EngineTypeResponseDTO::fromEngineType);
    }

    @Override
    public void update(Long id, EngineTypeRequestDTO request) {
        var engineType = engineTypeRepository.findById(id).orElseThrow(() -> ServiceException.entityNotFound(ExceptionMessages.CAN_NOT_FIND_ENGINE));
        engineType.setType(request.getType());
        engineType.setCapacity(request.getCapacity());
        engineTypeRepository.save(engineType);
    }

    @Override
    public Optional<EngineTypeResponseDTO> deleteById(Long id) {
        var engineType = engineTypeRepository.findById(id);
        engineType.ifPresent(engineTypeRepository::delete);
        return engineType.map(EngineTypeResponseDTO::fromEngineType);
    }

    public List<EngineTypeResponseDTO> findAll() {
        return convertEngineTypeListToEngineTypeResponseDTOList(engineTypeRepository.findAll());
    }
}

