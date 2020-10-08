package nix.finalproject.carrentalservice.controller;

import nix.finalproject.carrentalservice.dto.BrandResponseDTO;
import nix.finalproject.carrentalservice.dto.request.BrandRequestDTO;
import nix.finalproject.carrentalservice.exceptions.ExceptionMessages;
import nix.finalproject.carrentalservice.exceptions.ServiceException;
import nix.finalproject.carrentalservice.service.BrandServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/brands")
public class BrandController {

    private final BrandServiceImpl brandServiceImpl;

    @Autowired
    public BrandController(BrandServiceImpl brandServiceImpl) {
        this.brandServiceImpl = brandServiceImpl;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public BrandResponseDTO createNewBrand(@Valid @RequestBody BrandRequestDTO request) {
        return brandServiceImpl.create(request);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public BrandResponseDTO findById(@PathVariable Long id) {
        return brandServiceImpl.getById(id)
                .orElseThrow(() -> ServiceException.entityNotFound(ExceptionMessages.CAN_NOT_FIND_BRAND));
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/{id}")
    public void updateBrand(@PathVariable Long id, @Valid @RequestBody BrandRequestDTO request) {
        brandServiceImpl.update(id, request);
    }

    @DeleteMapping("/{id}")
    public BrandResponseDTO deleteById(@PathVariable Long id) {
        return brandServiceImpl.deleteById(id)
                .orElseThrow(() -> ServiceException.entityNotFound(ExceptionMessages.CAN_NOT_FIND_BRAND));
    }

    @GetMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<BrandResponseDTO> findAll() {
        return brandServiceImpl.findAll();
    }
}
