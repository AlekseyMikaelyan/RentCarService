package nix.finalproject.carrentalservice.service;

import nix.finalproject.carrentalservice.dto.BrandDTO;
import nix.finalproject.carrentalservice.dto.request.BrandRequestDTO;
import nix.finalproject.carrentalservice.entity.Brand;
import nix.finalproject.carrentalservice.exceptions.ExceptionMessages;
import nix.finalproject.carrentalservice.exceptions.ServiceException;
import nix.finalproject.carrentalservice.repository.BrandRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class BrandService {

    private final BrandRepository brandRepository;

    @Autowired
    public BrandService(BrandRepository brandRepository) {
        this.brandRepository = brandRepository;
    }

    public BrandDTO convertBrandToBrandDTO(Brand brand) {
        return new BrandDTO(brand.getId(), brand.getBrandName());
    }

    public List<BrandDTO> convertBrandListToBrandDTOList(List<Brand> brands) {
        return brands.stream().map(this::convertBrandToBrandDTO).collect(Collectors.toList());
    }

    public List<BrandDTO> findAllBrandDTO() {
        return convertBrandListToBrandDTOList(brandRepository.findAll());
    }

    public BrandDTO findBrandDTOById(Long id) {
        Brand brand = brandRepository.findById(id).orElseThrow(() -> ServiceException.entityNotFound(ExceptionMessages.CAN_NOT_FIND_BRAND));
        return convertBrandToBrandDTO(brand);
    }

    public BrandRequestDTO convertBrandToBrandRequestDTO(Brand brand) {
        return new BrandRequestDTO(brand.getBrandName());
    }

    public BrandRequestDTO createNewBrand(Brand brand) {
        return convertBrandToBrandRequestDTO(brandRepository.save(brand));
    }

    public void deleteById(Long id) {
        brandRepository.deleteById(id);
    }
}
