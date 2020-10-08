package nix.finalproject.carrentalservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nix.finalproject.carrentalservice.entity.Car;

import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CarResponseDTO {

    private Long id;

    private String brand;

    private String model;

    private String engineType;

    private String engineCapacity;

    private String carType;

    private String transmission;

    private int yearOfManufacture;

    private String carStatus;

    private String price;

    public static CarResponseDTO fromCar(Car car) {
        return new CarResponseDTO(
                car.getId(),
                car.getBrand().getBrandName(),
                car.getModel(),
                car.getEngineType().getType(),
                car.getEngineType().getCapacity(),
                car.getCarType().getBodyType(),
                car.getTransmission(),
                car.getYearOfManufacture(),
                car.getCarStatus().getStatus(),
                car.getPrice()
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CarResponseDTO that = (CarResponseDTO) o;
        return yearOfManufacture == that.yearOfManufacture &&
                Objects.equals(id, that.id) &&
                Objects.equals(brand, that.brand) &&
                Objects.equals(model, that.model) &&
                Objects.equals(engineType, that.engineType) &&
                Objects.equals(engineCapacity, that.engineCapacity) &&
                Objects.equals(carType, that.carType) &&
                Objects.equals(transmission, that.transmission) &&
                Objects.equals(carStatus, that.carStatus) &&
                Objects.equals(price, that.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, brand, model, engineType, engineCapacity, carType, transmission, yearOfManufacture, carStatus, price);
    }

    @Override
    public String toString() {
        return "CarResponseDTO{" +
                "id=" + id +
                ", brand='" + brand + '\'' +
                ", model='" + model + '\'' +
                ", engineType='" + engineType + '\'' +
                ", engineCapacity='" + engineCapacity + '\'' +
                ", carType='" + carType + '\'' +
                ", transmission='" + transmission + '\'' +
                ", yearOfManufacture=" + yearOfManufacture +
                ", carStatus='" + carStatus + '\'' +
                ", price='" + price + '\'' +
                '}';
    }
}
