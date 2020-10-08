package nix.finalproject.carrentalservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nix.finalproject.carrentalservice.entity.CarType;

import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CarTypeResponseDTO {

    private Long id;

    private String bodyType;

    public static CarTypeResponseDTO fromCarType(CarType carType) {
        return new CarTypeResponseDTO(carType.getId(), carType.getBodyType());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CarTypeResponseDTO that = (CarTypeResponseDTO) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(bodyType, that.bodyType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, bodyType);
    }

    @Override
    public String toString() {
        return "CarTypeResponseDTO{" +
                "id=" + id +
                ", bodyType='" + bodyType + '\'' +
                '}';
    }
}
