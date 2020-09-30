package nix.finalproject.carrentalservice.controller;

import nix.finalproject.carrentalservice.dto.CarDTO;
import nix.finalproject.carrentalservice.dto.request.CarRequestDTO;
import nix.finalproject.carrentalservice.service.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cars")
public class CarController {

    private final CarService carService;

    @Autowired
    public CarController(CarService carService) {
        this.carService = carService;
    }

    @GetMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<CarDTO> findAll() {
        return carService.findAllCarDTO();
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CarDTO findById(@PathVariable("id") Long id) {
        return carService.findCarDTOById(id);
    }

    @GetMapping(value = "/brand", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<CarDTO> findByBrand(@RequestParam("brandName") String brandName) {
        return carService.findAllCarDTOByBrandName(brandName);
    }

    @GetMapping(value = "/engine", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<CarDTO> findByEngine(@RequestParam("type") String type) {
        return carService.findAllCarDTOByEngine(type);
    }

    @GetMapping(value = "/status", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<CarDTO> findByStatus(@RequestParam("status") String status) {
        return carService.findAllCarDTOByStatus(status);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public CarRequestDTO createNewCar(@RequestBody CarRequestDTO car) {
        return carService.createNewCar(car);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/{id}")
    public void updateCar(@PathVariable Long id, @RequestBody CarRequestDTO carRequestDTO) {
        carService.updateCar(id, carRequestDTO);
    }

    @DeleteMapping(value = "/{id}")
    public void deleteById(@PathVariable("id") Long id) {
        carService.deleteById(id);
    }
}
