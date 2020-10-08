package nix.finalproject.carrentalservice.service;

import nix.finalproject.carrentalservice.dto.BrandResponseDTO;
import nix.finalproject.carrentalservice.dto.request.BrandRequestDTO;
import nix.finalproject.carrentalservice.entity.Brand;
import nix.finalproject.carrentalservice.exceptions.ExceptionMessages;
import nix.finalproject.carrentalservice.exceptions.ServiceException;
import nix.finalproject.carrentalservice.repository.BrandRepository;
import nix.finalproject.carrentalservice.service.interfaces.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class BrandServiceImpl implements BrandService {

    private final BrandRepository brandRepository;

    @Autowired
    public BrandServiceImpl(BrandRepository brandRepository) {
        this.brandRepository = brandRepository;
    }


    public List<BrandResponseDTO> convertBrandListToBrandResponseDTOList(List<Brand> brands) {
        return brands.stream().map(BrandResponseDTO::fromBrand).collect(Collectors.toList());
    }

    @Override
    public BrandResponseDTO create(BrandRequestDTO request) {
        var brand = new Brand(request.getBrandName());
        return BrandResponseDTO.fromBrand(brandRepository.save(brand));
    }

    @Override
    public Optional<BrandResponseDTO> getById(Long id) {
        return brandRepository.findById(id).map(BrandResponseDTO::fromBrand);
    }

    @Override
    public void update(Long id, BrandRequestDTO request) {
        var brand = brandRepository.findById(id).orElseThrow(() -> ServiceException.entityNotFound(ExceptionMessages.CAN_NOT_FIND_BRAND));
        brand.setBrandName(request.getBrandName());
        brandRepository.save(brand);
    }

    @Override
    public Optional<BrandResponseDTO> deleteById(Long id) {
        var brand = brandRepository.findById(id);
        brand.ifPresent(brandRepository::delete);
        return brand.map(BrandResponseDTO::fromBrand);
    }

    public List<BrandResponseDTO> findAll() {
        return convertBrandListToBrandResponseDTOList(brandRepository.findAll());
    }
}
