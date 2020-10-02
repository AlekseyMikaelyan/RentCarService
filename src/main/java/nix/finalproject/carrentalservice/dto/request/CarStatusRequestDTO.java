package nix.finalproject.carrentalservice.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
public class CarStatusRequestDTO {

    private Long id;

    private String status;

    public CarStatusRequestDTO(String status) {
        this.status = status;
    }
}
