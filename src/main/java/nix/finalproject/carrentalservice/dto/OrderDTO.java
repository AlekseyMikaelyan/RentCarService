package nix.finalproject.carrentalservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {

    private Long id;

    private String clientFirstName;

    private String clientLastName;

    private String clientPhoneNumber;

    private String carBrand;

    private String carModel;

    private LocalDate startOrder;

    private LocalDate endOrder;

    private String totalPrice;

}
