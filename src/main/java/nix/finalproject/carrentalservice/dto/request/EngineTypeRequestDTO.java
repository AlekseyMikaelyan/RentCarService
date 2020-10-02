package nix.finalproject.carrentalservice.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
public class EngineTypeRequestDTO {

    private Long id;

    private String type;

    private String capacity;

    public EngineTypeRequestDTO(String type, String capacity) {
        this.type = type;
        this.capacity = capacity;
    }
}
