package nix.finalproject.carrentalservice.service;

import nix.finalproject.carrentalservice.dto.CarTypeDTO;
import nix.finalproject.carrentalservice.dto.request.CarTypeRequestDTO;
import nix.finalproject.carrentalservice.entity.CarType;
import nix.finalproject.carrentalservice.exceptions.ExceptionMessages;
import nix.finalproject.carrentalservice.exceptions.ServiceException;
import nix.finalproject.carrentalservice.repository.CarTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class CarTypeService {

    private final CarTypeRepository carTypeRepository;

    @Autowired
    public CarTypeService(CarTypeRepository carTypeRepository) {
        this.carTypeRepository = carTypeRepository;
    }

    public CarTypeDTO convertCarTypeToCarTypeDTO(CarType carType) {
        return new CarTypeDTO(carType.getId(), carType.getBodyType());
    }

    public List<CarTypeDTO> convertCarTypeListToCarTypeDTOList(List<CarType> carTypeList) {
        return carTypeList.stream().map(this::convertCarTypeToCarTypeDTO).collect(Collectors.toList());
    }

    public List<CarTypeDTO> findAllCarTypeDTO() {
        return convertCarTypeListToCarTypeDTOList(carTypeRepository.findAll());
    }

    public CarTypeDTO findCarTypeDTOById(Long id) {
        CarType carType = carTypeRepository.findById(id).orElseThrow(() -> ServiceException.entityNotFound(ExceptionMessages.CAN_NOT_FIND_CAR_TYPE));
        return convertCarTypeToCarTypeDTO(carType);
    }

    public CarTypeRequestDTO convertCarTypeToCarTypeRequestDTO(CarType carType) {
        return new CarTypeRequestDTO(carType.getBodyType());
    }

    public CarTypeRequestDTO createNewType(CarType carType) {
        return convertCarTypeToCarTypeRequestDTO(carTypeRepository.save(carType));
    }

    public void deleteById(Long id) {
        carTypeRepository.deleteById(id);
    }
}
