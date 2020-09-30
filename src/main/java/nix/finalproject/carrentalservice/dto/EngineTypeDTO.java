package nix.finalproject.carrentalservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EngineTypeDTO {

    private Long id;

    private String type;

    private String engineCapacity;

}
