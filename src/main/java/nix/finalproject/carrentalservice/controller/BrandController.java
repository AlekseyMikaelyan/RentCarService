package nix.finalproject.carrentalservice.controller;

import nix.finalproject.carrentalservice.dto.BrandDTO;
import nix.finalproject.carrentalservice.dto.request.BrandRequestDTO;
import nix.finalproject.carrentalservice.entity.Brand;
import nix.finalproject.carrentalservice.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/brands")
public class BrandController {

    private final BrandService brandService;

    @Autowired
    public BrandController(BrandService brandService) {
        this.brandService = brandService;
    }

    @GetMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<BrandDTO> findAll() {
        return brandService.findAllBrandDTO();
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public BrandDTO findById(@PathVariable("id") Long id) {
        return brandService.findBrandDTOById(id);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public BrandRequestDTO createNewBrand(@RequestBody Brand brand) {
        return brandService.createNewBrand(brand);
    }

    @DeleteMapping(value = "/{id}")
    public void deleteById(@PathVariable("id") Long id) {
        brandService.deleteById(id);
    }
}
