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
public class EngineTypeRequestDTO {

    @NotBlank(message = "Engine type is mandatory")
    private String type;

    @NotBlank(message = "Engine capacity is mandatory")
    private String capacity;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EngineTypeRequestDTO that = (EngineTypeRequestDTO) o;
        return Objects.equals(type, that.type) &&
                Objects.equals(capacity, that.capacity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, capacity);
    }

    @Override
    public String toString() {
        return "EngineTypeRequestDTO{" +
                "type='" + type + '\'' +
                ", capacity='" + capacity + '\'' +
                '}';
    }
}
