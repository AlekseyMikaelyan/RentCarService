package nix.finalproject.carrentalservice.controller;

import nix.finalproject.carrentalservice.dto.CarTypeResponseDTO;
import nix.finalproject.carrentalservice.dto.request.CarTypeRequestDTO;
import nix.finalproject.carrentalservice.exceptions.ExceptionMessages;
import nix.finalproject.carrentalservice.exceptions.ServiceException;
import nix.finalproject.carrentalservice.service.CarTypeServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/car_types")
public class CarTypeController {

    private final CarTypeServiceImpl carTypeServiceImpl;

    @Autowired
    public CarTypeController(CarTypeServiceImpl carTypeServiceImpl) {
        this.carTypeServiceImpl = carTypeServiceImpl;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public CarTypeResponseDTO createNewType(@Valid @RequestBody CarTypeRequestDTO carType) {
        return carTypeServiceImpl.create(carType);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CarTypeResponseDTO findById(@PathVariable Long id) {
        return carTypeServiceImpl.getById(id)
                .orElseThrow(() -> ServiceException.entityNotFound(ExceptionMessages.CAN_NOT_FIND_CAR_TYPE));
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/{id}")
    public void updateCarType(@PathVariable Long id, @Valid @RequestBody CarTypeRequestDTO request) {
        carTypeServiceImpl.update(id, request);
    }

    @DeleteMapping("/{id}")
    public CarTypeResponseDTO deleteById(@PathVariable Long id) {
        return carTypeServiceImpl.deleteById(id)
                .orElseThrow(() -> ServiceException.entityNotFound(ExceptionMessages.CAN_NOT_FIND_CAR_TYPE));
    }

    @GetMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<CarTypeResponseDTO> findAll() {
        return carTypeServiceImpl.findAll();
    }
}
