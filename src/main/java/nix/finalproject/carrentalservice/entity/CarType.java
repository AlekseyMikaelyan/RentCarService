package nix.finalproject.carrentalservice.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "car_types")
public class CarType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "body_type", nullable = false)
    private String bodyType;

    @OneToMany(mappedBy = "carType")
    private List<Car> cars;

    public CarType(Long id, String bodyType) {
        this.id = id;
        this.bodyType = bodyType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CarType carType = (CarType) o;
        return Objects.equals(id, carType.id) &&
                Objects.equals(bodyType, carType.bodyType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, bodyType);
    }
}
