package nix.finalproject.carrentalservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nix.finalproject.carrentalservice.entity.EngineType;

import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EngineTypeResponseDTO {

    private Long id;

    private String type;

    private String engineCapacity;

    public static EngineTypeResponseDTO fromEngineType(EngineType engineType) {
        return new EngineTypeResponseDTO(engineType.getId(), engineType.getType(), engineType.getCapacity());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EngineTypeResponseDTO that = (EngineTypeResponseDTO) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(type, that.type) &&
                Objects.equals(engineCapacity, that.engineCapacity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, type, engineCapacity);
    }

    @Override
    public String toString() {
        return "EngineTypeResponseDTO{" +
                "id=" + id +
                ", type='" + type + '\'' +
                ", engineCapacity='" + engineCapacity + '\'' +
                '}';
    }
}
