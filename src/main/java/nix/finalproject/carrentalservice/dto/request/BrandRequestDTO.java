package nix.finalproject.carrentalservice.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BrandRequestDTO {

    @NotBlank(message = "Brand name is mandatory")
    private String brandName;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BrandRequestDTO that = (BrandRequestDTO) o;
        return Objects.equals(brandName, that.brandName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(brandName);
    }

    @Override
    public String toString() {
        return "BrandRequestDTO{" +
                "brandName='" + brandName + '\'' +
                '}';
    }
}
