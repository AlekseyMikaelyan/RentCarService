package nix.finalproject.carrentalservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nix.finalproject.carrentalservice.entity.CarStatus;

import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CarStatusResponseDTO {

    private Long id;

    private String status;

    public static CarStatusResponseDTO fromCarStatus(CarStatus carStatus) {
        return new CarStatusResponseDTO(carStatus.getId(), carStatus.getStatus());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CarStatusResponseDTO that = (CarStatusResponseDTO) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(status, that.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, status);
    }

    @Override
    public String toString() {
        return "CarStatusResponseDTO{" +
                "id=" + id +
                ", status='" + status + '\'' +
                '}';
    }
}
