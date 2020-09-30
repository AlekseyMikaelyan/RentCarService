package nix.finalproject.carrentalservice.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class OrderRequestDTO {

    private Long id;

    private Long clientId;

    private Long carId;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate startOrder;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate endOrder;

    public OrderRequestDTO(Long clientId, Long carId, LocalDate startOrder, LocalDate endOrder) {
        this.clientId = clientId;
        this.carId = carId;
        this.startOrder = startOrder;
        this.endOrder = endOrder;
    }
}
