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
public class CarRequestDTO {

    private Long brandId;

    private Long engineTypeId;

    private Long carTypeId;

    @NotBlank(message = "Model is mandatory")
    private String model;

    @NotBlank(message = "Transmission type is mandatory")
    private String transmission;

    private int yearOfManufacture;

    private Long carStatusId;

    @NotBlank(message = "You must enter the price")
    private String price;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CarRequestDTO that = (CarRequestDTO) o;
        return yearOfManufacture == that.yearOfManufacture &&
                Objects.equals(brandId, that.brandId) &&
                Objects.equals(engineTypeId, that.engineTypeId) &&
                Objects.equals(carTypeId, that.carTypeId) &&
                Objects.equals(model, that.model) &&
                Objects.equals(transmission, that.transmission) &&
                Objects.equals(carStatusId, that.carStatusId) &&
                Objects.equals(price, that.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(brandId, engineTypeId, carTypeId, model, transmission, yearOfManufacture, carStatusId, price);
    }

    @Override
    public String toString() {
        return "CarRequestDTO{" +
                "brandId=" + brandId +
                ", engineTypeId=" + engineTypeId +
                ", carTypeId=" + carTypeId +
                ", model='" + model + '\'' +
                ", transmission='" + transmission + '\'' +
                ", yearOfManufacture=" + yearOfManufacture +
                ", carStatusId=" + carStatusId +
                ", price='" + price + '\'' +
                '}';
    }
}
