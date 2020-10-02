package nix.finalproject.carrentalservice.service;

import nix.finalproject.carrentalservice.dto.CarDTO;
import nix.finalproject.carrentalservice.dto.request.CarRequestDTO;
import nix.finalproject.carrentalservice.entity.*;
import nix.finalproject.carrentalservice.exceptions.ExceptionMessages;
import nix.finalproject.carrentalservice.exceptions.ServiceException;
import nix.finalproject.carrentalservice.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class CarService {

    private final CarRepository carRepository;
    private final BrandRepository brandRepository;
    private final EngineTypeRepository engineTypeRepository;
    private final CarTypeRepository carTypeRepository;
    private final CarStatusRepository carStatusRepository;

    @Autowired
    public CarService(CarRepository carRepository, BrandRepository brandRepository, EngineTypeRepository engineTypeRepository, CarTypeRepository carTypeRepository, CarStatusRepository carStatusRepository) {
        this.carRepository = carRepository;
        this.brandRepository = brandRepository;
        this.engineTypeRepository = engineTypeRepository;
        this.carTypeRepository = carTypeRepository;
        this.carStatusRepository = carStatusRepository;
    }

    public CarDTO convertCarToCarDTO(Car car) {
        return new CarDTO(
                car.getBrand().getBrandName(),
                car.getModel(),
                car.getEngineType().getType(),
                car.getEngineType().getCapacity(),
                car.getCarType().getBodyType(),
                car.getTransmission(),
                car.getYearOfManufacture(),
                car.getCarStatus().getStatus(),
                car.getPrice()
                );
    }

    public List<CarDTO> convertCarListToCarDTOList(List<Car> cars) {
        return cars.stream().map(this::convertCarToCarDTO).collect(Collectors.toList());
    }

    public List<CarDTO> findAllCarDTO() {
        return convertCarListToCarDTOList(carRepository.findAll());
    }

    public CarDTO findCarDTOById(Long id) {
        Car car = carRepository.findById(id).orElseThrow(() -> ServiceException.entityNotFound(ExceptionMessages.CAN_NOT_FIND_CAR));
        return convertCarToCarDTO(car);
    }

    public List<CarDTO> findAllCarDTOByBrandName(String brandName) {
        return convertCarListToCarDTOList(carRepository.findAllByBrand(brandName));
    }

    public List<CarDTO> findAllCarDTOByEngine(String type) {
        return convertCarListToCarDTOList(carRepository.findAllByEngine(type));
    }

    public List<CarDTO> findAllCarDTOByStatus(String status) {
        return convertCarListToCarDTOList(carRepository.findAllByStatus(status));
    }

    public CarRequestDTO convertCarToCarRequestDTO(Car car) {
        return new CarRequestDTO(car.getBrand().getId(),
                car.getEngineType().getId(),
                car.getCarType().getId(),
                car.getModel(),
                car.getTransmission(),
                car.getYearOfManufacture(),
                car.getCarStatus().getId(),
                car.getPrice());
    }

    public CarRequestDTO createNewCar(CarRequestDTO carRequestDTO) {

        Brand brand = brandRepository.findById(carRequestDTO.getBrandId()).orElseThrow(() -> ServiceException.entityNotFound(ExceptionMessages.CAN_NOT_FIND_BRAND));

        EngineType engineType = engineTypeRepository.findById(carRequestDTO.getEngineTypeId()).orElseThrow(() -> ServiceException.entityNotFound(ExceptionMessages.CAN_NOT_FIND_ENGINE));

        CarType carType = carTypeRepository.findById(carRequestDTO.getCarTypeId()).orElseThrow(() -> ServiceException.entityNotFound(ExceptionMessages.CAN_NOT_FIND_CAR_TYPE));

        CarStatus carStatus = carStatusRepository.findById(carRequestDTO.getCarStatusId()).orElseThrow(() -> ServiceException.entityNotFound(ExceptionMessages.CAN_NOT_FIND_CAR_STATUS));

        Car car = new Car(brand,
                engineType,
                carType,
                carRequestDTO.getModel(),
                carRequestDTO.getTransmission(),
                carRequestDTO.getYearOfManufacture(),
                carStatus,
                carRequestDTO.getPrice());

        return convertCarToCarRequestDTO(carRepository.save(car));
    }

    public void updateCar(Long id, CarRequestDTO carRequestDTO) {
        Car car = carRepository.findById(id).orElseThrow(() -> ServiceException.entityNotFound(ExceptionMessages.CAN_NOT_FIND_CAR));
        car.setBrand(brandRepository.findById(carRequestDTO.getBrandId()).orElseThrow(() -> ServiceException.entityNotFound(ExceptionMessages.CAN_NOT_FIND_BRAND)));
        car.setEngineType(engineTypeRepository.findById(carRequestDTO.getEngineTypeId()).orElseThrow(() -> ServiceException.entityNotFound(ExceptionMessages.CAN_NOT_FIND_ENGINE)));
        car.setCarType(carTypeRepository.findById(carRequestDTO.getCarTypeId()).orElseThrow(() -> ServiceException.entityNotFound(ExceptionMessages.CAN_NOT_FIND_CAR_TYPE)));
        car.setModel(carRequestDTO.getModel());
        car.setTransmission(carRequestDTO.getTransmission());
        car.setYearOfManufacture(carRequestDTO.getYearOfManufacture());
        car.setCarStatus(carStatusRepository.findById(carRequestDTO.getCarStatusId()).orElseThrow(() -> ServiceException.entityNotFound(ExceptionMessages.CAN_NOT_FIND_CAR_STATUS)));
        car.setPrice(carRequestDTO.getPrice());

        convertCarToCarRequestDTO(carRepository.save(car));
    }

    public void deleteById(Long id) {
        carRepository.deleteById(id);
    }

}
