package nix.finalproject.carrentalservice.service;

import nix.finalproject.carrentalservice.dto.CarResponseDTO;
import nix.finalproject.carrentalservice.dto.request.CarRequestDTO;
import nix.finalproject.carrentalservice.entity.*;
import nix.finalproject.carrentalservice.exceptions.ExceptionMessages;
import nix.finalproject.carrentalservice.exceptions.ServiceException;
import nix.finalproject.carrentalservice.repository.*;
import nix.finalproject.carrentalservice.service.interfaces.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class CarServiceImpl implements CarService {

    private final CarRepository carRepository;
    private final BrandRepository brandRepository;
    private final EngineTypeRepository engineTypeRepository;
    private final CarTypeRepository carTypeRepository;
    private final CarStatusRepository carStatusRepository;

    @Autowired
    public CarServiceImpl(CarRepository carRepository, BrandRepository brandRepository, EngineTypeRepository engineTypeRepository, CarTypeRepository carTypeRepository, CarStatusRepository carStatusRepository) {
        this.carRepository = carRepository;
        this.brandRepository = brandRepository;
        this.engineTypeRepository = engineTypeRepository;
        this.carTypeRepository = carTypeRepository;
        this.carStatusRepository = carStatusRepository;
    }

    public List<CarResponseDTO> convertCarListToCarResponseDTOList(List<Car> cars) {
        return cars.stream().map(CarResponseDTO::fromCar).collect(Collectors.toList());
    }

    @Override
    public CarResponseDTO create(CarRequestDTO request) {
        Brand brand = brandRepository.findById(request.getBrandId()).orElseThrow(() -> ServiceException.entityNotFound(ExceptionMessages.CAN_NOT_FIND_BRAND));

        EngineType engineType = engineTypeRepository.findById(request.getEngineTypeId()).orElseThrow(() -> ServiceException.entityNotFound(ExceptionMessages.CAN_NOT_FIND_ENGINE));

        CarType carType = carTypeRepository.findById(request.getCarTypeId()).orElseThrow(() -> ServiceException.entityNotFound(ExceptionMessages.CAN_NOT_FIND_CAR_TYPE));

        CarStatus carStatus = carStatusRepository.findById(request.getCarStatusId()).orElseThrow(() -> ServiceException.entityNotFound(ExceptionMessages.CAN_NOT_FIND_CAR_STATUS));

        Car car = new Car(brand,
                engineType,
                carType,
                request.getModel(),
                request.getTransmission(),
                request.getYearOfManufacture(),
                carStatus,
                request.getPrice());

        return CarResponseDTO.fromCar(carRepository.save(car));
    }

    @Override
    public Optional<CarResponseDTO> getById(Long id) {
        return carRepository.findById(id).map(CarResponseDTO::fromCar);
    }

    @Override
    public void update(Long id, CarRequestDTO request) {
        var car = carRepository.findById(id).orElseThrow(() -> ServiceException.entityNotFound(ExceptionMessages.CAN_NOT_FIND_CAR));
        car.setBrand(brandRepository.findById(request.getBrandId()).orElseThrow(() -> ServiceException.entityNotFound(ExceptionMessages.CAN_NOT_FIND_BRAND)));
        car.setEngineType(engineTypeRepository.findById(request.getEngineTypeId()).orElseThrow(() -> ServiceException.entityNotFound(ExceptionMessages.CAN_NOT_FIND_ENGINE)));
        car.setCarType(carTypeRepository.findById(request.getCarTypeId()).orElseThrow(() -> ServiceException.entityNotFound(ExceptionMessages.CAN_NOT_FIND_CAR_TYPE)));
        car.setModel(request.getModel());
        car.setTransmission(request.getTransmission());
        car.setYearOfManufacture(request.getYearOfManufacture());
        car.setCarStatus(carStatusRepository.findById(request.getCarStatusId()).orElseThrow(() -> ServiceException.entityNotFound(ExceptionMessages.CAN_NOT_FIND_CAR_STATUS)));
        car.setPrice(request.getPrice());
        carRepository.save(car);
    }

    @Override
    public Optional<CarResponseDTO> deleteById(Long id) {
        var car = carRepository.findById(id);
        car.ifPresent(carRepository::delete);
        return car.map(CarResponseDTO::fromCar);
    }

    public List<CarResponseDTO> findAll() {
        return convertCarListToCarResponseDTOList(carRepository.findAll());
    }

    public List<CarResponseDTO> findAllCarDTOByBrandName(String brandName) {
        return convertCarListToCarResponseDTOList(carRepository.findAllByBrand(brandName));
    }

    public List<CarResponseDTO> findAllCarDTOByEngine(String type) {
        return convertCarListToCarResponseDTOList(carRepository.findAllByEngine(type));
    }

    public List<CarResponseDTO> findAllCarDTOByStatus(String status) {
        return convertCarListToCarResponseDTOList(carRepository.findAllByStatus(status));
    }
}
