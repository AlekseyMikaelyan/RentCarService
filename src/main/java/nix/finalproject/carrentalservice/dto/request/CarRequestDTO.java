package nix.finalproject.carrentalservice.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nix.finalproject.carrentalservice.entity.Brand;
import nix.finalproject.carrentalservice.entity.CarStatus;
import nix.finalproject.carrentalservice.entity.CarType;
import nix.finalproject.carrentalservice.entity.EngineType;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CarRequestDTO {

    private Long brandId;

    private Long engineTypeId;

    private Long carTypeId;

    private String model;

    private String transmission;

    private int yearOfManufacture;

    private Long carStatusId;

    private String price;

}
