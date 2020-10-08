package nix.finalproject.carrentalservice.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequestDTO {

    private Long clientId;

    private Long carId;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate startOrder;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate endOrder;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderRequestDTO that = (OrderRequestDTO) o;
        return Objects.equals(clientId, that.clientId) &&
                Objects.equals(carId, that.carId) &&
                Objects.equals(startOrder, that.startOrder) &&
                Objects.equals(endOrder, that.endOrder);
    }

    @Override
    public int hashCode() {
        return Objects.hash(clientId, carId, startOrder, endOrder);
    }

    @Override
    public String toString() {
        return "OrderRequestDTO{" +
                "clientId=" + clientId +
                ", carId=" + carId +
                ", startOrder=" + startOrder +
                ", endOrder=" + endOrder +
                '}';
    }
}
