package nix.finalproject.carrentalservice.service;

import nix.finalproject.carrentalservice.dto.CarTypeResponseDTO;
import nix.finalproject.carrentalservice.dto.request.CarTypeRequestDTO;
import nix.finalproject.carrentalservice.entity.CarType;
import nix.finalproject.carrentalservice.exceptions.ExceptionMessages;
import nix.finalproject.carrentalservice.exceptions.ServiceException;
import nix.finalproject.carrentalservice.repository.CarTypeRepository;
import nix.finalproject.carrentalservice.service.interfaces.CarTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class CarTypeServiceImpl implements CarTypeService {

    private final CarTypeRepository carTypeRepository;

    @Autowired
    public CarTypeServiceImpl(CarTypeRepository carTypeRepository) {
        this.carTypeRepository = carTypeRepository;
    }

    public List<CarTypeResponseDTO> convertCarTypeListToCarTypeResponseDTOList(List<CarType> carTypeList) {
        return carTypeList.stream().map(CarTypeResponseDTO::fromCarType).collect(Collectors.toList());
    }

    @Override
    public CarTypeResponseDTO create(CarTypeRequestDTO request) {
        var carType = new CarType(request.getBodyType());
        return CarTypeResponseDTO.fromCarType(carTypeRepository.save(carType));
    }

    @Override
    public Optional<CarTypeResponseDTO> getById(Long id) {
        return carTypeRepository.findById(id).map(CarTypeResponseDTO::fromCarType);
    }

    @Override
    public void update(Long id, CarTypeRequestDTO request) {
        var carType = carTypeRepository.findById(id).orElseThrow(() -> ServiceException.entityNotFound(ExceptionMessages.CAN_NOT_FIND_CAR_TYPE));
        carType.setBodyType(request.getBodyType());
        carTypeRepository.save(carType);
    }

    @Override
    public Optional<CarTypeResponseDTO> deleteById(Long id) {
        var carType = carTypeRepository.findById(id);
        carType.ifPresent(carTypeRepository::delete);
        return carType.map(CarTypeResponseDTO::fromCarType);
    }

    public List<CarTypeResponseDTO> findAll() {
        return convertCarTypeListToCarTypeResponseDTOList(carTypeRepository.findAll());
    }
}
