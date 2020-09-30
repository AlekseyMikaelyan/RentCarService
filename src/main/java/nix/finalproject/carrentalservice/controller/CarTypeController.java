package nix.finalproject.carrentalservice.controller;

import nix.finalproject.carrentalservice.dto.CarTypeDTO;
import nix.finalproject.carrentalservice.dto.request.CarTypeRequestDTO;
import nix.finalproject.carrentalservice.entity.CarType;
import nix.finalproject.carrentalservice.service.CarTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/car_types")
public class CarTypeController {

    private final CarTypeService carTypeService;

    @Autowired
    public CarTypeController(CarTypeService carTypeService) {
        this.carTypeService = carTypeService;
    }

    @GetMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<CarTypeDTO> findAll() {
        return carTypeService.findAllCarTypeDTO();
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CarTypeDTO findById(@PathVariable("id") Long id) {
        return carTypeService.findCarTypeDTOById(id);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public CarTypeRequestDTO createNewType(@RequestBody CarType carType) {
        return carTypeService.createNewType(carType);
    }

    @DeleteMapping(value = "/{id}")
    public void deleteById(@PathVariable("id") Long id) {
        carTypeService.deleteById(id);
    }

}
