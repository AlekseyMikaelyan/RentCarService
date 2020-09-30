package nix.finalproject.carrentalservice.service;

import nix.finalproject.carrentalservice.dto.CarStatusDTO;
import nix.finalproject.carrentalservice.dto.request.CarStatusRequestDTO;
import nix.finalproject.carrentalservice.entity.CarStatus;
import nix.finalproject.carrentalservice.exceptions.ExceptionMessages;
import nix.finalproject.carrentalservice.exceptions.ServiceException;
import nix.finalproject.carrentalservice.repository.CarStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class CarStatusService {

    private final CarStatusRepository carStatusRepository;

    @Autowired
    public CarStatusService(CarStatusRepository carStatusRepository) {
        this.carStatusRepository = carStatusRepository;
    }

    public CarStatusDTO convertCarStatusToCarStatusDTO(CarStatus carStatus) {
        return new CarStatusDTO(carStatus.getId(), carStatus.getStatus());
    }

    public List<CarStatusDTO> convertListCarStatusToCarStatusDTOList(List<CarStatus> carStatusList) {
        return carStatusList.stream().map(this::convertCarStatusToCarStatusDTO).collect(Collectors.toList());
    }

    public List<CarStatusDTO> findAllCarStatusDTO() {
        return convertListCarStatusToCarStatusDTOList(carStatusRepository.findAll());
    }

    public CarStatusDTO findCarStatusDTOById(Long id) {
        CarStatus carStatus = carStatusRepository.findById(id).orElseThrow(() -> ServiceException.entityNotFound(ExceptionMessages.CAN_NOT_FIND_CAR_STATUS));
        return convertCarStatusToCarStatusDTO(carStatus);
    }

    public CarStatusRequestDTO convertCarStatusToCarStatusRequestDTO(CarStatus carStatus) {
        return new CarStatusRequestDTO(carStatus.getStatus());
    }

    public CarStatusRequestDTO createNewStatus(CarStatus carStatus) {
        return convertCarStatusToCarStatusRequestDTO(carStatusRepository.save(carStatus));
    }

    public void deleteById(Long id) {
        carStatusRepository.deleteById(id);
    }
}
