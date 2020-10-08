package nix.finalproject.carrentalservice.service;

import nix.finalproject.carrentalservice.dto.BrandResponseDTO;
import nix.finalproject.carrentalservice.dto.request.BrandRequestDTO;
import nix.finalproject.carrentalservice.entity.Brand;
import nix.finalproject.carrentalservice.exceptions.ExceptionMessages;
import nix.finalproject.carrentalservice.exceptions.ServiceException;
import nix.finalproject.carrentalservice.repository.BrandRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

public class BrandServiceImplTest {

    @Mock
    private BrandRepository brandRepository;

    private final BrandServiceImpl brandServiceImpl;

    public BrandServiceImplTest() {
        MockitoAnnotations.initMocks(this);
        this.brandServiceImpl = new BrandServiceImpl(brandRepository);
    }

    @Test
    public void getBrandByIdTest() {

        Long absentId = 1L;
        Long presentId = 2L;

        Brand brand = new Brand(presentId, "Renault");
        when(brandRepository.findById(absentId)).thenReturn(Optional.empty());
        when(brandRepository.findById(presentId)).thenReturn(Optional.of(brand));

        Optional<BrandResponseDTO> absentResponse = brandServiceImpl.getById(absentId);

        assertThat(absentResponse).isEmpty();
        verify(brandRepository).findById(absentId);

        Optional<BrandResponseDTO> presentResponse = brandServiceImpl.getById(presentId);

        assertThat(presentResponse).hasValueSatisfying(brandResponse ->
                assertBrandMatchesResponse(brand, brandResponse));
        verify(brandRepository).findById(presentId);

        verifyNoMoreInteractions(brandRepository);
    }

    @Test()
    public void methodShouldThrowException() {
        Long absentId = 20L;
        Long presentId = 1L;

        Brand brand = new Brand(presentId, "Audi");

        given(brandRepository.findById(absentId)).willThrow(ServiceException.entityNotFound(ExceptionMessages.CAN_NOT_FIND_BRAND));
        given(brandRepository.findById(presentId)).willReturn(Optional.of(brand));

        assertThrows(ResponseStatusException.class, () -> brandServiceImpl.getById(absentId));

        verify(brandRepository).findById(absentId);
    }

    @Test
    public void updateBrandByIdTest() {
        Long presentId = 1L;
        Long absentId = 2L;

        BrandRequestDTO update = new BrandRequestDTO("Renault");

        Brand brand = new Brand(presentId, "Peugeot");

        when(brandRepository.findById(absentId)).thenReturn(Optional.empty());
        when(brandRepository.findById(presentId)).thenReturn(Optional.of(brand));
        when(brandRepository.save(same(brand))).thenReturn(brand);

        assertThatExceptionOfType(ResponseStatusException.class)
                .isThrownBy(() -> brandServiceImpl.update(absentId, update))
                .satisfies(e -> assertThat(e.getStatus()).isSameAs(HttpStatus.NOT_FOUND));

        verify(brandRepository).findById(absentId);

        brandServiceImpl.update(presentId, update);

        assertThat(brand.getBrandName()).isEqualTo(update.getBrandName());
        verify(brandRepository).findById(presentId);
        verify(brandRepository).save(same(brand));

        verifyNoMoreInteractions(brandRepository);
    }

    @Test
    public void methodShouldReturnAllBrands() {
        List<Brand> brands = new ArrayList<>();
        brands.add(new Brand(1L, "Audi"));
        brands.add(new Brand(2L, "BMW"));
        brands.add(new Brand(3L, "Mercedes"));

        given(brandRepository.findAll()).willReturn(brands);

        List<BrandResponseDTO> dtoBrands = brandServiceImpl.findAll();

        assertEquals(brands.get(0).getBrandName(), dtoBrands.get(0).getBrandName());
        assertEquals(brands.get(1).getBrandName(), dtoBrands.get(1).getBrandName());
        assertEquals(brands.get(2).getBrandName(), dtoBrands.get(2).getBrandName());
    }

    @Test
    public void createNewBrandTest() {

        BrandRequestDTO request = new BrandRequestDTO("Renault");
        Long brandId = 1L;

        when(brandRepository.save(notNull())).thenAnswer(invocation -> {
            Brand entity = invocation.getArgument(0);
            assertThat(entity.getId()).isNull();
            assertThat(entity.getBrandName()).isEqualTo(request.getBrandName());
            entity.setId(brandId);
            return entity;
        });

        BrandResponseDTO response = brandServiceImpl.create(request);

        assertThat(response.getId()).isEqualTo(brandId);
        assertThat(response.getBrandName()).isEqualTo(request.getBrandName());
        verify(brandRepository, only()).save(notNull());
    }

    @Test
    public void deleteBrandTest() {

        Long absentId = 20L;
        Long presentId = 1L;
        Brand brand = new Brand(presentId, "Renault");

        when(brandRepository.findById(absentId)).thenReturn(Optional.empty());
        when(brandRepository.findById(presentId)).thenReturn(Optional.of(brand));

        Optional<BrandResponseDTO> absentResponse = brandServiceImpl.deleteById(absentId);

        assertThat(absentResponse).isEmpty();
        verify(brandRepository).findById(absentId);

        Optional<BrandResponseDTO> presentResponse = brandServiceImpl.deleteById(presentId);

        assertThat(presentResponse).hasValueSatisfying(brandResponse ->
                assertBrandMatchesResponse(brand, brandResponse));
        verify(brandRepository).findById(presentId);
        verify(brandRepository).delete(brand);

        verifyNoMoreInteractions(brandRepository);
    }

    private static void assertBrandMatchesResponse(Brand brand, BrandResponseDTO brandResponseDTO) {
        assertThat(brandResponseDTO.getId()).isEqualTo(brand.getId());
        assertThat(brandResponseDTO.getBrandName()).isEqualTo(brand.getBrandName());
    }
}
