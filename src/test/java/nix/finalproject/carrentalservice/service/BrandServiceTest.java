package nix.finalproject.carrentalservice.service;

import nix.finalproject.carrentalservice.dto.BrandDTO;
import nix.finalproject.carrentalservice.dto.request.BrandRequestDTO;
import nix.finalproject.carrentalservice.entity.Brand;
import nix.finalproject.carrentalservice.exceptions.ExceptionMessages;
import nix.finalproject.carrentalservice.exceptions.ServiceException;
import nix.finalproject.carrentalservice.repository.BrandRepository;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

public class BrandServiceTest {

    @Mock
    private BrandRepository brandRepository;

    private final BrandService brandService;

    public BrandServiceTest() {
        MockitoAnnotations.initMocks(this);
        this.brandService = new BrandService(brandRepository);
    }

    @Test
    public void methodShouldReturnCorrectBrandName() {
        Long id1 = 1L;
        Long id2 = 2L;

        Brand brand1 = new Brand(id1, "Audi");
        Brand brand2 = new Brand(id2, "BMW");

        given(brandRepository.findById(id1)).willReturn(Optional.of(brand1));
        given(brandRepository.findById(id2)).willReturn(Optional.of(brand2));

        BrandDTO brandDTO1 = brandService.findBrandDTOById(id1);
        BrandDTO brandDTO2 = brandService.findBrandDTOById(id2);

        Assert.assertEquals("Audi", brandDTO1.getBrandName());
        Assert.assertEquals("BMW", brandDTO2.getBrandName());

        verify(brandRepository).findById(id1);
        verify(brandRepository).findById(id2);
    }

    @Test(expected = ResponseStatusException.class)
    public void methodShouldThrowException() {
        Long absentId = 20L;
        Long presentId = 1L;

        Brand brand = new Brand(presentId, "Audi");

        given(brandRepository.findById(absentId)).willThrow(ServiceException.entityNotFound(ExceptionMessages.CAN_NOT_FIND_BRAND));
        given(brandRepository.findById(presentId)).willReturn(Optional.of(brand));

        BrandDTO brandDTO = brandService.findBrandDTOById(absentId);
        assertThat(brandDTO).isNull();

        verify(brandRepository).findById(absentId);
    }

    @Test
    public void methodShouldReturnAllBrands() {
        List<Brand> brands = new ArrayList<>();
        brands.add(new Brand(1L, "Audi"));
        brands.add(new Brand(2L, "BMW"));
        brands.add(new Brand(3L, "Mercedes"));

        given(brandRepository.findAll()).willReturn(brands);

        List<BrandDTO> dtoBrands = brandService.findAllBrandDTO();

        Assert.assertEquals(brands.get(0).getBrandName(), dtoBrands.get(0).getBrandName());
        Assert.assertEquals(brands.get(1).getBrandName(), dtoBrands.get(1).getBrandName());
        Assert.assertEquals(brands.get(2).getBrandName(), dtoBrands.get(2).getBrandName());
    }

    @Test
    public void methodShouldSaveNewBrandSuccessfully() {
        Brand brand = new Brand(1L, "Peugeot");

        given(brandRepository.findById(brand.getId())).willReturn(Optional.empty());
        given(brandRepository.save(brand)).willAnswer(invocation -> invocation.getArgument(0));

        BrandRequestDTO brandRequestDTO = brandService.createNewBrand(brand);

        assertThat(brandRequestDTO).isNotNull();
        assertThat(brandRequestDTO.getBrandName()).isEqualTo(brand.getBrandName());

        verify(brandRepository).save(any(Brand.class));
    }

    @Test
    public void methodShouldDeleteBrand() {
        Long id = 1L;

        Brand brand = new Brand(id, "Audi");

        when(brandRepository.findById(id)).thenReturn(Optional.of(brand));

        brandService.deleteById(id);

        verify(brandRepository).deleteById(id);
    }

}
