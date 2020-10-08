package nix.finalproject.carrentalservice.controller;

import nix.finalproject.carrentalservice.dto.CarResponseDTO;
import nix.finalproject.carrentalservice.dto.request.CarRequestDTO;
import nix.finalproject.carrentalservice.exceptions.ExceptionMessages;
import nix.finalproject.carrentalservice.exceptions.ServiceException;
import nix.finalproject.carrentalservice.service.CarServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/cars")
public class CarController {

    private final CarServiceImpl carServiceImpl;

    @Autowired
    public CarController(CarServiceImpl carServiceImpl) {
        this.carServiceImpl = carServiceImpl;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public CarResponseDTO createNewCar(@Valid @RequestBody CarRequestDTO car) {
        return carServiceImpl.create(car);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CarResponseDTO findById(@PathVariable Long id) {
        return carServiceImpl.getById(id)
                .orElseThrow(() -> ServiceException.entityNotFound(ExceptionMessages.CAN_NOT_FIND_CAR));
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/{id}")
    public void updateCar(@PathVariable Long id, @Valid @RequestBody CarRequestDTO request) {
        carServiceImpl.update(id, request);
    }

    @DeleteMapping("/{id}")
    public CarResponseDTO deleteById(@PathVariable Long id) {
        return carServiceImpl.deleteById(id)
                .orElseThrow(() -> ServiceException.entityNotFound(ExceptionMessages.CAN_NOT_FIND_CAR));
    }

    @GetMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<CarResponseDTO> findAll() {
        return carServiceImpl.findAll();
    }

    @GetMapping(value = "/brand", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<CarResponseDTO> findByBrand(@RequestParam("brandName") String brandName) {
        return carServiceImpl.findAllCarDTOByBrandName(brandName);
    }

    @GetMapping(value = "/engine", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<CarResponseDTO> findByEngine(@RequestParam("type") String type) {
        return carServiceImpl.findAllCarDTOByEngine(type);
    }

    @GetMapping(value = "/status", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<CarResponseDTO> findByStatus(@RequestParam("status") String status) {
        return carServiceImpl.findAllCarDTOByStatus(status);
    }

}
