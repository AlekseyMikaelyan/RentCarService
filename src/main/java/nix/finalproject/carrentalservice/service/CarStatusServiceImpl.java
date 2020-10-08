package nix.finalproject.carrentalservice.service;

import nix.finalproject.carrentalservice.dto.CarStatusResponseDTO;
import nix.finalproject.carrentalservice.dto.request.CarStatusRequestDTO;
import nix.finalproject.carrentalservice.entity.CarStatus;
import nix.finalproject.carrentalservice.exceptions.ExceptionMessages;
import nix.finalproject.carrentalservice.exceptions.ServiceException;
import nix.finalproject.carrentalservice.repository.CarStatusRepository;
import nix.finalproject.carrentalservice.service.interfaces.CarStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class CarStatusServiceImpl implements CarStatusService {

    private final CarStatusRepository carStatusRepository;

    @Autowired
    public CarStatusServiceImpl(CarStatusRepository carStatusRepository) {
        this.carStatusRepository = carStatusRepository;
    }

    public List<CarStatusResponseDTO> convertListCarStatusToCarStatusResponseDTOList(List<CarStatus> carStatusList) {
        return carStatusList.stream().map(CarStatusResponseDTO::fromCarStatus).collect(Collectors.toList());
    }

    @Override
    public CarStatusResponseDTO create(CarStatusRequestDTO request) {
        var carStatus = new CarStatus(request.getStatus());
        return CarStatusResponseDTO.fromCarStatus(carStatusRepository.save(carStatus));
    }

    @Override
    public Optional<CarStatusResponseDTO> getById(Long id) {
        return carStatusRepository.findById(id).map(CarStatusResponseDTO::fromCarStatus);
    }

    @Override
    public void update(Long id, CarStatusRequestDTO request) {
        var carStatus = carStatusRepository.findById(id).orElseThrow(() -> ServiceException.entityNotFound(ExceptionMessages.CAN_NOT_FIND_CAR_STATUS));
        carStatus.setStatus(request.getStatus());
        carStatusRepository.save(carStatus);
    }

    @Override
    public Optional<CarStatusResponseDTO> deleteById(Long id) {
        var carStatus = carStatusRepository.findById(id);
        carStatus.ifPresent(carStatusRepository::delete);
        return carStatus.map(CarStatusResponseDTO::fromCarStatus);
    }

    public List<CarStatusResponseDTO> findAll() {
        return convertListCarStatusToCarStatusResponseDTOList(carStatusRepository.findAll());
    }
}
