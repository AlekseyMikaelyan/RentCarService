package nix.finalproject.carrentalservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nix.finalproject.carrentalservice.entity.Brand;

import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BrandResponseDTO {

    private Long id;

    private String brandName;

    public static BrandResponseDTO fromBrand(Brand brand) {
        return new BrandResponseDTO(brand.getId(), brand.getBrandName());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BrandResponseDTO that = (BrandResponseDTO) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(brandName, that.brandName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, brandName);
    }

    @Override
    public String toString() {
        return "BrandResponseDTO{" +
                "id=" + id +
                ", brandName='" + brandName + '\'' +
                '}';
    }
}
