package nix.finalproject.carrentalservice.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CarDTO {

    private String brand;

    private String model;

    private String engineType;

    private String engineCapacity;

    private String carType;

    private String transmission;

    private int yearOfManufacture;

    private String carStatus;

    private String price;

    public CarDTO(String brand, String model, String engineType, String engineCapacity, String carType, String transmission, int yearOfManufacture, String carStatus, String price) {
        this.brand = brand;
        this.model = model;
        this.engineType = engineType;
        this.engineCapacity = engineCapacity;
        this.carType = carType;
        this.transmission = transmission;
        this.yearOfManufacture = yearOfManufacture;
        this.carStatus = carStatus;
        this.price = price;
    }

}
