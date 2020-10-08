package nix.finalproject.carrentalservice.controller;

import nix.finalproject.carrentalservice.dto.CarStatusResponseDTO;
import nix.finalproject.carrentalservice.dto.request.CarStatusRequestDTO;
import nix.finalproject.carrentalservice.exceptions.ExceptionMessages;
import nix.finalproject.carrentalservice.exceptions.ServiceException;
import nix.finalproject.carrentalservice.service.CarStatusServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/car_status")
public class CarStatusController {

    private final CarStatusServiceImpl carStatusServiceImpl;

    @Autowired
    public CarStatusController(CarStatusServiceImpl carStatusServiceImpl) {
        this.carStatusServiceImpl = carStatusServiceImpl;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public CarStatusResponseDTO createNewStatus(@Valid @RequestBody CarStatusRequestDTO request) {
        return carStatusServiceImpl.create(request);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CarStatusResponseDTO findById(@PathVariable Long id) {
        return carStatusServiceImpl.getById(id)
                .orElseThrow(() -> ServiceException.entityNotFound(ExceptionMessages.CAN_NOT_FIND_CAR_STATUS));
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/{id}")
    public void updateCarStatus(@PathVariable Long id, @Valid @RequestBody CarStatusRequestDTO request) {
        carStatusServiceImpl.update(id, request);
    }

    @DeleteMapping("/{id}")
    public CarStatusResponseDTO deleteById(@PathVariable Long id) {
        return carStatusServiceImpl.deleteById(id)
                .orElseThrow(() -> ServiceException.entityNotFound(ExceptionMessages.CAN_NOT_FIND_CAR_STATUS));
    }

    @GetMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<CarStatusResponseDTO> findAll() {
        return carStatusServiceImpl.findAll();
    }
}
