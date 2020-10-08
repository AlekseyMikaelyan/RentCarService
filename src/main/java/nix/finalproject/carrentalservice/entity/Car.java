package nix.finalproject.carrentalservice.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "cars")
public class Car {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "brand_id")
    private Brand brand;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "engine_type_id")
    private EngineType engineType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "car_type_id")
    private CarType carType;

    @Column(name = "model", nullable = false)
    private String model;

    @Column(name = "transmission", nullable = false)
    private String transmission;

    @Column(name = "year_of_manufacture", nullable = false)
    private int yearOfManufacture;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "car_status_id")
    private CarStatus carStatus;

    @Column(name = "price", nullable = false)
    private String price;

    @OneToMany(mappedBy = "car", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Order> orders;

    public Car(Long id, Brand brand, EngineType engineType, CarType carType, String model, String transmission, int yearOfManufacture, CarStatus carStatus, String price) {
        this.id = id;
        this.brand = brand;
        this.engineType = engineType;
        this.carType = carType;
        this.model = model;
        this.transmission = transmission;
        this.yearOfManufacture = yearOfManufacture;
        this.carStatus = carStatus;
        this.price = price;
    }

    public Car(Brand brand, EngineType engineType, CarType carType, String model, String transmission, int yearOfManufacture, CarStatus carStatus, String price) {
        this.brand = brand;
        this.engineType = engineType;
        this.carType = carType;
        this.model = model;
        this.transmission = transmission;
        this.yearOfManufacture = yearOfManufacture;
        this.carStatus = carStatus;
        this.price = price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Car car = (Car) o;
        return yearOfManufacture == car.yearOfManufacture &&
                Objects.equals(id, car.id) &&
                Objects.equals(brand, car.brand) &&
                Objects.equals(engineType, car.engineType) &&
                Objects.equals(carType, car.carType) &&
                Objects.equals(model, car.model) &&
                Objects.equals(transmission, car.transmission) &&
                Objects.equals(carStatus, car.carStatus) &&
                Objects.equals(price, car.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, brand, engineType, carType, model, transmission, yearOfManufacture, carStatus, price);
    }
}
