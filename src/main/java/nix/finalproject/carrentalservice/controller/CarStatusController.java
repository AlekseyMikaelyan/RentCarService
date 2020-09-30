package nix.finalproject.carrentalservice.controller;

import nix.finalproject.carrentalservice.dto.CarStatusDTO;
import nix.finalproject.carrentalservice.dto.request.CarStatusRequestDTO;
import nix.finalproject.carrentalservice.entity.CarStatus;
import nix.finalproject.carrentalservice.service.CarStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/car_status")
public class CarStatusController {

    private final CarStatusService carStatusService;

    @Autowired
    public CarStatusController(CarStatusService carStatusService) {
        this.carStatusService = carStatusService;
    }

    @GetMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<CarStatusDTO> findAll() {
        return carStatusService.findAllCarStatusDTO();
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CarStatusDTO findById(@PathVariable("id") Long id) {
        return carStatusService.findCarStatusDTOById(id);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public CarStatusRequestDTO createNewStatus(@RequestBody CarStatus carStatus) {
        return carStatusService.createNewStatus(carStatus);
    }

    @DeleteMapping(value = "/{id}")
    public void deleteById(@PathVariable("id") Long id) {
        carStatusService.deleteById(id);
    }
}
