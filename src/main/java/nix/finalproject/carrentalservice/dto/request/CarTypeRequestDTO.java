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
public class CarTypeRequestDTO {

    @NotBlank(message = "Body Type is mandatory")
    private String bodyType;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CarTypeRequestDTO that = (CarTypeRequestDTO) o;
        return Objects.equals(bodyType, that.bodyType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bodyType);
    }

    @Override
    public String toString() {
        return "CarTypeRequestDTO{" +
                "bodyType='" + bodyType + '\'' +
                '}';
    }
}
