package nix.finalproject.carrentalservice.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CarTypeRequestDTO {

    private Long id;

    private String bodyType;

    public CarTypeRequestDTO(String bodyType) {
        this.bodyType = bodyType;
    }
}
