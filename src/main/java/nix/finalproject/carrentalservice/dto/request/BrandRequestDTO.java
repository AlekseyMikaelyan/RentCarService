package nix.finalproject.carrentalservice.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BrandRequestDTO {

    private Long id;

    private String brandName;

    public BrandRequestDTO(String brandName) {
        this.brandName = brandName;
    }
}
